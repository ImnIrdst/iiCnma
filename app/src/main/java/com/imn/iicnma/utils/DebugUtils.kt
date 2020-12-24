@file:Suppress("unused")

package com.imn.iicnma.utils

import android.util.Log


fun iiDebugLog(msg: String) {
    Log.e("IMNIMN", msg)
}

fun Any?.getNullableClassSimpleName() = if (this != null) this::class.simpleName else "null"