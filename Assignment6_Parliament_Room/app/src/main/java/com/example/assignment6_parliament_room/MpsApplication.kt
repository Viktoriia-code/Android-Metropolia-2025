package com.example.assignment6_parliament_room

import android.app.Application
import com.example.assignment6_parliament_room.data.local.MpsDatabase
import com.example.assignment6_parliament_room.data.remote.MpsApiService
import com.example.assignment6_parliament_room.data.MpRepository
import com.example.assignment6_parliament_room.data.RatingRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MpsApplication : Application() {

    // All app-wide dependencies are stored here
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(app: Application) {

    // 1. Build Moshi (JSON parser)
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // 2. Build Retrofit (HTTP client) using Moshi to parse responses
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://users.metropolia.fi/~peterh/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // 3. Create the API service from Retrofit
    val apiService: MpsApiService = retrofit.create(MpsApiService::class.java)

    // 4. Get the Room database instance
    private val database = MpsDatabase.getDatabase(app)

    // 5. Create repositories (these are what ViewModels use)
    val mpRepository = MpRepository(
        apiService = apiService,
        mpDao      = database.mpDao()
    )

    val ratingRepository = RatingRepository(
        ratingDao = database.ratingDao()
    )
}