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
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.databinding.FragmentMovieDetailBinding
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.utils.dateTransitionName
import com.imn.iicnma.utils.getColorCompat
import com.imn.iicnma.utils.posterTransitionName
import com.imn.iicnma.utils.titleTransitionName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentMovieDetailBinding.inflate(inflater).also {
        binding = it
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            posterImageView.transitionName = posterTransitionName(args.movieId)
            titleTextView.transitionName = titleTransitionName(args.movieId)
            dateTextView.transitionName = dateTransitionName(args.movieId)
        }

        viewModel.apply {
            loadMovie(args.movieId).observe(viewLifecycleOwner, { populateUi(it) })
            isFavoredStatus(args.movieId).observe(viewLifecycleOwner, {
                val fabColor = if (it) {
                    getColorCompat(R.color.orange_200)
                } else {
                    getColorCompat(R.color.gray)
                }
                binding.favoriteButton.backgroundTintList = ColorStateList.valueOf(fabColor)
            })
        }
    }

    private fun populateUi(movie: Movie?) {
        movie ?: return

        with(binding) {
            favoriteButton.setOnClickListener {
                viewModel.toggleFavorite(args.movieId)
            }

            Glide.with(requireContext())
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_place_holder_24dp)
                .into(posterImageView)

            ratingView.rateProgress.progress = movie.rate100
            ratingView.rateText.text = movie.rate100.toString()

            titleTextView.text = movie.title
            dateTextView.text = movie.releaseDate
            genreTextView.text = movie.genres
            overviewTextView.text = movie.overview

            scrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, dy, _, oldDy ->
                    favoriteButton.isVisible = (dy <= oldDy)
                }
            )
        }

    }

}