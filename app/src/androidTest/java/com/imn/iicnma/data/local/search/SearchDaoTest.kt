package com.imn.iicnma.data.local.search

import androidx.paging.PagingSource
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import com.imn.iicnma.data.repository.search.SearchRepository.Companion.toSearchQuery
import com.imn.iicnma.utils.IITestCase
import com.imn.iicnma.utils.LATCH_AWAIT_TIMEOUT
import com.imn.iicnma.utils.movieItemEntity2
import com.imn.iicnma.utils.pagedListResponse
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
class SearchDaoTest : IITestCase() {

    @Inject
    lateinit var searchMoviesDao: SearchDao

    @Before
    override fun setUp() = super.setUp()

    @Test
    fun insertAndSearchTest() = td.runBlockingTest {

        val latch = CountDownLatch(1)
        val job = testScope.launch {
            searchMoviesDao.cacheResponse(pagedListResponse, pagedListResponse.page, true)

            val pagedSource = searchMoviesDao.searchMovies("Hard".toSearchQuery())

            val page = pagedSource.load(
                PagingSource.LoadParams.Refresh(0, NETWORK_PAGE_SIZE, false)
            )

            assertThat(page).isEqualTo(
                PagingSource.LoadResult.Page(
                    data = listOf(movieItemEntity2),
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
        throwScopeExceptions()
        job.cancel()
    }
}