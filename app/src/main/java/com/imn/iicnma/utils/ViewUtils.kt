package com.imn.iicnma.utils

import android.app.Activity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imn.iicnma.R
import com.imn.iicnma.ui.widget.PageLoadStateView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
    loadStateView: PageLoadStateView,
    topMessageTextView: TextView,
) {
    loadStateView.setOnRetryListener { this.retry() }
    topMessageTextView.setOnClickListener { this.retry() }

    getLoadStateFlow().collectLatest { loadState ->
        loadState ?: return@collectLatest

        val context = topMessageTextView.context

        recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
                || loadState.source.refresh is LoadState.NotLoading
        loadStateView.isLoadingVisible = loadState.refresh is LoadState.Loading

        if (loadState.refresh is LoadState.Error) {

            val sourceErrorState = loadState.source.refresh as? LoadState.Error

            if (sourceErrorState != null) {
                loadStateView.showErrorMessage(sourceErrorState.error.toString())
            } else {
                recyclerView.isVisible = true
                topMessageTextView.isVisible = true
                loadStateView.hideErrorMessage()
            }

            (loadState.mediator?.refresh as? LoadState.Error)?.let {
                context.showToast(
                    context.getString(R.string.api_error_prefix) + it.error.toString()
                )
            }
        } else {
            topMessageTextView.isVisible = false
            loadStateView.hideErrorMessage()
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
fun PagingDataAdapter<*, *>.getLoadStateFlow(): Flow<CombinedLoadStates?> {

    val loadStateFlow = MutableStateFlow<CombinedLoadStates?>(null)

    this.addLoadStateListener {
        loadStateFlow.value = it
    }

    return loadStateFlow.debounce(500)
}