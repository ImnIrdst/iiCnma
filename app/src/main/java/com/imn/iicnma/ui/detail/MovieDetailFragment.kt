package com.imn.iicnma.ui.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentMovieDetailBinding
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.domain.model.utils.IIError
import com.imn.iicnma.domain.model.utils.State
import com.imn.iicnma.domain.model.utils.getHumanReadableText
import com.imn.iicnma.ui.common.DetailsTransition
import com.imn.iicnma.ui.common.base.BaseFragment
import com.imn.iicnma.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {


    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentMovieDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        loadData()
    }

    private fun loadData() = viewModel.apply {
        loadMovie(args.movie.id).observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Success -> state.value?.let { populateUi(it) }
                is State.Loading -> showLoading()
                is State.Failure -> populateError(state.error)
                else -> throw IllegalStateException("bad state for load movie")
            }
        }
        isFavoredStatus(args.movie.id).observe(viewLifecycleOwner, {
            it?.let { populateFavoriteButton(it) }
        })
    }

    private fun initUI() = with(binding) {
        val movie = args.movie

        sharedElementEnterTransition = DetailsTransition()
        sharedElementReturnTransition = DetailsTransition()
        postponeEnterTransition()

        posterImageView.transitionName = posterTransitionName(movie.id)
        titleTextView.transitionName = titleTransitionName(movie.id)
        dateTextView.transitionName = dateTransitionName(movie.id)

        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate

        Glide.with(requireContext())
            .load(movie.posterUrl)
            .dontAnimate()
            .placeholder(R.drawable.ic_place_holder_24dp)
            .doOnFinished { startPostponedEnterTransition() }
            .into(posterImageView)

        loadStateView.setOnRetryListener { loadData() }
        favoriteButton.setOnClickListener { viewModel.toggleFavorite(movie.id) }
    }

    private fun populateUi(movie: Movie) = with(binding) {

        loadStateView.hide()

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

        loadStateView.hideErrorMessage()
    }

    private fun populateFavoriteButton(state: Boolean) = with(binding) {
        val fabColor = if (state) {
            getColorCompat(R.color.orange_200)
        } else {
            getColorCompat(R.color.gray)
        }
        favoriteButton.backgroundTintList = ColorStateList.valueOf(fabColor)
    }

    private fun populateError(error: IIError) = with(binding) {
        startPostponedEnterTransition()
        loadStateView.showErrorMessage(error.getHumanReadableText(requireContext()))
    }

    private fun showLoading() = with(binding) {
        loadStateView.isLoadingVisible = true
        loadStateView.hideErrorMessage()
    }
}