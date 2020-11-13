package com.imn.iicnma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imn.iicnma.databinding.LoadStateListFooterBinding

class ListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder.create(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}

class LoadStateViewHolder(
    private val binding: LoadStateListFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.messageTextView.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.messageTextView.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val binding = LoadStateListFooterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return LoadStateViewHolder(binding, retry)
        }
    }
}