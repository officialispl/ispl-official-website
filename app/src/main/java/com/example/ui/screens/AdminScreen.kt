package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun AdminScreen(viewModel: ISPLViewModel) {
    val adminCoroutineScope = rememberCoroutineScope()
    var isAdminAuthenticated by remember { mutableStateOf(false) }
    var pinCode by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    // Admin state filters
    val searchQuery by viewModel.adminSearchQuery.collectAsState()
    val filterStatus by viewModel.adminFilterStatus.collectAsState()
    val sortBy by viewModel.adminSortBy.collectAsState()

    // Database arrays
    val players by viewModel.players.collectAsState()
    val schools by viewModel.schools.collectAsState()
    val franchises by viewModel.franchises.collectAsState()
    val sponsors by viewModel.sponsors.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val notifications by viewModel.notificationLogs.collectAsState()

    var activeAdminTab by remember { mutableStateOf("Players") } // Players, Schools, Franchises, Sponsors, Contacts, Notifications
    
    // Details dialog trigger
    var selectedItemForAction by remember { mutableStateOf<Any?>(null) }
    val exportMessage by viewModel.exportState.collectAsState()

    if (!isAdminAuthenticated) {
        // --- 1. PIN LOCK SCREEN ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NavyDeepDusk)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldBright, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(GoldBright.copy(alpha = 0.1f), CircleShape)
                            .size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = GoldBright, modifier = Modifier.size(32.dp))
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "SECURE ADMIN PORTAL",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldBright
                    )
                    Text(
                        text = "Enter 4-Digit administrative token",
                        fontSize = 12.sp,
                        color = SlateTextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = {
                            pinCode = it
                            pinError = false
                        },
                        label = { Text("Admin PIN Code") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        colors = outlinedFieldColors(),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        isError = pinError
                    )

                    if (pinError) {
                        Text(
                            text = "Invalid credentials. Try PIN: 2027",
                            color = CrimsonStatus,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "(Simulated Key: 2027 / admin)",
                        fontSize = 10.sp,
                        color = SlateTextMuted
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (pinCode == "2027" || pinCode.equals("admin", ignoreCase = true)) {
                                isAdminAuthenticated = true
                            } else {
                                pinError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verify Authentication", color = NavyDeepDusk, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // --- 2. AUTHENTICATED SYSTEM CONSOLE ---
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NavyDeepDusk)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Console Header Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🛡️ SECURE CONSOLE ACTIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldStatus,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "ISPL Core Controller",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextLight
                        )
                    }
                    IconButton(
                        onClick = {
                            isAdminAuthenticated = false
                            pinCode = ""
                        },
                        modifier = Modifier.background(CrimsonStatus.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Lock", tint = CrimsonStatus)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // EXPORT BAR ACTION PACK
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyCardBlue, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Reports Center:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldBright)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ReportBtn(label = ".CSV", onClick = { viewModel.triggerExport("CSV", activeAdminTab) })
                        ReportBtn(label = ".XLSX", onClick = { viewModel.triggerExport("Excel", activeAdminTab) })
                        ReportBtn(label = ".PDF", onClick = { viewModel.triggerExport("PDF", activeAdminTab) })
                    }
                }

                // SUB SECTOR CHIP TABS
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf("Players", "Schools", "Franchises", "Sponsors", "Contacts", "Notifications")
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            tabs.take(3).forEach { tab ->
                                AdminSectionChip(title = tab, isActive = activeAdminTab == tab, onClick = { activeAdminTab = tab; viewModel.updateAdminFilters("", "All", "Recent") })
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            tabs.drop(3).forEach { tab ->
                                AdminSectionChip(title = tab, isActive = activeAdminTab == tab, onClick = { activeAdminTab = tab; viewModel.updateAdminFilters("", "All", "Recent") })
                            }
                        }
                    }
                }

                // FILTER AND SEARCH TEXTBOX CARDS
                Spacer(modifier = Modifier.height(10.dp))
                AdminFiltersWidget(
                    search = searchQuery,
                    statusFilter = filterStatus,
                    sort = sortBy,
                    onUpdate = { s, f, st -> viewModel.updateAdminFilters(s, f, st) }
                )

                // ADMINISTRATIVE LISTINGS
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Double-Tap / Click item to update workflow track status:",
                    fontSize = 10.sp,
                    color = SlateTextMuted,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Switch lists based on tab and apply searches & filters dynamically
                    when (activeAdminTab) {
                        "Players" -> {
                            val filtered = players.filter {
                                (searchQuery.isBlank() || it.name.contains(searchQuery, true) || it.school.contains(searchQuery, true) || it.state.contains(searchQuery, true)) &&
                                        (filterStatus == "All" || it.status.equals(filterStatus, true))
                            }.sortedWith { a, b ->
                                if (sortBy == "Name") a.name.compareTo(b.name) else b.timestamp.compareTo(a.timestamp)
                            }
                            items(filtered) { p ->
                                PlayerAdminCard(player = p, onClick = { selectedItemForAction = p })
                            }
                        }
                        "Schools" -> {
                            val filtered = schools.filter {
                                (searchQuery.isBlank() || it.schoolName.contains(searchQuery, true) || it.contactPerson.contains(searchQuery, true)) &&
                                        (filterStatus == "All" || it.status.equals(filterStatus, true))
                            }.sortedWith { a, b ->
                                if (sortBy == "Name") a.schoolName.compareTo(b.schoolName) else b.timestamp.compareTo(a.timestamp)
                            }
                            items(filtered) { s ->
                                SchoolAdminCard(school = s, onClick = { selectedItemForAction = s })
                            }
                        }
                        "Franchises" -> {
                            val filtered = franchises.filter {
                                (searchQuery.isBlank() || it.applicantName.contains(searchQuery, true) || it.targetState.contains(searchQuery, true)) &&
                                        (filterStatus == "All" || it.status.equals(filterStatus, true))
                            }.sortedWith { a, b ->
                                if (sortBy == "Name") a.applicantName.compareTo(b.applicantName) else b.timestamp.compareTo(a.timestamp)
                            }
                            items(filtered) { f ->
                                FranchiseAdminCard(franchise = f, onClick = { selectedItemForAction = f })
                            }
                        }
                        "Sponsors" -> {
                            val filtered = sponsors.filter {
                                (searchQuery.isBlank() || it.companyName.contains(searchQuery, true) || it.brandName.contains(searchQuery, true)) &&
                                        (filterStatus == "All" || it.status.equals(filterStatus, true))
                            }.sortedWith { a, b ->
                                if (sortBy == "Name") a.companyName.compareTo(b.companyName) else b.timestamp.compareTo(a.timestamp)
                            }
                            items(filtered) { sp ->
                                SponsorAdminCard(sponsor = sp, onClick = { selectedItemForAction = sp })
                            }
                        }
                        "Contacts" -> {
                            val filtered = contacts.filter {
                                (searchQuery.isBlank() || it.name.contains(searchQuery, true) || it.subject.contains(searchQuery, true)) &&
                                        (filterStatus == "All" || it.status.equals(filterStatus, true))
                            }.sortedWith { a, b ->
                                if (sortBy == "Name") a.name.compareTo(b.name) else b.timestamp.compareTo(a.timestamp)
                            }
                            items(filtered) { c ->
                                ContactAdminCard(contact = c, onClick = { selectedItemForAction = c })
                            }
                        }
                        "Notifications" -> {
                            val filtered = notifications.filter {
                                searchQuery.isBlank() || it.title.contains(searchQuery, true) || it.description.contains(searchQuery, true) || it.recipient.contains(searchQuery, true)
                            }
                            items(filtered) { notification ->
                                NotificationAdminCard(notification = notification)
                            }
                        }
                    }
                }
            }

            // --- STATUS ACTION WORKFLOW DIALOG ---
            if (selectedItemForAction != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.82f))
                        .clickable { selectedItemForAction = null },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .border(1.dp, GoldBright, RoundedCornerShape(16.dp))
                            .clickable(enabled = false) {}
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "📋 ACTION PROFILE WORKFLOW",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldBright
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Describe details based on entity class type
                            val item = selectedItemForAction
                            var heading = ""
                            var sub = ""
                            var currentStatus = ""
                            if (item is com.example.data.Player) {
                                heading = item.name
                                sub = "Player - ${item.playingRole} @ ${item.school}"
                                currentStatus = item.status
                            } else if (item is com.example.data.School) {
                                heading = item.schoolName
                                sub = "School - Sports HOD: ${item.contactPerson}"
                                currentStatus = item.status
                            } else if (item is com.example.data.Franchise) {
                                heading = item.applicantName
                                sub = "Franchise bid for ${item.targetState} with budget ${item.budget}"
                                currentStatus = item.status
                            } else if (item is com.example.data.Sponsor) {
                                heading = item.companyName
                                sub = "Sponsor (${item.sponsorType}) promoting ${item.brandName}"
                                currentStatus = item.status
                            } else if (item is com.example.data.Contact) {
                                heading = item.name
                                sub = "Inquiry Subject: ${item.subject}\nMsg: \"${item.message}\""
                                currentStatus = item.status
                            }

                            Text(heading, fontSize = 16.sp, fontWeight = FontWeight.Black, color = SlateTextLight, textAlign = TextAlign.Center)
                            Text(sub, fontSize = 11.sp, color = SlateTextMuted, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 2.dp))

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Current State Tag: ", fontSize = 12.sp, color = SlateTextLight)
                                Text(currentStatus.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Black, color = getStatusColor(currentStatus))
                            }

                            // Modification status list buttons
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = SlateTextMuted.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("UPDATE TO NEW WORKFLOW TAG:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldBright)
                            Spacer(modifier = Modifier.height(8.dp))

                            val statusOptions = listOf("New", "Contacted", "Under Review", "Approved", "Rejected")
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                                statusOptions.forEach { statusOpt ->
                                    Button(
                                        onClick = {
                                            adminCoroutineScope.launch {
                                                if (item is com.example.data.Player) {
                                                    viewModel.updatePlayerStatus(item.id, statusOpt)
                                                } else if (item is com.example.data.School) {
                                                    viewModel.updateSchoolStatus(item.id, statusOpt)
                                                } else if (item is com.example.data.Franchise) {
                                                    viewModel.updateFranchiseStatus(item.id, statusOpt)
                                                } else if (item is com.example.data.Sponsor) {
                                                    viewModel.updateSponsorStatus(item.id, statusOpt)
                                                } else if (item is com.example.data.Contact) {
                                                    viewModel.updateContactStatus(item.id, statusOpt)
                                                }
                                                delay(100)
                                                selectedItemForAction = null
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyDeepDusk),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(34.dp)
                                            .border(0.5.dp, getStatusColor(statusOpt), RoundedCornerShape(4.dp)),
                                        shape = RoundedCornerShape(4.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(statusOpt, color = getStatusColor(statusOpt), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // permanent Deletion trigger
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    adminCoroutineScope.launch {
                                        if (item is com.example.data.Player) {
                                            viewModel.deletePlayer(item.id)
                                        } else if (item is com.example.data.School) {
                                            viewModel.deleteSchool(item.id)
                                        } else if (item is com.example.data.Franchise) {
                                            viewModel.deleteFranchise(item.id)
                                        } else if (item is com.example.data.Sponsor) {
                                            viewModel.deleteSponsor(item.id)
                                        } else if (item is com.example.data.Contact) {
                                            viewModel.deleteContact(item.id)
                                        }
                                        delay(100)
                                        selectedItemForAction = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonStatus),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = SlateTextLight, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Delete Record Permanently", color = SlateTextLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Export loader overlay
            if (exportMessage != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .clickable { viewModel.clearExportState() },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(1.dp, GoldBright, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldStatus, modifier = Modifier.size(42.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "EXPORTER COMPLETED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldBright)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = exportMessage ?: "", fontSize = 12.sp, color = SlateTextLight, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Tap block to dismiss", fontSize = 10.sp, color = SlateTextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.AdminSectionChip(title: String, isActive: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (isActive) GoldBright else NavyCardBlue, RoundedCornerShape(4.dp))
            .border(0.5.dp, GoldBright.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isActive) NavyDeepDusk else SlateTextLight)
    }
}

@Composable
fun ReportBtn(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(NavyDeepDusk, RoundedCornerShape(4.dp))
            .border(1.dp, GoldBright, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldBright)
    }
}

@Composable
fun AdminFiltersWidget(
    search: String,
    statusFilter: String,
    sort: String,
    onUpdate: (String, String, String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            OutlinedTextField(
                value = search,
                onValueChange = { onUpdate(it, statusFilter, sort) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SlateTextMuted) },
                placeholder = { Text("Search by keywords...") },
                colors = outlinedFieldColors(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Status Filter Selection row
                Column(modifier = Modifier.weight(1f)) {
                    Text("Filter By Status", fontSize = 10.sp, color = SlateTextMuted)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        val opts = listOf("All", "New", "Approved")
                        opts.forEach { opt ->
                            Box(
                                modifier = Modifier
                                    .background(if (statusFilter == opt) GoldBright else NavyDeepDusk, RoundedCornerShape(4.dp))
                                    .border(0.5.dp, GoldBright, RoundedCornerShape(4.dp))
                                    .clickable { onUpdate(search, opt, sort) }
                                    .weight(1f)
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(opt, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (statusFilter == opt) NavyDeepDusk else SlateTextLight)
                            }
                        }
                    }
                }
                
                // Sorting row
                Column(modifier = Modifier.weight(0.8f)) {
                    Text("Sort By Order", fontSize = 10.sp, color = SlateTextMuted)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        val sOpts = listOf("Recent", "Name")
                        sOpts.forEach { opt ->
                            Box(
                                modifier = Modifier
                                    .background(if (sort == opt) GoldBright else NavyDeepDusk, RoundedCornerShape(4.dp))
                                    .border(0.5.dp, GoldBright, RoundedCornerShape(4.dp))
                                    .clickable { onUpdate(search, statusFilter, opt) }
                                    .weight(1f)
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(opt, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (sort == opt) NavyDeepDusk else SlateTextLight)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Cards
@Composable
fun PlayerAdminCard(player: com.example.data.Player, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, GoldBright.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(getAvatarColor(player.avatarId), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(getAvatarEmoji(player.avatarId), fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = player.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
                Text(text = "School: ${player.school}", fontSize = 11.sp, color = SlateTextMuted, maxLines = 1)
                Row(modifier = Modifier.padding(top = 3.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatIndicatorChip(label = player.ageCategory)
                    StatIndicatorChip(label = player.playingRole)
                    StatIndicatorChip(label = player.state)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(status = player.status)
        }
    }
}

@Composable
fun SchoolAdminCard(school: com.example.data.School, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, GoldBright.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🏫", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = school.schoolName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
                Text(text = "Principal/HOD: ${school.contactPerson} | Phone: ${school.phone}", fontSize = 11.sp, color = SlateTextMuted)
                Text(text = "Proposed Captain Name: ${school.captainName} | Students: ${school.studentsCount}", fontSize = 10.sp, color = GoldDust)
            }
            StatusBadge(status = school.status)
        }
    }
}

@Composable
fun FranchiseAdminCard(franchise: com.example.data.Franchise, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, GoldBright.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🏛️", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = franchise.applicantName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
                Text(text = "Target Franchise: ${franchise.targetState} Titian | Net Worth: ${franchise.netWorth}", fontSize = 11.sp, color = SlateTextMuted)
                Text(text = "Stadium: ${franchise.proposedStadium} | Budget commitment: ${franchise.budget}", fontSize = 10.sp, color = GoldDust)
            }
            StatusBadge(status = franchise.status)
        }
    }
}

@Composable
fun SponsorAdminCard(sponsor: com.example.data.Sponsor, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, GoldBright.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🤝", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = sponsor.companyName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
                Text(text = "Brand Promo: ${sponsor.brandName} | Contact: ${sponsor.phone}", fontSize = 11.sp, color = SlateTextMuted)
                Text(text = "Sponsor Level Interest: ${sponsor.sponsorType}", fontSize = 10.sp, color = GoldBright, fontWeight = FontWeight.Bold)
            }
            StatusBadge(status = sponsor.status)
        }
    }
}

@Composable
fun ContactAdminCard(contact: com.example.data.Contact, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, GoldBright.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("✉️", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = contact.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
                Text(text = "Subject: ${contact.subject} | Email: ${contact.email}", fontSize = 11.sp, color = SlateTextMuted)
                Text(text = "Msg: \"${contact.message}\"", fontSize = 11.sp, color = GoldDust, maxLines = 1)
            }
            StatusBadge(status = contact.status)
        }
    }
}

@Composable
fun StatIndicatorChip(label: String) {
    Box(
        modifier = Modifier
            .background(NavyDeepDusk, RoundedCornerShape(4.dp))
            .border(0.5.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = label, fontSize = 9.sp, color = GoldDust)
    }
}

@Composable
fun StatusBadge(status: String) {
    Box(
        modifier = Modifier
            .background(getStatusColor(status).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
            .border(0.5.dp, getStatusColor(status), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = getStatusColor(status)
        )
    }
}

fun getStatusColor(status: String): Color {
    return when (status) {
        "Approved" -> EmeraldStatus
        "Rejected" -> CrimsonStatus
        "Under Review" -> AmberStatus
        "Contacted" -> SkyStatus
        "New" -> SlateTextLight
        else -> SlateTextMuted
    }
}

@Composable
fun NotificationAdminCard(notification: com.example.ui.NotificationLog) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                0.5.dp,
                when (notification.type) {
                    "Email" -> GoldBright.copy(alpha = 0.3f)
                    "SMS" -> EmeraldStatus.copy(alpha = 0.3f)
                    else -> CrimsonStatus.copy(alpha = 0.3f)
                },
                RoundedCornerShape(8.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (notification.type) {
                        "Email" -> "✉️"
                        "SMS" -> "📱"
                        else -> "⚙️"
                    }
                    Text(icon, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = notification.type.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (notification.type) {
                            "Email" -> GoldBright
                            "SMS" -> EmeraldStatus
                            else -> CrimsonStatus
                        }
                    )
                }
                Text(text = notification.timestamp, fontSize = 10.sp, color = SlateTextMuted)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = notification.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = notification.description, fontSize = 11.sp, color = SlateTextMuted)
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Recipient: ", fontSize = 9.sp, color = SlateTextMuted)
                Text(text = notification.recipient, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldDust)
            }
        }
    }
}
