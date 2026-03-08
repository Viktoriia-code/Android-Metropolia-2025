package com.example.assignment6_parliament_room.data.local.mp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MpDao {

    // Save all MPs to the database (replace if they already exist)
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(mps: List<MpEntity>)

    // Get all MPs sorted by last name - returns Flow so UI updates automatically
    @Query("SELECT * FROM members_of_parliament ORDER BY lastname, firstname")
    fun getAllMps(): Flow<List<MpEntity>>

    // Get all unique constituency names
    @Query("SELECT DISTINCT constituency FROM members_of_parliament ORDER BY constituency")
    fun getAllConstituencies(): Flow<List<String>>

    // Get all unique party names
    @Query("SELECT DISTINCT party FROM members_of_parliament ORDER BY party")
    fun getAllParties(): Flow<List<String>>

    // Get MPs from a specific constituency
    @Query("SELECT * FROM members_of_parliament WHERE constituency = :constituency ORDER BY lastname")
    fun getMpsByConstituency(constituency: String): Flow<List<MpEntity>>

    // Get MPs from a specific party
    @Query("SELECT * FROM members_of_parliament WHERE party = :party ORDER BY lastname")
    fun getMpsByParty(party: String): Flow<List<MpEntity>>

    // Get a single MP by their ID
    @Query("SELECT * FROM members_of_parliament WHERE personNumber = :personNumber")
    fun getMpById(personNumber: Int): Flow<MpEntity?>

    // Check how many MPs are in the database (used to decide whether to fetch from network)
    @Query("SELECT COUNT(*) FROM members_of_parliament")
    suspend fun count(): Int

    @Query("UPDATE members_of_parliament SET isFavorite = :isFavorite WHERE personNumber = :personNumber")
    suspend fun setFavorite(personNumber: Int, isFavorite: Boolean)
}