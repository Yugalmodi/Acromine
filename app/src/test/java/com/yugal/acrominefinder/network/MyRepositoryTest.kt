package com.yugal.acrominefinder.network

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class MyRepositoryTest {

    @Mock
    lateinit var api: MyApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun test_getAcromines_Success() = runTest{
        Mockito.`when`(
            api.getAcro(emptyMap())
        ).thenReturn(
            Response.success(emptyList())
        )
        val sut = MyRepository(api)
        val result= sut.getAcromines().first()
        Assert.assertEquals(0, result.data?.size?:0)
    }
}