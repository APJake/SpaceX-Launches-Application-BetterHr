package com.apjake.yoteshingeek.data.features.movies.util

import com.apjake.spacexlaunchesbetterhr.data.util.CustomException
import retrofit2.HttpException
import java.io.IOException

object GetErrorMessage {
    fun fromException(e: IOException): String{
        return "No internet! Please check your internet connection."
    }
    fun fromException(e: HttpException): String{
        return when(e.code()){
            400 -> "Bad request"
            401 -> "You are not authorized"
            402 -> "Payment required!!!"
            403 -> "Forbidden"
            404 -> "You request not found"
            405 -> "Method is not allowed!!!"
            else -> "Unhandled error occurred!!!"
        }
    }
    fun fromException(e: CustomException): String{
        return e.message?: "Unhandled error occurred!!!"
    }
}