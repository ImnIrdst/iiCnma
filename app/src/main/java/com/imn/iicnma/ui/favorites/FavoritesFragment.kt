package com.imn.iicnma.ui.favorites

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentFavoritesBinding
import com.imn.iicnma.ui.home.ListLoadStateAdapter
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
            FavoritesFragmentDirections.actionNavigationFavoritesToMovieDetails(movieId), extras
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentFavoritesBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            favoritesViewModel.movies.collectLatest { favoritesAdapter.submitData(it) }
        }

        populateUI()
    }

    private fun populateUI() = with(binding) {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()

        recyclerView.apply {
            adapter = favoritesAdapter.withLoadStateHeaderAndFooter(
                header = ListLoadStateAdapter { favoritesAdapter.retry() },
                footer = ListLoadStateAdapter { favoritesAdapter.retry() }
            )
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        loadStateLayout.retryButton.setOnClickListener { favoritesAdapter.retry() }
        favoritesAdapter.addLoadStateListener { loadState ->
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
        }
    }
}