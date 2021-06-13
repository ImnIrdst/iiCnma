package com.imn.iicnma.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Ignore
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
@Ignore("This is base class")
open class IITestCase {

    protected val td = TestCoroutineDispatcher()
    protected val testScope = TestCoroutineScope(td)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    open fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(td)
    }

    fun throwScopeExceptions() {
        testScope.uncaughtExceptions.firstOrNull()?.let {
            throw it
        }
    }

    open fun tearDown() {
        testScope.uncaughtExceptions.firstOrNull()?.let { throw it }
        Dispatchers.resetMain()
    }
}