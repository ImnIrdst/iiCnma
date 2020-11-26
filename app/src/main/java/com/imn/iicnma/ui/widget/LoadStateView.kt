package com.imn.iicnma.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import com.imn.iicnma.databinding.LayoutLoadStateBinding

class LoadStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @Suppress("UNUSED_PARAMETER") @AttrRes defStyleAttr: Int = 0,
    @Suppress("UNUSED_PARAMETER") @StyleRes defStyleRes: Int = 0,
) : FrameLayout(context, attrs) {

    private val binding = LayoutLoadStateBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun hide() {
        binding.root.isVisible = false
    }

    fun setErrorMessage(message: String, isRetryButtonVisible: Boolean = true) = with(binding) {
        root.isVisible = true
        progressBar.isVisible = false
        retryButton.isVisible = isRetryButtonVisible
        messageTextView.apply {
            isVisible = true
            text = message
        }
    }

    fun showLoading() = with(binding) {
        root.isVisible = true
        progressBar.isVisible = true
        retryButton.isVisible = false
        messageTextView.isVisible = false
    }

    fun setOnRetryListener(listener: (View) -> Unit) {
        binding.retryButton.setOnClickListener(listener)
    }
}