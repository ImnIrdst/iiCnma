package com.imn.iicnma.ui.common.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imn.iicnma.databinding.LayoutPageLoadStateBinding
import timber.log.Timber

class ListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder.create(parent, retry)
            .also { Timber.v("loadState $loadState") }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
            .also { Timber.v("loadState: $loadState") }
    }

}

class LoadStateViewHolder(
    private val binding: LayoutPageLoadStateBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) = binding.apply {
        if (loadState is LoadState.Error) {
            messageTextView.text = loadState.error.localizedMessage
        }
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState !is LoadState.Loading
        messageTextView.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val binding = LayoutPageLoadStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return LoadStateViewHolder(binding, retry)
        }
    }
}