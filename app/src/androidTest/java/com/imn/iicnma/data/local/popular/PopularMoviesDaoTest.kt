package com.imn.iicnma.data.local.popular

import androidx.paging.PagingSource
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.imn.iicnma.utils.*
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class PopularMoviesDaoTest : IITestCase() {

    @Inject
    lateinit var popularMoviesDao: PopularMoviesDao

    @Before
    override fun setUp() = super.setUp()

    @Test
    fun insertAndRetrieveTest() = td.runBlockingTest {

        val latch = CountDownLatch(1)
        val job = testScope.launch {
            popularMoviesDao.cacheResponse(page1Response, 1, true)
            popularMoviesDao.cacheResponse(page2Response, 2, false)
//
            val pagedSource = popularMoviesDao.getAll()

            var page = pagedSource.load(
                PagingSource.LoadParams.Refresh(0, 1, false)
            )

            assertThat(page).isEqualTo(
                PagingSource.LoadResult.Page(
                    data = listOf(movieItemEntity),
                    prevKey = null,
                    nextKey = 1,
                    itemsBefore = 0,
                    itemsAfter = 1
                )
            )

            page = pagedSource.load(
                PagingSource.LoadParams.Append(1, 1, false)
            )

            assertThat(page).isEqualTo(
                PagingSource.LoadResult.Page(
                    data = listOf(movieItemEntity2),
                    prevKey = 1,
                    nextKey = 2,
                    itemsBefore = Int.MIN_VALUE,
                    itemsAfter = Int.MIN_VALUE
                )
            )
            latch.countDown()
        }
        @Suppress("BlockingMethodInNonBlockingContext")
        latch.await(LATCH_AWAIT_TIMEOUT, TimeUnit.SECONDS)
        throwScopeExceptions()
        job.cancel()
    }
}