package com.imn.iicnma.ui.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentMovieDetailBinding
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.domain.model.utils.IIError
import com.imn.iicnma.domain.model.utils.State
import com.imn.iicnma.domain.model.utils.getHumanReadableText
import com.imn.iicnma.ui.DetailsTransition
import com.imn.iicnma.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentMovieDetailBinding.inflate(inflater).also { binding = it; initUI() }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        viewModel.apply {
            loadMovie(args.movieId).observe(viewLifecycleOwner) { state ->
                when (state) {
                    is State.Success -> state.value?.let { populateUi(it) }
                    is State.Loading -> showLoading()
                    is State.Failure -> populateError(state.error)
                    else -> throw IllegalStateException("bad state for load movie")
                }
            }
            isFavoredStatus(args.movieId).observe(viewLifecycleOwner, {
                it?.let { populateFavoriteButton(it) }
            })
        }
    }

    private fun initUI() = with(binding) {
        sharedElementEnterTransition = DetailsTransition()
        sharedElementReturnTransition = DetailsTransition()
        postponeEnterTransition()

        posterImageView.transitionName = posterTransitionName(args.movieId)
        titleTextView.transitionName = titleTransitionName(args.movieId)
        dateTextView.transitionName = dateTransitionName(args.movieId)

        loadStateView.setOnRetryListener { loadData() }
    }

    private fun populateUi(movie: Movie) = with(binding) {

        loadStateView.hide()

        favoriteButton.setOnClickListener {
            viewModel.toggleFavorite(args.movieId)
        }

        Glide.with(requireContext())
            .load(movie.posterUrl)
            .dontAnimate()
            .placeholder(R.drawable.ic_place_holder_24dp)
            .doOnFinished { startPostponedEnterTransition() }
            .into(posterImageView)

        ratingView.apply {
            root.isVisible = true
            rateProgress.progress = movie.rate100
            rateText.text = movie.rate100.toString()
        }

        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate
        genreTextView.text = movie.genres
        overviewTextView.text = movie.overview
        headingOverview.isVisible = true

        scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, dy, _, oldDy ->
                favoriteButton.isVisible = (dy <= oldDy)
            }
        )

    }

    private fun populateFavoriteButton(state: Boolean) {
        val fabColor = if (state) {
            getColorCompat(R.color.orange_200)
        } else {
            getColorCompat(R.color.gray)
        }
        binding.favoriteButton.backgroundTintList = ColorStateList.valueOf(fabColor)
    }

    private fun populateError(error: IIError) = with(binding) {
        startPostponedEnterTransition()
        loadStateView.setErrorMessage(error.getHumanReadableText(requireContext()))
    }

    private fun showLoading() = with(binding) {
        loadStateView.showLoading()
    }
}