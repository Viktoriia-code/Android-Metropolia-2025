package com.example.assignment6_parliament_room.data.remote

import retrofit2.http.GET

interface MpsApiService {
    // GET https://users.metropolia.fi/~peterh/mps.json
    @GET("mps.json")
    suspend fun fetchAllMps(): List<MpDto>
}