package com.example.assignment6_parliament_room.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members_of_parliament")
data class MpEntity(
    @PrimaryKey val personNumber: Int,
    val seatNumber: Int?,
    val lastname: String,
    val firstname: String,
    val party: String,
    val minister: Boolean,
    val constituency: String,
    val twitter: String?,
    val bornYear: Int,
    val imageUrl: String?,   // full URL built from lastname + firstname + personNumber
    val isFavorite: Boolean = false
)