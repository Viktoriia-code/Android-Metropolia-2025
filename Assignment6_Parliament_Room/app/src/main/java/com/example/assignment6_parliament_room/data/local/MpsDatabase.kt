package com.example.assignment6_parliament_room.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment6_parliament_room.data.local.mp.MpDao
import com.example.assignment6_parliament_room.data.local.mp.MpEntity
import com.example.assignment6_parliament_room.data.local.rating.RatingDao
import com.example.assignment6_parliament_room.data.local.rating.RatingEntity

@Database(
    entities = [MpEntity::class, RatingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MpsDatabase : RoomDatabase() {

    abstract fun mpDao(): MpDao
    abstract fun ratingDao(): RatingDao

    companion object {
        // @Volatile means the value is always read from main memory, never from cache
        @Volatile
        private var INSTANCE: MpsDatabase? = null

        /**
         * Returns the singleton database instance.
         * Creates it if it doesn't exist yet.
         */
        fun getDatabase(context: Context): MpsDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MpsDatabase::class.java,
                    "mps_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}