package com.imn.iicnma.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.recyclerview.widget.RecyclerView
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentFavoritesBinding
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.ui.widget.ListLoadStateAdapter
import com.imn.iicnma.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private val favoritesAdapter = FavoritesAdapter(::onMovieClicked)

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
        findNavController().navigate(
            FavoritesFragmentDirections.actionNavigationFavoritesToMovieDetails(movie), extras
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentFavoritesBinding.inflate(inflater).also { binding = it; initUi() }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            favoritesViewModel.movies.collectLatest { favoritesAdapter.submitData(it) }
        }
    }

    private fun initUi() = with(binding) {
        postponeEnterTransition()

        recyclerView.apply {
            adapter = favoritesAdapter.withLoadStateHeaderAndFooter(
                header = ListLoadStateAdapter { favoritesAdapter.retry() },
                footer = ListLoadStateAdapter { favoritesAdapter.retry() }
            )
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewTreeObserver.addOnPreDrawListener { startPostponedEnterTransition(); true }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                private var dySum = 0

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE,
                        RecyclerView.SCROLL_STATE_SETTLING,
                        -> {
                            pageTitle.isVisible = (dySum <= 0)
                            dySum = 0
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    dySum += dy
                }
            })
        }

        loadStateView.setOnRetryListener { favoritesAdapter.retry() }
        favoritesAdapter.addLoadStateListener { loadState ->

            recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
            loadStateView.isLoadingVisible = loadState.refresh is LoadState.Loading

            if (loadState.refresh is LoadState.Error) {

                val sourceErrorState = loadState.source.refresh as? LoadState.Error

                if (sourceErrorState != null) {
                    loadStateView.showErrorMessage(sourceErrorState.error.toString())
                } else {
                    recyclerView.isVisible = true
                    loadStateView.hideErrorMessage()
                }

                (loadState.mediator?.refresh as? LoadState.Error)?.let {
                    showToast(getString(R.string.api_error_prefix) + it.error.toString())
                }
            } else {
                loadStateView.hideErrorMessage()
            }

            if (loadState.refresh is LoadState.NotLoading && favoritesAdapter.itemCount == 0) {
                recyclerView.isVisible = false
                loadStateView.showErrorMessage(getString(R.string.no_favorite_movies), false)
            }

        }
    }
}