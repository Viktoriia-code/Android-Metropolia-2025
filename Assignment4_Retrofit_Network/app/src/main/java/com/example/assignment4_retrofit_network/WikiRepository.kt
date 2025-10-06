package com.example.assignment4_retrofit_network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WikiRepository {

    private val api: WikiApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(WikiApi::class.java)
    }

    suspend fun hitCountCheck(name: String): WikiResponse {
        return api.getSearchResults(name)
    }
}