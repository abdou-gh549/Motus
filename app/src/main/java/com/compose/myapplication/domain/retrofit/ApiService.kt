package com.compose.myapplication.domain.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    /*
    * This function is used to get the motus words from the provided url
     */
    @GET(".")
    suspend fun getMotusData(): Response<String>
}