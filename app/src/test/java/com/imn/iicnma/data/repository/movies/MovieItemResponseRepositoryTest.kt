package com.imn.iicnma.data.repository.movies

import com.google.common.truth.Truth.assertThat
import com.imn.iicnma.*
import com.imn.iicnma.domain.model.Movie
import com.imn.iicnma.domain.model.utils.NetworkError
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieResponseRepositoryTest : IITestCase() {

    private val local: MoviesLocalDataSource = mockk(relaxUnitFun = true)
    private val remote: MovieRemoteDataSource = mockk(relaxUnitFun = true)
    private val movieRepository = MovieRepository(local, remote)

    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        confirmVerified(local, remote)
        super.tearDown()
    }

    @Test
    fun `test get movie when exists in db`() = td.runBlockingTest {
        every { local.getMovieFlow(movieId) } returns listOf(movieDetailEntity).asFlow()

        movieRepository.getMovie(movieId)
            .toList()
            .also { assertThat(it).isEqualTo(listOf(movieDetail)) }

        coVerifySequence {
            local.getMovieFlow(movieId)
        }
    }

    @Test
    fun `test get movie when not exists in db`() = td.runBlockingTest {
        val stateFlow = MutableStateFlow(movieItemEntity)
        every { local.getMovieFlow(movieId) } returns stateFlow
        coEvery { remote.getMovie(movieId) } answers {
            stateFlow.value = movieDetailEntity
            movieDetailResponse
        }
        val result = mutableListOf<Movie?>()

        testScope.launch {
            movieRepository.getMovie(movieId)
                .collect { result.add(it) }
        }

        assertThat(result).isEqualTo(listOf(movieItem, movieDetail))

        coVerifySequence {
            local.getMovieFlow(movieId)
            remote.getMovie(movieId)
            local.insert(movieDetailEntity)
        }
    }

    @Test
    fun `test get movie when network error happens`() = td.runBlockingTest {
        every { local.getMovieFlow(movieId) } returns MutableStateFlow(movieItemEntity)
        coEvery { remote.getMovie(movieId) } throws unknownHostException

        testScope.launch {
            movieRepository.getMovie(movieId)
                .catch {
                    assertThat(it).isInstanceOf(NetworkError::class.java)
                }
                .collect()
        }

        coVerifySequence {
            local.getMovieFlow(movieId)
            remote.getMovie(movieId)
        }
    }
}