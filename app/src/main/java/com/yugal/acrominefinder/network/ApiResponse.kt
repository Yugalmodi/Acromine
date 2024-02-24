package com.yugal.acrominefinder.network

sealed class ApiResponse<T> (val data:T?=null, val errorMessage:String?=null){
    class Initial<T>:ApiResponse<T>()
    class Loading<T>:ApiResponse<T>()
    class Success<T>(data: T?=null):ApiResponse<T>(data=data)
    class Failure<T>(errorMessage:String?=null):ApiResponse<T>(errorMessage=errorMessage)
}

