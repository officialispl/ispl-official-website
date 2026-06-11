package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Player
import com.example.ui.ISPLViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.widget.Toast

@Composable
fun DashboardScreen(
    viewModel: ISPLViewModel,
    modifier: Modifier = Modifier
) {
    val players by viewModel.players.collectAsState()
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Keep track of any simulated player progress updates
    var isSimulatingTrial by remember { mutableStateOf(false) }
    var simulationStep by remember { mutableStateOf("") }
    var isPayingFee by remember { mutableStateOf(false) }

    // Synchronize active selected player when players flow changes or on initialization
    LaunchedEffect(players) {
        if (players.isNotEmpty()) {
            if (selectedPlayer == null || !players.any { it.id == selectedPlayer?.id }) {
                selectedPlayer = players.first()
            } else {
                // Keep selectedPlayer reference in sync with latest db updates
                selectedPlayer = players.first { it.id == selectedPlayer?.id }
            }
        }
    }

    // Mathematical Seeded stats generator
    val metrics = remember(selectedPlayer, isSimulatingTrial) {
        selectedPlayer?.let { getScoutingMetricsForPlayer(it) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDeepDusk)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // --- MODULE HEADER ---
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚡ ADAPTIVE SCOUTING COCKPIT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldBright,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "ISPL Performance & Draft Readiness",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextLight,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- PLAYER SELECTION SLIDER ---
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👤 SELECT REGISTERED PROFILE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(NavyCardBlue, CircleShape)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Total: ${players.size}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldBright
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    if (players.isEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No players found. Please use the 'Register' tab to register a school or player.",
                                fontSize = 12.sp,
                                color = SlateTextMuted,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("player_selector")
                        ) {
                            items(players) { player ->
                                val isSelected = selectedPlayer?.id == player.id
                                val borderBrush = if (isSelected) {
                                    Brush.linearGradient(listOf(GoldBright, GoldAmber))
                                } else {
                                    Brush.linearGradient(listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.05f)))
                                }
                                
                                val statusColor = when (player.status) {
                                    "Approved" -> EmeraldStatus
                                    "Contacted" -> SkyStatus
                                    "Under Review" -> AmberStatus
                                    "Rejected" -> CrimsonStatus
                                    else -> SlateTextMuted
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSelected) NavyCardBlue else NavyCardBlue.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .width(160.dp)
                                        .border(
                                            width = if (isSelected) 2.dp else 0.5.dp,
                                            brush = borderBrush,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedPlayer = player }
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(GoldBright.copy(alpha = 0.2f), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = getEmojiForRole(player.playingRole),
                                                    fontSize = 14.sp
                                                )
                                            }
                                            Column {
                                                Text(
                                                    text = player.name,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SlateTextLight,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = player.ageCategory,
                                                    fontSize = 9.sp,
                                                    color = GoldBright
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = player.playingRole,
                                                fontSize = 9.sp,
                                                color = SlateTextMuted
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = player.status.uppercase(),
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = statusColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- ACTIVE PROFILE DISPLAY PACKETET ---
            selectedPlayer?.let { player ->
                val metricData = metrics ?: getScoutingMetricsForPlayer(player)

                // 1. Profile Summary Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, GoldBright.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Dynamic styled avatar ID representation
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(Brush.radialGradient(listOf(GoldBright.copy(alpha = 0.4f), Color.Transparent)), CircleShape)
                                        .border(2.dp, GoldBright, CircleShape)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = getProfileEmojiById(player.avatarId),
                                        fontSize = 32.sp
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = player.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SlateTextLight
                                        )
                                        if (player.status == "Approved") {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = "Verified",
                                                tint = EmeraldStatus,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = player.school,
                                        fontSize = 12.sp,
                                        color = SlateTextMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        BadgeChip(text = player.ageCategory, color = GoldBright, bgColor = GoldBright.copy(alpha = 0.15f))
                                        BadgeChip(text = player.playingRole, color = Color.White, bgColor = NavyLightBlue)
                                        BadgeChip(text = "${player.city}, ${player.state}", color = SlateTextMuted, bgColor = NavyDeepDusk)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = SlateTextMuted.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "REGISTERED NUMBER", fontSize = 8.sp, color = SlateTextMuted)
                                    Text(text = player.mobile, fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(text = "SCOUT ID NUMBER", fontSize = 8.sp, color = SlateTextMuted)
                                    Text(text = "ISPL-S1-${1982 + player.id}", fontSize = 11.sp, color = GoldBright, fontWeight = FontWeight.ExtraBold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "TRIAL VENUE STATE", fontSize = 8.sp, color = SlateTextMuted)
                                    Text(text = player.state.uppercase(), fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // 2. Performance Metrics Progress Bars
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyCardBlue, RoundedCornerShape(16.dp))
                            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "📐 SCOUTING TRIAL METRICS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldBright
                        )
                        Text(
                            text = "Evaluated performance parameters derived from zonal coaching sessions.",
                            fontSize = 10.sp,
                            color = SlateTextMuted,
                            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                        )

                        // Progress metric blocks
                        val isBowler = player.playingRole.lowercase() == "bowler"
                        val isKeeper = player.playingRole.lowercase() == "wicketkeeper"

                        val firstMetricLabel = if (isBowler) "BOWLING PACE / FLIGHT SPEED" else "BATTING TIMING & POWER"
                        val firstMetricMax = if (isBowler) "145 km/h" else "100%"
                        val firstMetricScore = metricData.scoutTimingPower / 100f

                        val secondMetricLabel = if (isBowler) "BOWLING ACCURACY & SEAM" else if (isKeeper) "GLOVE-WORK REFLEXES" else "SHOT SELECTION RATING"
                        val secondMetricScore = metricData.scoutPaceAccuracy / 100f

                        MetricGaugeRow(
                            label = firstMetricLabel,
                            score = firstMetricScore,
                            scoreText = if (isBowler) "${(100 + (metricData.scoutTimingPower * 0.45)).toInt()} km/h" else "${metricData.scoutTimingPower}%",
                            color = GoldBright
                        )

                        MetricGaugeRow(
                            label = secondMetricLabel,
                            score = secondMetricScore,
                            scoreText = "${metricData.scoutPaceAccuracy}%",
                            color = Color(0xFF3B82F6) // Sky accent
                        )

                        MetricGaugeRow(
                            label = "FIELDING VELOCITY & AGILITY",
                            score = metricData.scoutFieldingAgility / 100f,
                            scoreText = "${metricData.scoutFieldingAgility}%",
                            color = EmeraldStatus
                        )

                        MetricGaugeRow(
                            label = "YO-YO FITNESS ENDURANCE",
                            score = ((metricData.scoutYoYoScore - 14.0) / (22.0 - 14.0)).toFloat().coerceIn(0f, 1f),
                            scoreText = "${String.format("%.1f", metricData.scoutYoYoScore)} (Score)",
                            color = Color(0xFFA855F7) // Purple
                        )

                        MetricGaugeRow(
                            label = "TACTICAL ACUMEN & MENTAL TOUGHNESS",
                            score = metricData.scoutMentalToughness / 100f,
                            scoreText = "${metricData.scoutMentalToughness}%",
                            color = Color(0xFFEF4444) // Red accent
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NavyDeepDusk, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = "Scout Remarks", tint = GoldBright, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "EXECUTIVE SCOUT REVIEW", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldBright)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${metricData.remarks}\"",
                                    fontSize = 11.sp,
                                    color = SlateTextLight,
                                    fontStyle = FontStyle.Italic,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                // 3. Player Stats Grid
                item {
                    Column {
                        Text(
                            text = "📊 HISTORICAL PLAYER STATISTICS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldBright,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Render Stats based on player's registered role!
                        val roleLower = player.playingRole.lowercase()
                        when {
                            roleLower.contains("bats") -> {
                                StatsGrid(
                                    stats = listOf(
                                        StatItem("Matches", metricData.matches.toString(), "🏟️ Matches played"),
                                        StatItem("Total Runs", metricData.statRuns.toString(), "🏏 Runs scored"),
                                        StatItem("Batting Avg", String.format("%.2f", metricData.statAvg), "📈 Average"),
                                        StatItem("Strike Rate", String.format("%.1f", metricData.statSR), "⚡ Scoring rate"),
                                        StatItem("High Score", metricData.statHS, "🔝 Best performance"),
                                        StatItem("Fours / Sixes", "${metricData.statFours} / ${metricData.statSixes}", "💥 Boundaries")
                                    )
                                )
                            }
                            roleLower.contains("bowl") -> {
                                StatsGrid(
                                    stats = listOf(
                                        StatItem("Matches", metricData.matches.toString(), "🏟️ Matches played"),
                                        StatItem("Wickets", metricData.statWickets.toString(), "🏏 Bowling wickets"),
                                        StatItem("Avg / SR", "${String.format("%.2f", metricData.statAvg)} / ${String.format("%.1f", metricData.statSR)}", "📈 Averages"),
                                        StatItem("Economy Rate", String.format("%.2f", metricData.statEcon), "⚡ Economy"),
                                        StatItem("Best Bowling", metricData.statBB, "🔝 Best figures"),
                                        StatItem("Catches Filed", metricData.statCatches.toString(), "🖐️ Safe hands")
                                    )
                                )
                            }
                            roleLower.contains("round") -> {
                                StatsGrid(
                                    stats = listOf(
                                        StatItem("Matches", metricData.matches.toString(), "🏟️ Matches played"),
                                        StatItem("Total Runs", metricData.statRuns.toString(), "🏏 Runs scored"),
                                        StatItem("Wickets Took", metricData.statWickets.toString(), "⚡ Wickets taken"),
                                        StatItem("Batting SR", String.format("%.1f", metricData.statSR), "📈 Explisive rate"),
                                        StatItem("Bowling Econ", String.format("%.2f", metricData.statEcon), "🛡️ Bowling control"),
                                        StatItem("Catches / HS", "${metricData.statCatches} / ${metricData.statHS}", "💥 Overview")
                                    )
                                )
                            }
                            else -> { // Wicketkeeper
                                StatsGrid(
                                    stats = listOf(
                                        StatItem("Matches", metricData.matches.toString(), "🏟️ Matches played"),
                                        StatItem("Total Runs", metricData.statRuns.toString(), "🏏 Runs scored"),
                                        StatItem("Dismissals", metricData.statDismissals.toString(), "🧤 Catches & Stumpings"),
                                        StatItem("Batting Avg", String.format("%.2f", metricData.statAvg), "📈 Average"),
                                        StatItem("Dismiss/Match", String.format("%.1f", metricData.statDismissals.toDouble() / metricData.matches.toDouble()), "🛡️ Wicketkeeper rate"),
                                        StatItem("High Score", metricData.statHS, "🔝 Top score")
                                    )
                                )
                            }
                        }
                    }
                }

                // 4. Eligibility Tracker Module
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyCardBlue, RoundedCornerShape(16.dp))
                            .border(0.5.dp, GoldBright.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                            .testTag("eligibility_status_card")
                    ) {
                        Text(
                            text = "📜 TOURNAMENT ELIGIBILITY CHECKS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldBright
                        )
                        Text(
                            text = "Mandatory administrative audits required for entering Season 1 Draft.",
                            fontSize = 10.sp,
                            color = SlateTextMuted,
                            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                        )

                        // 4 parameters: Age, School Code, Registration Fee, Scouting Score
                        EligibilityItem(
                            label = "Age Board Verification (${player.ageCategory})",
                            isValid = metricData.ageVerified,
                            description = "Authenticated DOB: ${player.dob}. Valid for ${player.ageCategory} category limit."
                        )

                        EligibilityItem(
                            label = "Registered School Board Code Access",
                            isValid = metricData.schoolVerified,
                            description = "Affiliated with active institution '${player.school}'."
                        )

                        EligibilityItem(
                            label = "Trial Entry Registration Fee Payment",
                            isValid = metricData.feePaid,
                            description = if (metricData.feePaid) "Payment synced. Transaction reference: ISPL-PAY-SR" else "Administrative fee payment is pending checkout."
                        )

                        EligibilityItem(
                            label = "Board Scouting Qualification Score (>= 60%)",
                            isValid = metricData.scoutApproved,
                            description = if (metricData.scoutApproved) {
                                "Scouting trials score: ${String.format("%.1f", metricData.scoutingAverage)}%. Exceeds admission benchmark."
                            } else {
                                "Trial score currently pending. Attend or book your scouting schedule below."
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = SlateTextMuted.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Dynamic Banner showing state
                        val passesAll = metricData.isEligible
                        val bannerColor = if (passesAll) EmeraldStatus else AmberStatus
                        val bannerTitle = if (passesAll) "🟢 APPROVED - ELIGIBLE FOR DRAFT" else "🟡 ACTION REQUIRED - REGISTRATION INCOMPLETE"
                        val bannerDesc = if (passesAll) {
                            "Congratulations! ${player.name} meets all requirements. Registered under Scout ID ISPL-S1-${1982 + player.id}. The player has been added to the regional auction database."
                        } else {
                            "Needs actions: Complete fee payment or trigger trial session to populate official scouting metrics on your profile."
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bannerColor.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .border(1.dp, bannerColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = bannerTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = bannerColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = bannerDesc,
                                    fontSize = 10.sp,
                                    color = SlateTextLight,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        // Interactive Simulation Action Layout
                        if (!passesAll) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (isSimulatingTrial) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator(color = GoldBright, modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = simulationStep,
                                            fontSize = 11.sp,
                                            color = SlateTextLight,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 1. Pay Fee Button if pending
                                    if (player.status == "New" || player.status == "Under Review") {
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    isPayingFee = true
                                                    Toast.makeText(context, "Processing registration payment online...", Toast.LENGTH_SHORT).show()
                                                    delay(1000)
                                                    viewModel.updatePlayerStatus(player.id, "Contacted")
                                                    Toast.makeText(context, "Registration fee paid with reference ID!", Toast.LENGTH_LONG).show()
                                                    isPayingFee = false
                                                }
                                            },
                                            enabled = !isPayingFee,
                                            colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("pay_fee_btn")
                                        ) {
                                            if (isPayingFee) {
                                                CircularProgressIndicator(color = NavyDeepDusk, modifier = Modifier.size(16.dp))
                                            } else {
                                                Text("💳 Pay Trial Fee", fontSize = 11.sp, color = NavyDeepDusk, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    // 2. Perform Trial simulation
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                isSimulatingTrial = true
                                                simulationStep = "Calibrating bowling machine / bowling speed..."
                                                delay(850)
                                                simulationStep = "Registering high-speed video batting timing..."
                                                delay(850)
                                                simulationStep = "Calculating Yo-Yo stamina test thresholds..."
                                                delay(850)
                                                simulationStep = "Generating scouting grade score cards..."
                                                delay(700)
                                                viewModel.updatePlayerStatus(player.id, "Approved")
                                                Toast.makeText(context, "${player.name} finished trials. Profile Approved & Draft Registered!", Toast.LENGTH_LONG).show()
                                                isSimulatingTrial = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldStatus),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("simulate_trial_btn")
                                    ) {
                                        Text("🏏 Attend Trial Run", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// --- SUB-ELEMENT COMPOSABLES ---

@Composable
fun MetricGaugeRow(
    label: String,
    score: Float,
    scoreText: String,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 9.sp,
                color = SlateTextLight,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = scoreText,
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = score,
            color = color,
            trackColor = NavyDeepDusk,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun StatsGrid(stats: List<StatItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Lay out stats in 2-column rows
        for (i in stats.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // First Stat Item
                StatCard(item = stats[i], modifier = Modifier.weight(1f))

                // Second Stat Item
                if (i + 1 < stats.size) {
                    StatCard(item = stats[i + 1], modifier = Modifier.weight(1f))
                } else {
                    Box(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}

@Composable
fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = modifier.border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.label.uppercase(),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GoldBright
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.sub,
                fontSize = 8.sp,
                color = SlateTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EligibilityItem(
    label: String,
    isValid: Boolean,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = if (isValid) "Qualified" else "Pending",
            tint = if (isValid) EmeraldStatus else AmberStatus,
            modifier = Modifier.size(16.dp).padding(top = 1.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextLight
            )
            Text(
                text = description,
                fontSize = 9.sp,
                color = SlateTextMuted,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun BadgeChip(
    text: String,
    color: Color,
    bgColor: Color
) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

// --- DATA TYPES & SEEDED ALGORITHMS ---

data class StatItem(
    val label: String,
    val value: String,
    val sub: String
)

data class ScoutMetrics(
    val scoutTimingPower: Int,
    val scoutPaceAccuracy: Int,
    val scoutFieldingAgility: Int,
    val scoutYoYoScore: Double,
    val scoutMentalToughness: Int,
    val remarks: String,
    val matches: Int,
    val statRuns: Int,
    val statAvg: Double,
    val statSR: Double,
    val statHS: String,
    val statFours: Int,
    val statSixes: Int,
    val statWickets: Int,
    val statEcon: Double,
    val statBB: String,
    val statCatches: Int,
    val statDismissals: Int,
    val ageVerified: Boolean,
    val schoolVerified: Boolean,
    val feePaid: Boolean,
    val scoutApproved: Boolean,
    val scoutingAverage: Double,
    val isEligible: Boolean
)

// Generate consistent data based on player configuration
fun getScoutingMetricsForPlayer(player: Player): ScoutMetrics {
    // Generate deterministic values based on player's name characters as seed
    val seed = java.lang.Math.abs(player.name.hashCode())
    
    val matches = 8 + (seed % 8)
    val runs = 120 + (seed % 340)
    val highVal = 40 + (seed % 65)
    val wickets = 6 + (seed % 18)
    val catches = 3 + (seed % 10)
    val stavg = 15.0 + (seed % 200) / 10.0
    val stsr = 110.0 + (seed % 500) / 10.0
    val econ = 4.5 + (seed % 35) / 10.0
    val bestRuns = 10 + (seed % 20)
    val bestWickets = 3 + (seed % 3)

    // Derived Scouting Metrics
    val timingPower = 60 + (seed % 35)
    val accuracyGlove = 58 + (seed % 38)
    val fieldingAgility = 62 + (seed % 33)
    val yoyoScore = 15.2 + (seed % 42) / 10.0
    val mentalToughness = 65 + (seed % 30)

    val averageScoutMetric = (timingPower + accuracyGlove + fieldingAgility + mentalToughness) / 4.0

    // Eligibility check mapping
    val isApproved = player.status == "Approved"
    val isContactedOrApproved = player.status == "Approved" || player.status == "Contacted"

    val ageVerified = player.dob.isNotEmpty() // True if valid dob given
    val schoolVerified = player.school.isNotEmpty() && !player.school.contains("Untitled", ignoreCase = true)
    val feePaid = isContactedOrApproved // Contacted means fee is processed, Approved means complete
    val scoutApproved = isApproved && averageScoutMetric >= 60.0

    // Remarks corresponding to role & metrics
    val roleStr = player.playingRole.lowercase()
    val playRemarks = when {
        roleStr.contains("bats") -> {
            if (timingPower > 80) "Explosive lower-half stance. High bat speed with impeccable lofted timing on wrist dials."
            else "Balanced stance. Proficient square cut scorer, but needs minor footwork adjustments against moving seam."
        }
        roleStr.contains("bowl") -> {
            if (accuracyGlove > 80) "Smooth runup with late outswing release. Deadly Yorker pinpoint placement verified."
            else "Aggressive bowler. Good bounce length, but tends to drift down leg side under pressure."
        }
        roleStr.contains("round") -> {
            "Exceptional double utility asset. Covers standard death overs Bowling with quick middle order acceleration."
        }
        else -> { // Wicketkeeper
            "Slight reactive delay on leg-side takes, but lightning quick stumpings and highly vocal infield lead."
        }
    }

    return ScoutMetrics(
        scoutTimingPower = timingPower,
        scoutPaceAccuracy = accuracyGlove,
        scoutFieldingAgility = fieldingAgility,
        scoutYoYoScore = yoyoScore,
        scoutMentalToughness = mentalToughness,
        remarks = playRemarks,
        matches = matches,
        statRuns = runs,
        statAvg = stavg,
        statSR = stsr,
        statHS = "$highVal*",
        statFours = 10 + (runs / 20),
        statSixes = 2 + (runs / 40),
        statWickets = wickets,
        statEcon = econ,
        statBB = "$bestWickets/$bestRuns",
        statCatches = catches,
        statDismissals = catches + (seed % 6),
        ageVerified = ageVerified,
        schoolVerified = schoolVerified,
        feePaid = feePaid,
        scoutApproved = scoutApproved,
        scoutingAverage = averageScoutMetric,
        isEligible = ageVerified && schoolVerified && feePaid && scoutApproved
    )
}

fun getEmojiForRole(role: String): String {
    return when {
        role.lowercase().contains("bats") -> "🏏"
        role.lowercase().contains("bowl") -> "🥎"
        role.lowercase().contains("keeper") -> "🧤"
        else -> "⭐"
    }
}

fun getProfileEmojiById(id: Int): String {
    return when (id % 6) {
        0 -> "🦁"
        1 -> "⚡"
        2 -> "🦅"
        3 -> "🔥"
        4 -> "🐆"
        else -> "🛡️"
    }
}
