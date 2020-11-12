package com.imn.iicnma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imn.iicnma.databinding.HomeListItemBinding
import com.imn.iicnma.model.Movie

class HomeAdapter : RecyclerView.Adapter<HomeItemViewHolder>() {

    var data = listOf<Movie>()
        set(value) {
            field = value
            println("imnimn $data")
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder =
        HomeItemViewHolder(
            HomeListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class HomeItemViewHolder(
    private val binding: HomeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(movie: Movie) {
        binding.titleTextView.text = movie.originalTitle
//        Glide.with(binding.root.context)
//            .load(url)
//            .into(binding.imageView)
    }
}