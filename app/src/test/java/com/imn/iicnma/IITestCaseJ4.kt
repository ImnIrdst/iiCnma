package com.imn.iicnma

import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Ignore

@OptIn(ExperimentalCoroutinesApi::class)
@Ignore("This is base class")
open class IITestCase {

    protected val td = TestCoroutineDispatcher()
    protected val testScope = TestCoroutineScope(td)

    open fun setUp() {
        Dispatchers.setMain(td)
    }

    open fun tearDown() {
        testScope.uncaughtExceptions.firstOrNull()?.let { throw it }
        unmockkAll()
        Dispatchers.resetMain()
        td.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }
}