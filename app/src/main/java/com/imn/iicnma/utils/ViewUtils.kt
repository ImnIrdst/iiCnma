package com.imn.iicnma.utils

import android.app.Activity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

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