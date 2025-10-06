package com.example.assignment4_retrofit_network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WikiApi {

    @Headers("User-Agent: MyAgent/1.0")
    @GET("w/api.php?action=query&format=json&list=search")
    suspend fun getSearchResults(
        @Query("srsearch") name: String
    ): WikiResponse
}