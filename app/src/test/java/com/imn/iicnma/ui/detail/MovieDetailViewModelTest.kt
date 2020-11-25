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
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.asFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun `test getMovieDetail normal scenario`() {
        every { movieRepository.getMovie(movieId) } returns listOf(movieDetail).asFlow()

        viewModel.loadMovie(movieId).observeForever(movieDetailObserver)

        verifySequence {
            movieDetailObserver.onChanged(loadingState())
            movieRepository.getMovie(movieId)
            movieDetailObserver.onChanged(successState(movieDetail))
        }
    }
}