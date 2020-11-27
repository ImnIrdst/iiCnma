package com.imn.iicnma.utils

import android.app.Activity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
fun PagingDataAdapter<*, *>.getLoadStateFlow(): Flow<CombinedLoadStates?> {

    val loadStateFlow = MutableStateFlow<CombinedLoadStates?>(null)

    this.addLoadStateListener {
        loadStateFlow.value = it
    }

    return loadStateFlow.debounce(500)
}