package com.imn.iicnma.ui.movie_detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.databinding.FragmentMovieDetailBinding
import com.imn.iicnma.utils.getColorCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentMovieDetailBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            loadMovie(args.movieId)
            movie.observe(viewLifecycleOwner, { populateUi(it) })
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

    private fun populateUi(movie: MovieEntity?) {
        movie ?: return

        with(binding) {
            favoriteButton.setOnClickListener {
                viewModel.toggleFavorite(args.movieId)
            }

            Glide.with(requireContext())
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_place_holder_24dp)
                .into(posterImageView)

            titleTextView.text = movie.title
            dateTextView.text = movie.releaseDate
            genreTextView.text = movie.genres
            overviewTextView.text = movie.overview
        }

    }

}