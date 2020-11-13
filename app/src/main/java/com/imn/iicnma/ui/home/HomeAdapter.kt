package com.imn.iicnma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.databinding.HomeListItemBinding

class HomeAdapter : PagingDataAdapter<MovieEntity, HomeItemViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder =
        HomeItemViewHolder.create(parent)


    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
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

class HomeItemViewHolder(
    private val binding: HomeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(movie: MovieEntity) = with(binding) {
        titleTextView.text = movie.title
        dateTextView.text = movie.releaseDate

        Glide.with(root.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24dp)
            .into(posterImageView)
    }

    companion object {
        fun create(parent: ViewGroup) = HomeItemViewHolder(
            HomeListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}