package com.imn.iicnma.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
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

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY)

        populateUI(query)
        search(query)
    }

    private fun populateUI(query: String?) = with(binding) {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()

        editText.apply {
            requestFocus()
            showKeyboard()
            query?.let { setText(it) }
            setOnClickListener { isCursorVisible = true }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateSearchFromInput()
                    true
                } else {
                    false
                }
            }
        }

        searchButton.setOnClickListener {
            updateSearchFromInput()
            editText.hideKeyboard()
            editText.isCursorVisible = false
        }

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        recyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        }
    }

    private fun updateSearchFromInput() = with(binding) {
        editText.text?.trim()?.let {
            if (it.isNotEmpty()) {
                search(it.toString())
            }
        }
    }

    private var searchJob: Job? = null

    private fun search(query: String?) {
        query ?: return
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.search(query).collectLatest { searchAdapter.submitData(it) }
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}