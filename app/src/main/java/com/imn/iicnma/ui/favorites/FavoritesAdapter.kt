package com.imn.iicnma.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import com.imn.iicnma.databinding.FavoritesListItemBinding

class FavoritesAdapter(
    private val onItemClick: (Long) -> Unit
) : PagingDataAdapter<FavoritesEntity, FavoritesItemViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesItemViewHolder =
        FavoritesItemViewHolder.create(parent, onItemClick)


    override fun onBindViewHolder(holder: FavoritesItemViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<FavoritesEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoritesEntity,
                newItem: FavoritesEntity
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FavoritesEntity,
                newItem: FavoritesEntity
            ): Boolean =
                oldItem == newItem
        }
    }
}

class FavoritesItemViewHolder(
    private val binding: FavoritesListItemBinding,
    private val onItemClick: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var _movie: FavoritesEntity? = null

    init {
        binding.root.setOnClickListener {
            _movie?.let { onItemClick.invoke(it.id) }
        }
    }

    fun onBind(movie: FavoritesEntity) = with(binding) {
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
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit) = FavoritesItemViewHolder(
            FavoritesListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemClick
        )
    }
}