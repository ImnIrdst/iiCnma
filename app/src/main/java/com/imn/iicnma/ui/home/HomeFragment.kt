package com.imn.iicnma.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentHomeBinding
import com.imn.iicnma.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val homeAdapter = HomeAdapter(::onMovieClicked)

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
            HomeFragmentDirections.actionNavigationHomeToMovieDetails(movieId), extras
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater).apply {
            recyclerView.apply {
                adapter = homeAdapter.withLoadStateHeaderAndFooter(
                    header = ListLoadStateAdapter { homeAdapter.retry() },
                    footer = ListLoadStateAdapter { homeAdapter.retry() }
                )
                layoutManager = GridLayoutManager(context, 2).apply {
                    spanSizeLookup = object : SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position >= homeAdapter.itemCount) 2 else 1
                        }
                    }
                }
            }

        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()

        lifecycleScope.launch {
            homeViewModel.movies.collectLatest { homeAdapter.submitData(it) }
        }

        loadStateLayout.retryButton.setOnClickListener { homeAdapter.retry() }
        topMessageTextView.setOnClickListener { homeAdapter.retry() }

        homeAdapter.addLoadStateListener { loadState ->
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
}