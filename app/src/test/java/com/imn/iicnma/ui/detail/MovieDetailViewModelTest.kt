package com.imn.iicnma.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.imn.iicnma.IITestCase
import com.imn.iicnma.data.repository.favorites.FavoritesRepository
import com.imn.iicnma.data.repository.movies.MovieRepository
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.domain.model.utils.State
import com.imn.iicnma.domain.model.utils.loadingState
import com.imn.iicnma.domain.model.utils.successState
import com.imn.iicnma.movieDetail
import com.imn.iicnma.movieId
import com.imn.iicnma.unknownHostException
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest : IITestCase() {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val movieRepository: MovieRepository = mockk(relaxUnitFun = true)
    private val favoritesRepository: FavoritesRepository = mockk(relaxUnitFun = true)
    private val viewModel = MovieDetailViewModel(movieRepository, favoritesRepository)

    private val isFavoredObserver: Observer<Boolean> = mockk(relaxUnitFun = true)
    private val movieDetailObserver: Observer<State<Movie?>> = mockk(relaxUnitFun = true)

    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        confirmVerified(
            movieRepository,
            favoritesRepository,
            isFavoredObserver,
            movieDetailObserver
        )
        super.tearDown()
    }

    @Test
    fun `test loadMovie normal scenario`() {
        every { movieRepository.getMovie(movieId) } returns listOf(movieDetail).asFlow()

        viewModel.loadMovie(movieId).observeForever(movieDetailObserver)

        verifySequence {
            movieDetailObserver.onChanged(loadingState())
            movieRepository.getMovie(movieId)
            movieDetailObserver.onChanged(successState(movieDetail))
        }
    }

    @Test
    fun `test loadMovie failure scenario`() {
        every { movieRepository.getMovie(movieId) } returns flow<Movie> { throw unknownHostException }

        viewModel.loadMovie(movieId).observeForever(movieDetailObserver)

        verifySequence {
            movieDetailObserver.onChanged(loadingState())
            movieRepository.getMovie(movieId)
            movieDetailObserver.onChanged(ofType(State.Failure::class))
        }
    }

    @Test
    fun `test toggleFavorite normal scenario`() {
        val isFavoredFlow = MutableStateFlow(false)
        every { favoritesRepository.isFavored(movieId) } returns isFavoredFlow
        coEvery { favoritesRepository.addToFavorites(movieId) } answers {
            isFavoredFlow.value = true
        }
        coEvery { favoritesRepository.removeFromFavorites(movieId) } answers {
            isFavoredFlow.value = false
        }

        viewModel.isFavoredStatus(movieId).observeForever(isFavoredObserver)

        viewModel.toggleFavorite(movieId)

        viewModel.toggleFavorite(movieId)

        coVerifySequence {
            favoritesRepository.isFavored(movieId)
            isFavoredObserver.onChanged(false)

            favoritesRepository.addToFavorites(movieId)
            isFavoredObserver.onChanged(true)

            favoritesRepository.removeFromFavorites(movieId)
            isFavoredObserver.onChanged(false)
        }
    }
}