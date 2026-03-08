package com.example.assignment6_parliament_room.data

import com.example.assignment6_parliament_room.data.local.MpDao
import com.example.assignment6_parliament_room.data.local.MpEntity
import com.example.assignment6_parliament_room.data.remote.MpsApiService
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * Repository for MP data.
 *
 * This is the single source of truth for MPs in the app.
 * The ViewModel talks to this class - never directly to the database or network.
 *
 * Flow of data:
 *   Network (Retrofit) → Repository → Room database → ViewModel → UI
 */
class MpRepository(
    private val apiService: MpsApiService,
    private val mpDao: MpDao
) {

    // ── Public read operations (return Flow so UI updates automatically) ──────────

    fun getAllMps(): Flow<List<MpEntity>> = mpDao.getAllMps()

    fun getAllConstituencies(): Flow<List<String>> = mpDao.getAllConstituencies()

    fun getAllParties(): Flow<List<String>> = mpDao.getAllParties()

    fun getMpsByConstituency(constituency: String): Flow<List<MpEntity>> =
        mpDao.getMpsByConstituency(constituency)

    fun getMpsByParty(party: String): Flow<List<MpEntity>> =
        mpDao.getMpsByParty(party)

    fun getMpById(personNumber: Int): Flow<MpEntity?> =
        mpDao.getMpById(personNumber)

    // ── Data loading ──────────────────────────────────────────────────────────────

    /**
     * Load MPs from the network only if the database is empty.
     * Called once when the app starts.
     */
    suspend fun loadIfEmpty() {
        if (mpDao.count() == 0) {
            fetchFromNetwork()
        }
    }

    /** Force a fresh download from the API (e.g. when user pulls to refresh). */
    suspend fun refresh() {
        fetchFromNetwork()
    }

    /** Downloads MP data from the API and saves it to Room. */
    private suspend fun fetchFromNetwork() {
        val dtos = apiService.fetchAllMps()
        val entities = dtos.map { dto ->
            MpEntity(
                personNumber = dto.personNumber,
                seatNumber   = dto.seatNumber,
                lastname     = dto.lastname,
                firstname    = dto.firstname,
                party        = dto.party,
                minister     = dto.minister,
                constituency = dto.constituency,
                twitter      = dto.twitter,
                bornYear     = dto.bornYear,
                // Build the photo URL: e.g. "Essayah-Sari-web-778.jpg"
                imageUrl     = buildImageUrl(dto.lastname, dto.firstname, dto.personNumber)
            )
        }
        mpDao.insertAll(entities)
    }

    // ── Helper ────────────────────────────────────────────────────────────────────

    /**
     * Builds the full photo URL for an MP.
     * Example: Sari Essayah (778) → ".../Essayah-Sari-web-778.jpg"
     */
    private fun buildImageUrl(lastname: String, firstname: String, personNumber: Int): String {
        return "https://users.metropolia.fi/~peterh/edustajakuvat/${lastname}-${firstname}-web-${personNumber}.jpg"
    }

    suspend fun setFavorite(personNumber: Int, isFavorite: Boolean) {
        mpDao.setFavorite(personNumber, isFavorite)
    }
}

/** Helper extension: calculates age from birth year. */
fun MpEntity.age(): Int = Calendar.getInstance().get(Calendar.YEAR) - bornYear

/** Helper extension: returns full name as "Firstname Lastname". */
fun MpEntity.fullName(): String = "$firstname $lastname"
