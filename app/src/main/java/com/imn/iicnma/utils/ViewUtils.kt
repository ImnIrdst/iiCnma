package com.imn.iicnma.utils

import android.app.Activity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imn.iicnma.R
import com.imn.iicnma.domain.model.utils.getHumanReadableText
import com.imn.iicnma.domain.model.utils.toIIError
import com.imn.iicnma.ui.widget.ListLoadStateView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

fun EditText.hideKeyboard(): Boolean {
    return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.showKeyboard(): Boolean {
    return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(this, 0)
}

fun EditText.setOnKeyActionListener(actionId: Int, block: () -> Unit) {
    setOnEditorActionListener { _, _actionId, _ ->
        if (_actionId == actionId) {
            block.invoke()
            true
        } else {
            false
        }
    }
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            block.invoke()
            true
        } else {
            false
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
suspend fun PagingDataAdapter<*, *>.listenOnLoadStates(
    recyclerView: RecyclerView,
    loadStateView: ListLoadStateView,
    isEmpty: () -> Boolean,
    emptyMessage: String,
) {
    loadStateView.setOnRetryListener { this.retry() }

    loadStateFlow.debounce(500).collectLatest { loadState ->

        val context = loadStateView.context

        loadStateView.hide()

        recyclerView.isVisible =
            loadState.refresh is LoadState.NotLoading
                    || loadState.source.refresh is LoadState.NotLoading

        loadStateView.isLoadingVisible = loadState.refresh is LoadState.Loading

        if (loadState.refresh is LoadState.Error) {

            val sourceErrorState = loadState.source.refresh as? LoadState.Error
            val mediatorErrorState = loadState.mediator?.refresh as? LoadState.Error

            if (sourceErrorState != null) {
                loadStateView.showErrorMessage(sourceErrorState.error.toString())
            } else {
                recyclerView.isVisible = true

                if (mediatorErrorState != null) {
                    loadStateView.showErrorMessage(
                        mediatorErrorState.toIIError().getHumanReadableText(context)
                    )
                }
            }

            (loadState.mediator?.refresh as? LoadState.Error)?.let {
                context.showToast(
                    context.getString(R.string.api_error_prefix) + it.error.toString()
                )
            }
        } else {
            loadStateView.hideErrorMessage()
        }

        if (loadState.refresh is LoadState.NotLoading && isEmpty.invoke()) {
            recyclerView.isVisible = false
            loadStateView.showErrorMessage(emptyMessage, false)
        }
    }
}