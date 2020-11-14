package com.imn.iicnma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.imn.iicnma.databinding.FragmentSearchBinding
import com.imn.iicnma.utils.hideKeyboard
import com.imn.iicnma.utils.showKeyboard
import com.imn.iicnma.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    private val searchAdapter = SearchAdapter(::onMovieClicked)

    private fun onMovieClicked(movieId: Long) {
        findNavController().navigate(
            SearchFragmentDirections.actionNavigationSearchToMovieDetails(movieId)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSearchBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            searchViewModel.movies.collectLatest { searchAdapter.submitData(it) }
        }

        populateUI()
    }

    private fun populateUI() = with(binding) {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()

        editText.apply {
            requestFocus()
            showKeyboard()
            setOnClickListener { isCursorVisible = true }
        }

        searchButton.setOnClickListener {
            editText.hideKeyboard()
            editText.isCursorVisible = false
            showToast(editText.text?.toString())
        }

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        recyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        }
    }
}