package com.imn.iicnma.data.local.favorites

import androidx.paging.PagingSource
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.imn.iicnma.data.local.movie.MovieDao
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import com.imn.iicnma.utils.*
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@MediumTest
class FavoritesDaoTest : IITestCase() {

    @Inject
    lateinit var favoritesDao: FavoritesDao

    @Inject
    lateinit var movieDao: MovieDao

    @Before
    override fun setUp() = super.setUp()

    @Test
    fun insertDeleteAndRetrieveTest() = td.runBlockingTest {
        favoritesDao.insert(favoriteItemEntity)
        favoritesDao.insert(favoriteItemEntity2)

        val favoriteList = mutableListOf<FavoritesEntity?>()

        val job = launch {
            favoritesDao.getFavoriteFlow(favoriteItemEntity.movieId)
                .collect { favoriteList.add(it) }
        }

        assertThat(favoriteList).isEqualTo(listOf(favoriteItemEntity))

        favoritesDao.delete(favoriteItemEntity.movieId)

        assertThat(favoriteList).isEqualTo(listOf(favoriteItemEntity, null))

        job.cancel()
    }

    @Test
    fun pagedDataTest() = testScope.runBlockingTest {
        movieDao.insert(movieItemEntity)
        favoritesDao.insert(favoriteItemEntity)

        movieDao.insert(movieItemEntity2)
        favoritesDao.insert(favoriteItemEntity2)

        val latch = CountDownLatch(1)

        val pageSource = favoritesDao.getAllFavoredMovies()
        val job = testScope.launch {
            val page = pageSource.load(
                PagingSource.LoadParams.Refresh(null, NETWORK_PAGE_SIZE, false)
            )

            assertThat(page).isEqualTo(
                PagingSource.LoadResult.Page(
                    data = listOf(favoriteRelation, favoriteRelation2),
                    prevKey = null,
                    nextKey = null,
                    itemsBefore = 0,
                    itemsAfter = 0
                )
            )

            latch.countDown()
        }

        @Suppress("BlockingMethodInNonBlockingContext")
        latch.await(LATCH_AWAIT_TIMEOUT, TimeUnit.SECONDS)
        job.cancel()
    }
}