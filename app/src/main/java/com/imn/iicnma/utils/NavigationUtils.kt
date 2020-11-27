package com.imn.iicnma.utils

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigator

fun NavController.navigateSafe(
    directions: NavDirections,
    extras: Navigator.Extras? = null,
) {
    if (currentDestination?.getAction(directions.actionId) != null) {
        if (extras != null) {
            navigate(directions, extras)
        } else {
            navigate(directions)
        }

    } else {
        Log.w("navigateSafe", Throwable("trying to navigate to a unknown destination"))
    }
}