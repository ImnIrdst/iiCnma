package com.imn.iicnma.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.imn.iicnma.ui.common.base.FragmentCleaner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewLifecycleDelegateMutable<T> : ReadWriteProperty<Fragment, T>, LifecycleObserver {

    private var value: T? = null
    private var isObserverAdded = false
    private var fragmentCleaner: FragmentCleaner? = null

    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>,
    ): T {
        return value!!
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        if (!isObserverAdded) {
            fragmentCleaner = thisRef as? FragmentCleaner
            thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
            isObserverAdded = true
        }
        this.value = value
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        isObserverAdded = false
        fragmentCleaner?.cleanViews()
        fragmentCleaner = null
        value = null
    }
}