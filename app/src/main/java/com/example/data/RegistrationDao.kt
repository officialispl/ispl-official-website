package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistrationDao {

    // --- Players ---
    @Query("SELECT * FROM players ORDER BY timestamp DESC")
    fun getAllPlayers(): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player): Long

    @Query("UPDATE players SET status = :status WHERE id = :id")
    suspend fun updatePlayerStatus(id: Int, status: String)

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deletePlayerById(id: Int)

    // --- Schools ---
    @Query("SELECT * FROM schools ORDER BY timestamp DESC")
    fun getAllSchools(): Flow<List<School>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School): Long

    @Query("UPDATE schools SET status = :status WHERE id = :id")
    suspend fun updateSchoolStatus(id: Int, status: String)

    @Query("DELETE FROM schools WHERE id = :id")
    suspend fun deleteSchoolById(id: Int)

    // --- Franchises ---
    @Query("SELECT * FROM franchises ORDER BY timestamp DESC")
    fun getAllFranchises(): Flow<List<Franchise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFranchise(franchise: Franchise): Long

    @Query("UPDATE franchises SET status = :status WHERE id = :id")
    suspend fun updateFranchiseStatus(id: Int, status: String)

    @Query("DELETE FROM franchises WHERE id = :id")
    suspend fun deleteFranchiseById(id: Int)

    // --- Sponsors ---
    @Query("SELECT * FROM sponsors ORDER BY timestamp DESC")
    fun getAllSponsors(): Flow<List<Sponsor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSponsor(sponsor: Sponsor): Long

    @Query("UPDATE sponsors SET status = :status WHERE id = :id")
    suspend fun updateSponsorStatus(id: Int, status: String)

    @Query("DELETE FROM sponsors WHERE id = :id")
    suspend fun deleteSponsorById(id: Int)

    // --- Contacts ---
    @Query("SELECT * FROM contacts ORDER BY timestamp DESC")
    fun getAllContacts(): Flow<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact): Long

    @Query("UPDATE contacts SET status = :status WHERE id = :id")
    suspend fun updateContactStatus(id: Int, status: String)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Int)
}
