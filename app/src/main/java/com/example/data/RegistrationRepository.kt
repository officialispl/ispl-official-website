package com.example.data

import kotlinx.coroutines.flow.Flow

class RegistrationRepository(private val dao: RegistrationDao) {

    val allPlayers: Flow<List<Player>> = dao.getAllPlayers()
    val allSchools: Flow<List<School>> = dao.getAllSchools()
    val allFranchises: Flow<List<Franchise>> = dao.getAllFranchises()
    val allSponsors: Flow<List<Sponsor>> = dao.getAllSponsors()
    val allContacts: Flow<List<Contact>> = dao.getAllContacts()

    suspend fun insertPlayer(player: Player): Long = dao.insertPlayer(player)
    suspend fun updatePlayerStatus(id: Int, status: String) = dao.updatePlayerStatus(id, status)
    suspend fun deletePlayerById(id: Int) = dao.deletePlayerById(id)

    suspend fun insertSchool(school: School): Long = dao.insertSchool(school)
    suspend fun updateSchoolStatus(id: Int, status: String) = dao.updateSchoolStatus(id, status)
    suspend fun deleteSchoolById(id: Int) = dao.deleteSchoolById(id)

    suspend fun insertFranchise(franchise: Franchise): Long = dao.insertFranchise(franchise)
    suspend fun updateFranchiseStatus(id: Int, status: String) = dao.updateFranchiseStatus(id, status)
    suspend fun deleteFranchiseById(id: Int) = dao.deleteFranchiseById(id)

    suspend fun insertSponsor(sponsor: Sponsor): Long = dao.insertSponsor(sponsor)
    suspend fun updateSponsorStatus(id: Int, status: String) = dao.updateSponsorStatus(id, status)
    suspend fun deleteSponsorById(id: Int) = dao.deleteSponsorById(id)

    suspend fun insertContact(contact: Contact): Long = dao.insertContact(contact)
    suspend fun updateContactStatus(id: Int, status: String) = dao.updateContactStatus(id, status)
    suspend fun deleteContactById(id: Int) = dao.deleteContactById(id)
}
