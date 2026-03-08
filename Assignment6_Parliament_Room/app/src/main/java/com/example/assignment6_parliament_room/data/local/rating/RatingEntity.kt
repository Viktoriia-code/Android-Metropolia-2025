package com.example.assignment6_parliament_room.data.local.rating

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.assignment6_parliament_room.data.local.mp.MpEntity

@Entity(
    tableName = "ratings",
    foreignKeys = [
        ForeignKey(
            entity = MpEntity::class,
            parentColumns = ["personNumber"],
            childColumns = ["mpPersonNumber"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mpPersonNumber")]
)
data class RatingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mpPersonNumber: Int,        // which MP this rating belongs to
    val isPositive: Boolean,        // true = thumbs up (+), false = thumbs down (-)
    val comment: String,            // short text from the user
    val timestamp: Long = System.currentTimeMillis()
)