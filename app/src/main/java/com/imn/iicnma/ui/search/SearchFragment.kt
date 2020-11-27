package com.imn.iicnma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
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
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.ui.widget.ListLoadStateAdapter
import com.imn.iicnma.utils.*
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
        movie: Movie,
        posterImageView: ImageView,
        titleTextView: TextView,
        dateTextView: TextView,
    ) {
        val extras = FragmentNavigatorExtras(
            posterImageView to posterImageView.transitionName,
            titleTextView to titleTextView.transitionName,
            dateTextView to dateTextView.transitionName,
        )
        findNavController().navigateSafe(
            SearchFragmentDirections.actionNavigationSearchToMovieDetails(movie), extras
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentSearchBinding.inflate(inflater).also { binding = it; initUI() }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.getSavedQuery().let {
            populateUI(it)
            search(it)
        }
    }

    private fun initUI() = with(binding) {
        searchButton.setOnClickListener { updateSearchFromInput() }
        backButton.setOnClickListener { findNavController().navigateUp() }

        editText.apply {
            requestFocus()
            showKeyboard()
            setOnKeyActionListener(EditorInfo.IME_ACTION_SEARCH) { updateSearchFromInput() }
            setOnClickListener { isCursorVisible = true }
        }

        postponeEnterTransition()
        recyclerView.apply {
            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = ListLoadStateAdapter { searchAdapter.retry() },
                footer = ListLoadStateAdapter { searchAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
            viewTreeObserver.addOnPreDrawListener { startPostponedEnterTransition(); true }
        }

        loadStateView.setOnRetryListener { searchAdapter.retry() }
        topMessageTextView.setOnClickListener { searchAdapter.retry() }

        searchAdapter.addLoadStateListener { loadState ->

            recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
            loadStateView.isLoadingVisible = loadState.refresh is LoadState.Loading

            if (loadState.refresh is LoadState.Error) {

                val sourceErrorState = loadState.source.refresh as? LoadState.Error

                if (sourceErrorState != null) {
                    loadStateView.showErrorMessage(sourceErrorState.error.toString())
                } else {
                    recyclerView.isVisible = true
                    topMessageTextView.isVisible = true
                    loadStateView.hideErrorMessage()
                }

                (loadState.mediator?.refresh as? LoadState.Error)?.let {
                    showToast(getString(R.string.api_error_prefix) + it.error.toString())
                }
            } else {
                topMessageTextView.isVisible = false
                loadStateView.hideErrorMessage()
            }

            if (searchViewModel.isSearchedAnyThing
                && loadState.refresh is LoadState.NotLoading
                && searchAdapter.itemCount == 0
            ) {
                recyclerView.isVisible = false
                loadStateView.showErrorMessage(getString(R.string.no_search_results), false)

            }
        }
    }

    private fun populateUI(query: String?) = with(binding) {
        query?.let {
            editText.setText(it)
            updateSearchFromInput()
        }
    }

    private fun updateSearchFromInput() = with(binding.editText) {
        text?.trim()?.let {
            hideKeyboard()
            isCursorVisible = false
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
            searchViewModel.search(query)?.collectLatest { searchAdapter.submitData(it) }
        }
    }
}