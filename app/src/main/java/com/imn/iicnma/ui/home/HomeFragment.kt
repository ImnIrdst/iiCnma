package com.imn.iicnma.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.imn.iicnma.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private val homeAdapter = HomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater).apply {
            recyclerView.apply {
                adapter = homeAdapter.withLoadStateFooter(
                    footer = ListLoadStateAdapter { homeAdapter.retry() }
                )
                layoutManager = GridLayoutManager(context, 2).apply {
                    spanSizeLookup = object : SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when ((adapter as ConcatAdapter).getItemViewType(position)) {
                                0 -> 1
                                else -> 2
                            }
                        }
                    }
                }
            }

        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            homeViewModel.movies.collectLatest { homeAdapter.submitData(it) }
        }
    }
}