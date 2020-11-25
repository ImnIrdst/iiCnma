package com.imn.iicnma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.databinding.ListItemHomeBinding
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.utils.dateTransitionName
import com.imn.iicnma.utils.posterTransitionName
import com.imn.iicnma.utils.titleTransitionName

class HomeAdapter(
    private val onItemClick: (Long, ImageView, TextView, TextView) -> Unit,
) : PagingDataAdapter<Movie, HomeItemViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder =
        HomeItemViewHolder.create(parent, onItemClick)


    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}

class HomeItemViewHolder(
    private val binding: ListItemHomeBinding,
    private val onItemClick: (Long, ImageView, TextView, TextView) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private var _movie: Movie? = null

    init {
        with(binding) {
            root.setOnClickListener {
                _movie?.let {
                    onItemClick.invoke(
                        it.id,
                        posterImageView,
                        titleTextView,
                        dateTextView
                    )
                }
            }
        }
    }

    fun onBind(movie: Movie) = with(binding) {
        _movie = movie

        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate

        ratingView.rateProgress.apply {
            isIndeterminate = false
            progress = movie.rate100
            max = 100
        }
        ratingView.rateText.text = movie.rate100.toString()
        posterImageView.transitionName = posterTransitionName(movie.id)
        titleTextView.transitionName = titleTransitionName(movie.id)
        dateTextView.transitionName = dateTransitionName(movie.id)

        Glide.with(root.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24dp)
            .into(posterImageView)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long, ImageView, TextView, TextView) -> Unit) =
            HomeItemViewHolder(
                ListItemHomeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onItemClick
            )
    }
}