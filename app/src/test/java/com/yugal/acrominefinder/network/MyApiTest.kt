package com.yugal.acrominefinder.network

import com.yugal.acrominefinder.Helper
import dagger.Module
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var myApi: MyApi

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        myApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MyApi::class.java)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun test_getAcro_emptyList() = runTest{
        val mockResponse=MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)
        val response = myApi.getAcro(emptyMap())
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.body()!!.isEmpty())
    }

    @Test
    fun test_getAcro_data() = runTest{
        val mockResponse=MockResponse()
        val data = Helper.readFile("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(data)
        mockWebServer.enqueue(mockResponse)
        val response = myApi.getAcro(emptyMap())
        mockWebServer.takeRequest()
        Assert.assertEquals(false, response.body()!!.isEmpty())
        Assert.assertEquals (1, response.body()!!.size)
    }


    @Test
    fun test_getAcro_Error_500() = runTest{
        val mockResponse=MockResponse()
        val data = Helper.readFile("/response.json")
        mockResponse.setResponseCode(500)
        mockResponse.setBody("Something went wrong!")
        mockWebServer.enqueue(mockResponse)
        val response = myApi.getAcro(emptyMap())
        mockWebServer.takeRequest()
        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals (500, response.code())
    }
}