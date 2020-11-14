package com.imn.iicnma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentSearchBinding
import com.imn.iicnma.ui.home.ListLoadStateAdapter
import com.imn.iicnma.utils.hideKeyboard
import com.imn.iicnma.utils.showKeyboard
import com.imn.iicnma.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    private val searchAdapter = SearchAdapter(::onMovieClicked)

    private fun onMovieClicked(
        movieId: Long,
        posterImageView: ImageView,
        titleTextView: TextView,
        dateTextView: TextView
    ) {
        val extras = FragmentNavigatorExtras(
            posterImageView to posterImageView.transitionName,
            titleTextView to titleTextView.transitionName,
            dateTextView to dateTextView.transitionName,
        )
        findNavController().navigate(
            SearchFragmentDirections.actionNavigationSearchToMovieDetails(movieId), extras
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSearchBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY)

        populateUI(query)
        search(query)
    }

    private fun populateUI(query: String?) = with(binding) {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()

        editText.apply {
            requestFocus()
            showKeyboard()
            query?.let { setText(it) }
            setOnClickListener { isCursorVisible = true }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateSearchFromInput()
                    true
                } else {
                    false
                }
            }
        }

        searchButton.setOnClickListener {
            updateSearchFromInput()
            editText.hideKeyboard()
            editText.isCursorVisible = false
        }

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        recyclerView.apply {
            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = ListLoadStateAdapter { searchAdapter.retry() },
                footer = ListLoadStateAdapter { searchAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        }

        loadStateLayout.retryButton.setOnClickListener { searchAdapter.retry() }
        topMessageTextView.setOnClickListener { searchAdapter.retry() }

        searchAdapter.addLoadStateListener { loadState ->
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                    || loadState.mediator?.refresh is LoadState.NotLoading
            loadStateLayout.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    && loadState.mediator?.refresh is LoadState.Loading

            val sourceErrorState = loadState.source.refresh as? LoadState.Error

            loadStateLayout.retryButton.isVisible = (sourceErrorState != null)
            loadStateLayout.messageTextView.apply {
                isVisible = (sourceErrorState != null)
                text = sourceErrorState?.error.toString()
            }

            val mediatorErrorState = loadState.mediator?.refresh as? LoadState.Error
            mediatorErrorState?.let {
                showToast(getString(R.string.api_error_prefix) + mediatorErrorState.error.toString())
            }

            topMessageTextView.isVisible = (mediatorErrorState != null && sourceErrorState == null)
        }
    }

    private fun updateSearchFromInput() = with(binding) {
        editText.text?.trim()?.let {
            if (it.isNotEmpty()) {
                search(it.toString())
            }
        }
    }

    private var searchJob: Job? = null

    private fun search(query: String?) {
        query ?: return
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.search(query).collectLatest { searchAdapter.submitData(it) }
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}