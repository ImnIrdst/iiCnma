package com.imn.iicnma.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.databinding.ListItemFavoritesBinding
import com.imn.iicnma.utils.dateTransitionName
import com.imn.iicnma.utils.posterTransitionName
import com.imn.iicnma.utils.titleTransitionName

class FavoritesAdapter(
    private val onItemClick: (Long, ImageView, TextView, TextView) -> Unit,
) : PagingDataAdapter<MovieEntity, FavoritesItemViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesItemViewHolder =
        FavoritesItemViewHolder.create(parent, onItemClick)


    override fun onBindViewHolder(holder: FavoritesItemViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(
                oldItem: MovieEntity,
                newItem: MovieEntity,
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieEntity,
                newItem: MovieEntity,
            ): Boolean =
                oldItem == newItem
        }
    }
}

class FavoritesItemViewHolder(
    private val binding: ListItemFavoritesBinding,
    private val onItemClick: (Long, ImageView, TextView, TextView) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private var _movie: MovieEntity? = null

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

    fun onBind(movie: MovieEntity) = with(binding) {
        _movie = movie

        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate
        overviewTextView.text = movie.overview

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
            FavoritesItemViewHolder(
                ListItemFavoritesBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onItemClick
            )
    }
}