package com.imn.iicnma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imn.iicnma.R
import com.imn.iicnma.databinding.HomeListItemBinding
import com.imn.iicnma.model.Movie

class HomeAdapter : PagingDataAdapter<Movie, HomeItemViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder =
        HomeItemViewHolder(
            HomeListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


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
    private val binding: HomeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(movie: Movie) {
        binding.titleTextView.text = movie.originalTitle
        binding.dateTextView.text = movie.releaseDate

        Glide.with(binding.root.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_place_holder_24dp)
            .into(binding.posterImageView)
    }
}