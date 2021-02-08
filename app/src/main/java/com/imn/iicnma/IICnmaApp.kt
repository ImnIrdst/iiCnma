package com.imn.iicnma

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class IICnmaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement) =
                    "iiLog:(${super.createStackElementTag(element)}" +
                            ":${element.fileName}" +
                            ":${element.lineNumber})"

            })
        }
    }
}