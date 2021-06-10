package com.imn.iicnma.data.local.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.imn.iicnma.data.repository.movies.MoviesLocalDataSource
import com.imn.iicnma.utils.IITestCase
import com.imn.iicnma.utils.movieId
import com.imn.iicnma.utils.movieItemEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@MediumTest
class MovieDaoTest : IITestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var movieDao: MoviesLocalDataSource

    @Before
    override fun setUp() {
        super.setUp()
        hiltRule.inject()
    }

    @Test
    fun testInsertAndRetrieveMovie() = td.runBlockingTest {
        movieDao.insert(movieItemEntity)

        val movieList = mutableListOf<MovieEntity?>()
        val job = launch {
            movieDao.getMovieFlow(movieId)
                .collect { movieList.add(it) }
        }
        job.cancel()

        assertThat(movieList).isEqualTo(listOf(movieItemEntity))
    }
}