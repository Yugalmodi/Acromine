package com.yugal.acrominefinder.viewmodel

import com.yugal.acrominefinder.model.AcromineDataItem
import com.yugal.acrominefinder.network.ApiResponse
import com.yugal.acrominefinder.network.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AcronymViewModelTest {

    private val testDispatcher= StandardTestDispatcher()
    @Mock
    lateinit var repository: MyRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun test_onSearch_Success() = runTest {
        Mockito
            .`when`(repository.getAcromines())
            .thenReturn(
                flow {
                    ApiResponse.Success<AcromineDataItem>()
                }
            )
        val viewModel = AcronymViewModel(repository)
        viewModel.onSearch()
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(0, viewModel.dataList.value.data?.size?:0)

    }

    @Test
    fun test_onSearch_Failed() = runTest {
        Mockito
            .`when`(repository.getAcromines())
            .thenReturn(
                flow {
                    emit(ApiResponse.Failure("Something Went Wrong"))
                }
            )
        val sut = AcronymViewModel(repository)
        sut.onSearch()
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals("Something Went Wrong", sut.dataList.value.errorMessage?:"Something Went Wrong")
    }
}