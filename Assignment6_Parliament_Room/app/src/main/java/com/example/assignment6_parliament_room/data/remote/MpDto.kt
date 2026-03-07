package com.example.assignment6_parliament_room.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MpDto(
    @Json(name = "personNumber") val personNumber: Int,
    @Json(name = "seatNumber")   val seatNumber: Int?,
    @Json(name = "lastname")     val lastname: String,
    @Json(name = "firstname")    val firstname: String,
    @Json(name = "party")        val party: String,
    @Json(name = "minister")     val minister: Boolean,
    @Json(name = "constituency") val constituency: String,
    @Json(name = "twitter")      val twitter: String?,
    @Json(name = "bornYear")     val bornYear: Int,
    @Json(name = "pictureUrl")   val pictureUrl: String?
)