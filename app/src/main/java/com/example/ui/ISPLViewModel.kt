package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ISPLViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = RegistrationRepository(database.registrationDao())

    // --- Database Flows ---
    val players: StateFlow<List<Player>> = repository.allPlayers.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val schools: StateFlow<List<School>> = repository.allSchools.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val franchises: StateFlow<List<Franchise>> = repository.allFranchises.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val sponsors: StateFlow<List<Sponsor>> = repository.allSponsors.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val contacts: StateFlow<List<Contact>> = repository.allContacts.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // --- Navigation & Selected Section ---
    private val _currentSection = MutableStateFlow("Home") // Home, Journey, Register, Leaderboards, Admin, About
    val currentSection: StateFlow<String> = _currentSection.asStateFlow()

    fun navigateTo(section: String) {
        _currentSection.value = section
    }

    private fun seedInitialDataIfNecessary() {
        viewModelScope.launch {
            // Wait for DB flow to update
            delay(300)
            if (players.value.isEmpty()) {
                val mockPlayers = listOf(
                    Player(name = "Ishaan Sharma", dob = "2012-05-14", ageCategory = "U14", school = "Delhi Public School (R.K. Puram)", city = "New Delhi", state = "Delhi", mobile = "+91 98112 34567", email = "ishaan@dps.edu.in", playingRole = "Batsman", experience = "State Sub-Junior captain, 3 centuries", status = "Approved", avatarId = 1),
                    Player(name = "Arjun Tendulkar Jr.", dob = "2010-09-22", ageCategory = "U16", school = "Cathedral & John Connon", city = "Mumbai", state = "Maharashtra", mobile = "+91 99988 77766", email = "arjun@mumbai.in", playingRole = "All-Rounder", experience = "District top wicket taker & fast scoring middle order", status = "Under Review", avatarId = 2),
                    Player(name = "Rohit Deshmukh", dob = "2008-01-11", ageCategory = "U19", school = "St. Xavier's International", city = "Pune", state = "Maharashtra", mobile = "+91 91234 56789", email = "rohit@xavier.edu", playingRole = "Bowler", experience = "Right arm leg spin, 5-wicket haul in state trials", status = "Contacted", avatarId = 3),
                    Player(name = "Vivaan Roy", dob = "2011-12-03", ageCategory = "U14", school = "La Martiniere College", city = "Lucknow", state = "Uttar Pradesh", mobile = "+91 88877 66655", email = "vivaan@lamart.edu", playingRole = "Wicketkeeper", experience = "42 dismissals in school league last year", status = "Approved", avatarId = 4),
                    Player(name = "Aditya Verma", dob = "2009-04-18", ageCategory = "U16", school = "Chirec International", city = "Hyderabad", state = "Telangana", mobile = "+91 77766 55544", email = "aditya@chirec.in", playingRole = "Batsman", experience = "Under-16 regional run scorer, average 55.4", status = "Approved", avatarId = 5),
                    Player(name = "Siddharth Sen", dob = "2007-06-30", ageCategory = "U19", school = "Don Bosco School", city = "Kolkata", state = "West Bengal", mobile = "+91 66655 44433", email = "sid@donbosco.in", playingRole = "All-Rounder", experience = "Fast bowling opening batsman, explosive strike rate", status = "New", avatarId = 6)
                )
                mockPlayers.forEach { repository.insertPlayer(it) }
            }

            if (schools.value.isEmpty()) {
                val mockSchools = listOf(
                    School(schoolName = "Delhi Public School (R.K. Puram)", address = "Sector 12, RK Puram, New Delhi", contactPerson = "Dr. Sandeep Malik (HOD Sports)", phone = "+91 98112 34567", email = "sports@dpsrkp.in", studentsCount = 45, captainName = "Ishaan Sharma", status = "Approved"),
                    School(schoolName = "Cathedral & John Connon School", address = "Fort, Mumbai, Maharashtra", contactPerson = "Mrs. Meera Merchant (Principal)", phone = "+91 22220 12345", email = "cricket@cathedral-johnconnon.org", studentsCount = 30, captainName = "Arjun Tendulkar Jr.", status = "Approved"),
                    School(schoolName = "St. Xavier's International School", address = "Camp, Pune, Maharashtra", contactPerson = "Mr. Ramesh Kadam (Coach)", phone = "+91 20263 54321", email = "sports@xavierpune.edu", studentsCount = 22, captainName = "Rohit Deshmukh", status = "Under Review")
                )
                mockSchools.forEach { repository.insertSchool(it) }
            }

            if (franchises.value.isEmpty()) {
                val mockFranchises = listOf(
                    Franchise(applicantName = "Sanjeev Goenka Sports Group", targetState = "Maharashtra", netWorth = "₹500 Crore+", proposedStadium = "Wankhede Stadium, Mumbai", budget = "₹25 Crore", status = "Approved"),
                    Franchise(applicantName = "GMR & JSW Education JV", targetState = "Delhi", netWorth = "₹300 Crore+", proposedStadium = "Arun Jaitley Stadium, New Delhi", budget = "₹18 Crore", status = "Under Review"),
                    Franchise(applicantName = "Royal Indian Builders Pvt Ltd", targetState = "Karnataka", netWorth = "₹150 Crore+", proposedStadium = "M. Chinnaswamy Stadium, Bengaluru", budget = "₹12 Crore", status = "New")
                )
                mockFranchises.forEach { repository.insertFranchise(it) }
            }

            if (sponsors.value.isEmpty()) {
                val mockSponsors = listOf(
                    Sponsor(companyName = "Tata Group", brandName = "TATA", email = "sponsor@tata.com", sponsorType = "Title Sponsor", phone = "+91 22666 58282", status = "Approved"),
                    Sponsor(companyName = "Reliance Jio", brandName = "JIO", email = "corporate@jio.com", sponsorType = "Powered By Sponsor", phone = "+91 22447 70000", status = "Approved"),
                    Sponsor(companyName = "MRF Tyres", brandName = "MRF", email = "cricket@mrf.com", sponsorType = "Associate Sponsor", phone = "+91 44282 92777", status = "Approved")
                )
                mockSponsors.forEach { repository.insertSponsor(it) }
            }

            if (contacts.value.isEmpty()) {
                val mockContacts = listOf(
                    Contact(name = "Sourav Ganguly", email = "sourav@bcci-advisor.in", subject = "Scouting Collaboration", message = "Extremely excited about ISPL. I would love to explore how we can connect local school talent with state academies.", status = "Contacted"),
                    Contact(name = "Anil Kumble", email = "anil@spinacad.com", subject = "Coaching Association", message = "We want to bring our high performance training camps to ISPL registered players.", status = "New")
                )
                mockContacts.forEach { repository.insertContact(it) }
            }
        }
    }


    // --- Countdown Timer States ---
    private val targetDateString = "2027-02-01 09:00:00"
    private val targetTimeMs: Long by lazy {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            }
            sdf.parse(targetDateString)?.time ?: 1798794000000L
        } catch (e: Exception) {
            1798794000000L // Fallback
        }
    }

    private val _countdownState = MutableStateFlow(CountdownTime(0, 0, 0, 0, isFinished = false))
    val countdownState: StateFlow<CountdownTime> = _countdownState.asStateFlow()

    // Interactive developer toggle to force count down completion state instantly!
    private val _forceLiveState = MutableStateFlow(false)
    val forceLiveState: StateFlow<Boolean> = _forceLiveState.asStateFlow()

    fun toggleForceLiveState() {
        _forceLiveState.value = !_forceLiveState.value
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                if (_forceLiveState.value) {
                    _countdownState.value = CountdownTime(0, 0, 0, 0, isFinished = true)
                } else {
                    val currentMs = System.currentTimeMillis()
                    val diff = targetTimeMs - currentMs
                    if (diff <= 0) {
                        _countdownState.value = CountdownTime(0, 0, 0, 0, isFinished = true)
                    } else {
                        val seconds = (diff / 1000) % 60
                        val minutes = (diff / (1000 * 60)) % 60
                        val hours = (diff / (1000 * 60 * 60)) % 24
                        val days = diff / (1000 * 60 * 60 * 24)
                        _countdownState.value = CountdownTime(
                            days = days,
                            hours = hours,
                            minutes = minutes,
                            seconds = seconds,
                            isFinished = false
                        )
                    }
                }
                delay(1000)
            }
        }
    }


    // --- Form Syncing States & Automation Mock ---
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncMessage = MutableStateFlow("")
    val syncMessage: StateFlow<String> = _syncMessage.asStateFlow()

    private val _lastRegisteredPlayer = MutableStateFlow<Player?>(null)
    val lastRegisteredPlayer: StateFlow<Player?> = _lastRegisteredPlayer.asStateFlow()

    private val _successDialogMessage = MutableStateFlow<String?>(null)
    val successDialogMessage: StateFlow<String?> = _successDialogMessage.asStateFlow()

    fun clearSuccessDialog() {
        _successDialogMessage.value = null
        _lastRegisteredPlayer.value = null
    }

    // --- Notification logs ---
    private val _notificationLogs = MutableStateFlow<List<NotificationLog>>(emptyList())
    val notificationLogs: StateFlow<List<NotificationLog>> = _notificationLogs.asStateFlow()

    fun addNotificationLog(title: String, description: String, type: String, recipient: String) {
        val sdf = SimpleDateFormat("HH:mm:ss, dd MMM", Locale.getDefault())
        val log = NotificationLog(
            id = _notificationLogs.value.size + 1,
            title = title,
            description = description,
            type = type,
            recipient = recipient,
            timestamp = sdf.format(Date())
        )
        _notificationLogs.value = listOf(log) + _notificationLogs.value
    }

    // Dynamic submit handlers wrapped with sheets, emails, DB transactions and success delays
    fun submitPlayerRegistration(
        name: String, dob: String, ageCat: String, school: String,
        city: String, state: String, mobile: String, email: String,
        role: String, experience: String, avatarId: Int, photoUri: String? = null
    ) {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Establishing Room Transaction..."
            delay(500)
            if (photoUri != null) {
                _syncMessage.value = "Optimizing photo file: ${photoUri.substringAfterLast("/")}..."
                delay(600)
                _syncMessage.value = "Uploading photo to secure servers..."
                delay(600)
            }
            _syncMessage.value = "Saving record locally under id: pending..."
            delay(500)
            _syncMessage.value = "Auto-syncing to ISPL Sheets Hub [ID: xlsx_ispl_s1]..."
            delay(500)
            _syncMessage.value = "Emailing receipt to $email via SMTP..."
            delay(600)
            _syncMessage.value = "Success! Generating ID CARD..."
            delay(400)

            val newPlayer = Player(
                name = name,
                dob = dob,
                ageCategory = ageCat,
                school = school,
                city = city,
                state = state,
                mobile = mobile,
                email = email,
                playingRole = role,
                experience = experience,
                avatarId = avatarId,
                status = "New",
                photoUri = photoUri
            )

            val insertedId = repository.insertPlayer(newPlayer)
            val savedPlayer = newPlayer.copy(id = insertedId.toInt())
            _lastRegisteredPlayer.value = savedPlayer

            // Trigger mock backend notification sends
            addNotificationLog(
                title = "Trial Booking Confirmation",
                description = "Hi $name, your registration for ISPL Season 1 Trials ($ageCat) has been received. Venue schedule is sent to your device.",
                type = "Email",
                recipient = email
            )
            addNotificationLog(
                title = "Mobile Registration Code",
                description = "ISPL Gateway SMS: Trial registration code is ISPL-S1-${1000 + insertedId}. Show on arriving.",
                type = "SMS",
                recipient = mobile
            )
            addNotificationLog(
                title = "Secure Sync Active Alert",
                description = "System alert: New Player '$name' ($role, $ageCat) registered from $state. Profile picture processed.",
                type = "System",
                recipient = "Admin Dashboard"
            )

            _isSyncing.value = false
            _successDialogMessage.value = "Hi $name, your registration for $ageCat category has been synced successfully to Google Sheets & SQLite DB. Our trial officers will reach out on $mobile soon!"
        }
    }

    fun submitSchoolRegistration(
        name: String, address: String, person: String, phone: String, email: String, students: Int, captain: String, documentUri: String? = null
    ) {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Encrypting school data packet..."
            delay(500)
            if (documentUri != null) {
                _syncMessage.value = "Scanning attachment proof: ${documentUri.substringAfterLast("/")}..."
                delay(600)
            }
            _syncMessage.value = "Syncing with Master School Registry Sheet..."
            delay(600)
            _syncMessage.value = "Mailing confirmation packet to official school email: $email..."
            delay(700)

            val schoolObj = School(
                schoolName = name, address = address, contactPerson = person, phone = phone, email = email, studentsCount = students, captainName = captain, documentUri = documentUri
            )
            val insertedId = repository.insertSchool(schoolObj)

            // Trigger mock backend notifications
            addNotificationLog(
                title = "School Onboarding Proposal",
                description = "Official school affiliation draft packet has been delivered to HOD $person regarding integration of $name school board.",
                type = "Email",
                recipient = email
            )
            addNotificationLog(
                title = "Academic Portal Hook",
                description = "Admin alert: School registry node created for $name with $students interested candidates. Document verified.",
                type = "System",
                recipient = "Academic Committee"
            )

            _isSyncing.value = false
            _successDialogMessage.value = "School '$name' registered successfully. Welcome to the ISPL regional platform!"
        }
    }

    fun submitFranchiseApplication(
        name: String, state: String, netWorth: String, stadium: String, budget: String, documentUri: String? = null
    ) {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Filing Bid proposal securely lock..."
            delay(600)
            if (documentUri != null) {
                _syncMessage.value = "Uploaging and scanning franchise financial audit: ${documentUri.substringAfterLast("/")}..."
                delay(700)
            }
            _syncMessage.value = "Logging to Board of Trustees secure vault sheet..."
            delay(800)

            val franchiseObj = Franchise(
                applicantName = name, targetState = state, netWorth = netWorth, proposedStadium = stadium, budget = budget, documentUri = documentUri
            )
            val insertedId = repository.insertFranchise(franchiseObj)

            // Trigger mock backend notification emails
            addNotificationLog(
                title = "State Bid Proposal Receipt",
                description = "Tender proposition logged. Bidding group: $name target state: $state. Core bid commitment ₹$budget.",
                type = "Email",
                recipient = "bids-liaison@isplcricket.in"
            )
            addNotificationLog(
                title = "Franchise Financial Submission",
                description = "High Priority: Franchise application filed by $name for $state. Audit reports scanned successfully.",
                type = "System",
                recipient = "Board of Trustees"
            )

            _isSyncing.value = false
            _successDialogMessage.value = "Franchise Application for $state submitted which is Under Review. Premium packet mailed!"
        }
    }

    fun submitSponsorRegistration(
        company: String, brand: String, email: String, type: String, phone: String, logoUri: String? = null
    ) {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Registering Corporate Partnership Request..."
            delay(500)
            if (logoUri != null) {
                _syncMessage.value = "Fitting brand emblem logo: ${logoUri.substringAfterLast("/")}..."
                delay(600)
            }
            _syncMessage.value = "Syncing with Sponsorship sheets pool..."
            delay(700)

            val sponsorObj = Sponsor(
                companyName = company, brandName = brand, email = email, sponsorType = type, phone = phone, logoUri = logoUri
            )
            val insertedId = repository.insertSponsor(sponsorObj)

            // Trigger mock notifications
            addNotificationLog(
                title = "Sponsor Partnership Handshake",
                description = "Welcome $brand as a pending $type partner! Integration contract guidelines transmitted to $email.",
                type = "Email",
                recipient = email
            )
            addNotificationLog(
                title = "SIM Desk Handshake Dispatch",
                description = "Liaison direct call scheduled. SMS ping sent confirmation to executive $phone.",
                type = "SMS",
                recipient = phone
            )

            _isSyncing.value = false
            _successDialogMessage.value = "Sponsor Request of $brand ($type level) filed successfully! Pitch Deck and contract drafted to $email."
        }
    }

    fun submitContactMessage(
        name: String, email: String, subject: String, message: String
    ) {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Filing customer ticketing..."
            delay(600)
            _syncMessage.value = "Uploading to HelpDesk Sheet pool..."
            delay(600)

            val contactObj = Contact(
                name = name, email = email, subject = subject, message = message
            )
            repository.insertContact(contactObj)
            _isSyncing.value = false
            _successDialogMessage.value = "Thank you $name. Your message regarding '$subject' has been delivered safely. Ticket ID created!"
        }
    }


    // --- Administrative Commands ---
    fun updatePlayerStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updatePlayerStatus(id, status)
        }
    }
    fun deletePlayer(id: Int) {
        viewModelScope.launch {
            repository.deletePlayerById(id)
        }
    }

    fun updateSchoolStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateSchoolStatus(id, status)
        }
    }
    fun deleteSchool(id: Int) {
        viewModelScope.launch {
            repository.deleteSchoolById(id)
        }
    }

    fun updateFranchiseStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateFranchiseStatus(id, status)
        }
    }
    fun deleteFranchise(id: Int) {
        viewModelScope.launch {
            repository.deleteFranchiseById(id)
        }
    }

    fun updateSponsorStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateSponsorStatus(id, status)
        }
    }
    fun deleteSponsor(id: Int) {
        viewModelScope.launch {
            repository.deleteSponsorById(id)
        }
    }

    fun updateContactStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateContactStatus(id, status)
        }
    }
    fun deleteContact(id: Int) {
        viewModelScope.launch {
            repository.deleteContactById(id)
        }
    }


    // --- Search, Filter & Sort states ---
    // For general reuse across admin tabs
    private val _adminSearchQuery = MutableStateFlow("")
    val adminSearchQuery: StateFlow<String> = _adminSearchQuery.asStateFlow()

    private val _adminFilterStatus = MutableStateFlow("All") // All, New, Contacted, Under Review, Approved, Rejected
    val adminFilterStatus: StateFlow<String> = _adminFilterStatus.asStateFlow()

    private val _adminSortBy = MutableStateFlow("Recent") // Recent, Name
    val adminSortBy: StateFlow<String> = _adminSortBy.asStateFlow()

    fun updateAdminFilters(search: String, filter: String, sort: String) {
        _adminSearchQuery.value = search
        _adminFilterStatus.value = filter
        _adminSortBy.value = sort
    }

    // --- Dynamic Simulated Local CSV/PDF Export State ---
    private val _exportState = MutableStateFlow<String?>(null)
    val exportState: StateFlow<String?> = _exportState.asStateFlow()

    fun clearExportState() {
        _exportState.value = null
    }

    fun triggerExport(docType: String, sectionName: String) {
        viewModelScope.launch {
            _exportState.value = "Generating $docType for $sectionName..."
            delay(1200)
            _exportState.value = "Export completed successfully! Saved as: ISPL_${sectionName}_Report.$docType under Downloads."
        }
    }


    // --- Live Score simulation ---
    private val _liveMatch = MutableStateFlow(MatchScore("DPS Delhi Titans", "Cathedral Mumbai Kings", "DPS", 138, 4, 18, 3, "Arjun Tendulkar Jr. 45*(22), Rohit Deshmukh 2/24", "Final Over Thriller! DPS Titans need 12 runs off 6 balls!"))
    val liveMatch: StateFlow<MatchScore> = _liveMatch.asStateFlow()

    // --- Seed Data Check & Initialization ---
    init {
        seedInitialDataIfNecessary()
        startCountdownTimer()
        startLiveScoreSimulation()
    }

    private fun startLiveScoreSimulation() {
        viewModelScope.launch {
            var score = 138
            var wickets = 4
            var overs = 18
            var balls = 3
            val comments = listOf(
                "Spectacular cover drive by Ishaan Sharma for FOUR!",
                "Huge appeal for LBW! Turned down by the umpire.",
                "WHAT A HIT! Smashed straight down the ground for SIX!",
                "In the air... and is TAKEN! Wicket down!",
                "Quick single taken, excellent running between the wickets.",
                "Excellent yorker, squeezed out for a single."
            )

            while (true) {
                delay(4000) // update every 4 seconds
                if (_countdownState.value.isFinished) {
                    balls += 1
                    if (balls >= 6) {
                        balls = 0
                        overs += 1
                    }
                    if (overs >= 20) {
                        overs = 15
                        score = 110
                        wickets = 3
                    }

                    // add some runs
                    val chance = (0..10).random()
                    var lastAction = ""
                    when {
                        chance == 0 -> {
                            if (wickets < 9) {
                                wickets++
                                lastAction = "WICKET! Beautiful delivery clipped the top of off stump."
                            }
                        }
                        chance < 3 -> {
                            score += 4
                            lastAction = "FOUR! " + comments.random()
                        }
                        chance < 5 -> {
                            score += 6
                            lastAction = "SIX! " + comments.random()
                        }
                        else -> {
                            score += (0..2).random()
                            lastAction = comments.random()
                        }
                    }

                    _liveMatch.value = MatchScore(
                        teamA = "DPS Delhi Titans",
                        teamB = "Cathedral Mumbai Kings",
                        battingTeam = "DPS",
                        runs = score,
                        wickets = wickets,
                        overs = overs,
                        balls = balls,
                        highlights = "Ishaan Sharma 62*(35), Arjun Tendulkar Jr. 2/28",
                        commentary = lastAction
                    )
                }
            }
        }
    }
}

data class CountdownTime(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    val isFinished: Boolean
)

data class MatchScore(
    val teamA: String,
    val teamB: String,
    val battingTeam: String,
    val runs: Int,
    val wickets: Int,
    val overs: Int,
    val balls: Int,
    val highlights: String,
    val commentary: String
)

data class NotificationLog(
    val id: Int,
    val title: String,
    val description: String,
    val type: String, // Email, SMS, System
    val recipient: String,
    val timestamp: String
)
