package com.yugal.acrominefinder.network

import com.yugal.acrominefinder.model.AcromineDataItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MyApi {

    @GET("software/acromine/dictionary.py")
    suspend fun getAcro(@QueryMap options:Map<String, String>): Response<List<AcromineDataItem>>
}