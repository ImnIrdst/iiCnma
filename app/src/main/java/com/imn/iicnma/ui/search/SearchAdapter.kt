package com.imn.iicnma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.databinding.SearchListItemBinding

class SearchAdapter(
    private val onItemClick: (Long) -> Unit
) : PagingDataAdapter<MovieEntity, SearchItemViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder =
        SearchItemViewHolder.create(parent, onItemClick)


    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem == newItem
        }
    }
}

class SearchItemViewHolder(
    private val binding: SearchListItemBinding,
    private val onItemClick: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var _movie: MovieEntity? = null

    init {
        binding.root.setOnClickListener {
            _movie?.let { onItemClick.invoke(it.id) }
        }
    }

    fun onBind(movie: MovieEntity) = with(binding) {
        _movie = movie

        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate
        overviewTextView.text = movie.overview

        Glide.with(root.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24dp)
            .into(posterImageView)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit) = SearchItemViewHolder(
            SearchListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemClick
        )
    }
}