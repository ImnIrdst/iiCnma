package com.imn.iicnma.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.imn.iicnma.data.remote.MovieService
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val service: MovieService,
    @Assisted private val savedState: SavedStateHandle
) : ViewModel() {

    // TODO use repository
    private val _text = MutableLiveData<String>().apply {
        viewModelScope.launch {

            service.getMovie().toString().let {
                postValue(it)
            }

        }
    }
    val text: LiveData<String> = _text
}