package com.imn.iicnma.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewLifecycleDelegate<T>(
    private val initializer: () -> T,
) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private var value: T? = null
    private var isObserverAdded = false

    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>,
    ): T {

        if (!isObserverAdded) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
            isObserverAdded = true
        }

        if (value == null) {
            value = initializer.invoke()
        }
        return value!!
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        isObserverAdded = false
        value = null
    }
}