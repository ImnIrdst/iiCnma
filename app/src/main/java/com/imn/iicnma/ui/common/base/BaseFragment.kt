package com.imn.iicnma.ui.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.ref.WeakReference

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: WeakReference<T> = WeakReference(null)

    protected val binding: T get() = _binding.get()!!

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = inflateBinding(inflater, container).also { _binding = WeakReference(it); }.root
}