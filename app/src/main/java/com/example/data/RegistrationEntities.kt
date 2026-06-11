package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dob: String,
    val ageCategory: String, // U14, U16, U19
    val school: String,
    val city: String,
    val state: String,
    val mobile: String,
    val email: String,
    val playingRole: String, // Batsman, Bowler, All-Rounder, Wicketkeeper
    val experience: String,
    val avatarId: Int = 1,
    val status: String = "New", // New, Contacted, Under Review, Approved, Rejected
    val timestamp: Long = System.currentTimeMillis(),
    val photoUri: String? = null
)

@Entity(tableName = "schools")
data class School(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val schoolName: String,
    val address: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val studentsCount: Int,
    val captainName: String,
    val status: String = "New",
    val timestamp: Long = System.currentTimeMillis(),
    val documentUri: String? = null
)

@Entity(tableName = "franchises")
data class Franchise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val applicantName: String,
    val targetState: String,
    val netWorth: String,
    val proposedStadium: String,
    val budget: String,
    val status: String = "New",
    val timestamp: Long = System.currentTimeMillis(),
    val documentUri: String? = null
)

@Entity(tableName = "sponsors")
data class Sponsor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyName: String,
    val brandName: String,
    val email: String,
    val sponsorType: String, // Title, Powered By, Associate, Partner
    val phone: String,
    val status: String = "New",
    val timestamp: Long = System.currentTimeMillis(),
    val logoUri: String? = null
)

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val status: String = "New",
    val timestamp: Long = System.currentTimeMillis()
)
