package com.imn.iicnma.utils

import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String?) {
    context?.let {
        Toast.makeText(it, text, Toast.LENGTH_LONG).apply { show() }
    }
}

fun Fragment.getColorCompat(@ColorRes colorId: Int) =
    ContextCompat.getColor(requireContext(), colorId)

fun Fragment.isPortrait() =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT