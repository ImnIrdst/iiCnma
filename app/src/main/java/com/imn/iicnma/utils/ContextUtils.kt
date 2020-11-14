package com.imn.iicnma.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String?) {
    context?.let {
        Toast.makeText(it, text, Toast.LENGTH_LONG).apply { show() }
    }
}