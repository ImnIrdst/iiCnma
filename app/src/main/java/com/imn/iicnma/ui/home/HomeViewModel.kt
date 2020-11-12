package com.imn.iicnma.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.imn.iicnma.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted private val savedState: SavedStateHandle
) : ViewModel() {
    
    private val _text = MutableLiveData<String>().apply {
        viewModelScope.launch {

            movieRepository.getPopularMovies().toString().let {
                postValue(it)
            }

        }
    }
    val text: LiveData<String> = _text
}