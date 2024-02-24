package com.yugal.acrominefinder.network

import com.yugal.acrominefinder.model.AcromineDataItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyRepository @Inject constructor(private val api:MyApi) {

    suspend fun getAcromines(searchFor: String = "sf", searchText: String = "HMM"):
            Flow<ApiResponse<List<AcromineDataItem>>> {
        return flow {
            emit(ApiResponse.Loading())
            val response = api.getAcro(mapOf(searchFor to searchText))
            if (response.isSuccessful && response.body() != null) {
                if(response.body()!!.size>0)
                    emit(ApiResponse.Success(response.body()!!))
                else
                    emit(ApiResponse.Failure("No Data Found"))
            } else {
                emit(ApiResponse.Failure("Error Found ${response.code()} = ${response.errorBody()}"))
            }
        }.catch { e ->
            emit(ApiResponse.Failure(e.localizedMessage ?: "Some Error found"))
        }
    }
}