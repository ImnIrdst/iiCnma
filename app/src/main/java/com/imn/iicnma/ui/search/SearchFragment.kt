package com.imn.iicnma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentSearchBinding
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.ui.common.base.BaseFragment
import com.imn.iicnma.ui.common.loadstate.ListLoadStateAdapter
import com.imn.iicnma.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val searchViewModel: SearchViewModel by viewModels()

    private val searchAdapter by ViewLifecycleDelegate { SearchAdapter(::onMovieClicked) }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewLifecycleOwner.lifecycleScope.launch {
            binding?.apply {
                searchAdapter.listenOnLoadStates(
                    recyclerView,
                    loadStateView,
                    { searchViewModel.isSearchedAnyThing && searchAdapter.itemCount == 0 },
                    getString(R.string.no_search_results)
                )
            }
        }

        searchViewModel.getSavedQuery().let {
            populateUI(it)
            search(it)
        }
    }

    override fun onDestroyView() {
        binding?.recyclerView?.adapter = null
        super.onDestroyView()
    }

    private fun initUI() = binding?.apply {
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
            doOnPreDraw { startPostponedEnterTransition() }
        }

        loadStateView.setOnRetryListener { searchAdapter.retry() }
    }

    private fun populateUI(query: String?) = binding?.apply {
        query?.let {
            editText.setText(it)
            updateSearchFromInput()
        }
    }

    private fun updateSearchFromInput() = binding?.editText?.apply {
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
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.search(query)?.collectLatest { searchAdapter.submitData(it) }
        }
    }

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
}