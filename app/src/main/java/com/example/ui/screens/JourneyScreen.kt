package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun JourneyScreen() {
    var selectedStep by remember { mutableStateOf(1) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeepDusk)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- SECTION HEADER ---
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "THE S1 ROADMAP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldBright,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Player Ecosystem & Structured Pathway",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextLight,
                    textAlign = TextAlign.Center
                )
            }
        }

        // --- THE CHALLENGE PANEL ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CrimsonStatus.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(CrimsonStatus.copy(alpha = 0.15f), CircleShape)
                                .size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚠️", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "THE SPORTING CHALLENGE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonStatus
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Millions of highly talented school cricketers in India never make it to the state academies due to key grassroot issues:",
                        fontSize = 13.sp,
                        color = SlateTextLight,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BulletPoint(text = "❌ Lack of structured, bias-free trials and scouts")
                    BulletPoint(text = "❌ Zero national exposure or live-broadcast options")
                    BulletPoint(text = "❌ No professional digital statistics profiles")
                    BulletPoint(text = "❌ Financial entry-barriers to high-performance academies")
                }
            }
        }

        // --- THE SOLUTION PANEL ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, EmeraldStatus.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(EmeraldStatus.copy(alpha = 0.15f), CircleShape)
                                .size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💡", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "THE ISPL CORE SOLUTION",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldStatus
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ISPL provides a fully coordinated, tech-backed digital solution pipeline connecting street cricket to franchises:",
                        fontSize = 13.sp,
                        color = SlateTextLight,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PathwayFlow()
                }
            }
        }

        // --- INTERACTIVE SELECTOR STEPPER HEADER ---
        item {
            Text(
                text = "🎯 INTERACTIVE PLAYER PIPELINE (Tap Steps to Explore)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldBright,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        // Stepper numbers row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (step in 1..7) {
                    val isActive = selectedStep == step
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (isActive) GoldBright else NavyCardBlue,
                                CircleShape
                            )
                            .border(1.dp, if (isActive) GoldBright else SlateTextMuted.copy(alpha = 0.4f), CircleShape)
                            .clickable { selectedStep = step },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$step",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isActive) NavyDeepDusk else SlateTextLight
                        )
                    }
                }
            }
        }

        // ACTIVE STEP DETAIL CARD
        item {
            AnimatedContent(
                targetState = selectedStep,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "stepDetailAnimation"
            ) { step ->
                StepDetailCard(step = step)
            }
        }

        // --- ROADMAP PERSISTENCY ---
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎖️ HIGH PERFORMANCE MEASURABLES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldBright,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Every trial candidate evaluates under BCCI-certified trainers using smart bowling speedometers, AI-integrated ball tracking cameras, and professional fitness monitors. Results are certified directly on individual profile sheets.",
                        fontSize = 12.sp,
                        color = SlateTextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun StepDetailCard(step: Int) {
    val stepTitle = when (step) {
        1 -> "Player Registration"
        2 -> "District Trials & Audits"
        3 -> "District Championships"
        4 -> "Digital Player Profiles"
        5 -> "Franchise Scouting Desk"
        6 -> "Live ISPL Player Auction"
        7 -> "National Premier League"
        else -> ""
    }

    val stepSubtitle = when (step) {
        1 -> "Step 1: Onboarding Ecosystem"
        2 -> "Step 2: Grassroots Level Audits"
        3 -> "Step 3: State Level Tournaments"
        4 -> "Step 4: Real-time Stats Registry"
        5 -> "Step 5: Professional Evaluation"
        6 -> "Step 6: Premium Franchise Bidding"
        7 -> "Step 7: Stadium Live Stage"
        else -> ""
    }

    val stepDesc = when (step) {
        1 -> "Players enroll online for ₹599. They write school credentials, submit verification IDs, and declare their playing skills. Instantly logs onto the local database."
        2 -> "Every applicant is invited to physical district trials. Verified coaches systematically audit specific talent dimensions."
        3 -> "Best trials players are assembled into 16 district teams. Compete in competitive league-stages, aired live locally."
        4 -> "District matches stats are uploaded to create digital IDs with pictures, strike rates, school rankings, and trial indexes."
        5 -> "Owners of state franchises review profiles to shortlist talent. System offers detailed filters by role, average, and state."
        6 -> "Franchises gather for the ISPL Player Auction where they bid for players with virtual salaries starting at ₹50,000 up to ₹5,00,000 per talent!"
        7 -> "Selected school champions fly out to complete the grand season under floodlights, covered live on national platforms!"
        else -> ""
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GoldBright.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stepSubtitle.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldBright,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stepTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SlateTextLight
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stepDesc,
                fontSize = 13.sp,
                color = SlateTextLight,
                lineHeight = 18.sp
            )

            if (step == 2) {
                // EXPLICIT DRILL CHECKLISTS FOR STEP 2 AS REQUESTED
                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = SlateTextMuted.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "📈 TRIAL CORE ASSESSMENT CATEGORIES:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldBright
                )
                Spacer(modifier = Modifier.height(6.dp))
                Step2DrillItem(name = "Batting Skills", desc = "Stance, stroke control, power hitting index")
                Step2DrillItem(name = "Bowling Drills", desc = "Speedometer speed (km/h), accuracy, spin degree, variations")
                Step2DrillItem(name = "Fielding Drills", desc = "30m sprint throw, direct hits accuracy, catching efficiency")
                Step2DrillItem(name = "Physical Fitness", desc = "Yo-Yo endurance test, core strength indexes")
                Step2DrillItem(name = "Game Awareness", desc = "Tactical match-situation decision making")
            }

            if (step == 7) {
                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = SlateTextMuted.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "🏆 CHAMPIONSHIP STRUCTURE:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldBright
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🟢 1. Group Stage", fontSize = 12.sp, color = SlateTextLight)
                    Text("🔵 2. Quarter Finals", fontSize = 12.sp, color = SlateTextLight)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🟡 3. Semi Finals", fontSize = 12.sp, color = SlateTextLight)
                    Text("👑 4. Grand Final", fontSize = 12.sp, color = GoldBright, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun Step2DrillItem(name: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = GoldBright,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
            Text(text = desc, fontSize = 10.sp, color = SlateTextMuted)
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = SlateTextLight,
        modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp),
        lineHeight = 16.sp
    )
}

@Composable
fun PathwayFlow() {
    Column {
        val flowSteps = listOf(
            "School Registration Hub",
            "Player Profile Log",
            "District Trials Arena",
            "District Championships",
            "Dynamic Stats Collection",
            "Scouters Desk Bid",
            "Premium Player Auction",
            "Grand Premier League"
        )
        flowSteps.forEachIndexed { idx, stepName ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(GoldBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${idx + 1}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDeepDusk
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stepName,
                    fontSize = 12.sp,
                    color = SlateTextLight,
                    fontWeight = FontWeight.Medium
                )
            }
            if (idx < flowSteps.size - 1) {
                VerticalLineDivider()
            }
        }
    }
}

@Composable
fun VerticalLineDivider() {
    Box(
        modifier = Modifier
            .padding(start = 9.dp)
            .height(10.dp)
            .width(2.dp)
            .background(GoldBright.copy(alpha = 0.4f))
    )
}
