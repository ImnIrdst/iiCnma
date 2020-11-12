package com.imn.iicnma.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.imn.iicnma.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val homeAdapter: HomeAdapter
        get() = binding.recyclerView.adapter as HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater).apply {
            recyclerView.apply {
                adapter = HomeAdapter()
                layoutManager = GridLayoutManager(context, 2)
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.movies.observe(viewLifecycleOwner, { homeAdapter.data = it })
    }
}