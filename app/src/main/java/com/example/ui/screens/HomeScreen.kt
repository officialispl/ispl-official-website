package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import android.content.Intent
import android.net.Uri


@Composable
fun HomeScreen(
    viewModel: ISPLViewModel,
    onNavigateToRegistration: (String) -> Unit
) {
    val countdown by viewModel.countdownState.collectAsState()
    val isLiveOverride by viewModel.forceLiveState.collectAsState()
    val liveMatch by viewModel.liveMatch.collectAsState()
    
    val playersList by viewModel.players.collectAsState()
    val schoolsList by viewModel.schools.collectAsState()
    val franchisesList by viewModel.franchises.collectAsState()
    val sponsorsList by viewModel.sponsors.collectAsState()

    val totalPlayers = playersList.size + 1420
    val totalSchools = schoolsList.size + 185
    val totalDistricts = 420
    val totalStates = 29
    val totalFranchises = franchisesList.size + 8
    val totalSponsors = sponsorsList.size + 12

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeepDusk)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- STADIUM DRAWING HEADER ---
        item {
            StadiumLightsHeader()
        }

        // --- HIGH DENSITY THEME HEADER BAR ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(GoldBright, RoundedCornerShape(8.dp))
                            .border(1.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "I",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = NavyDeepDusk
                        )
                    }
                    Column {
                        Text(
                            text = "ISPL",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 13.sp,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "SCHOOL PREMIER LEAGUE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = GoldBright,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(50.dp))
                        .border(0.5.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "● LIVE REGISTRATION",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldBright
                    )
                }
            }
        }

        // --- HERO BRAND SECTION ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NavyCardBlue, NavyDeepDusk)
                        )
                    ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "INDIA'S BIGGEST",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    lineHeight = 32.sp,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "SCHOOL CRICKET",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = GoldBright,
                    lineHeight = 32.sp,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "PLATFORM",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    lineHeight = 32.sp,
                    letterSpacing = (-1).sp
                )

                Text(
                    text = "\"यत्र प्रतिभा तत्र विजयः\"",
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Prize Pool Card
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "PRIZE POOL",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "₹1 CRORE+",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldBright
                        )
                    }

                    // Categories Card
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "CATEGORIES",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "U14 | U16 | U19",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // --- STADIUM & TROPHY DECORATIVE ROW ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyCardBlue, RoundedCornerShape(12.dp))
                    .border(0.5.dp, SlateTextMuted.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Trophy visual icon using canvas and geometric shapes
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(GoldBright.copy(alpha = 0.1f), CircleShape)
                        .border(1.dp, GoldBright.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Trophy",
                        tint = GoldBright,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ISPL National Golden Trophy",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextLight
                    )
                    Text(
                        text = "Scouts from IPL & Ranji Academies actively tracking talent profiles",
                        fontSize = 12.sp,
                        color = SlateTextMuted
                    )
                }
            }
        }

        // --- EVENT COUNTDOWN PANEL ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyCardBlue, RoundedCornerShape(16.dp))
                    .border(
                        if (countdown.isFinished) 2.dp else 1.dp,
                        if (countdown.isFinished) GoldBright else NavyLightBlue,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (countdown.isFinished) "🚀 THE STAGE IS SET!" else "⏳ ISPL SEASON 1 BEGINS IN",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldBright,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (!countdown.isFinished) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CountdownUnit(value = countdown.days, label = "Days")
                        CountdownColon()
                        CountdownUnit(value = countdown.hours, label = "Hrs")
                        CountdownColon()
                        CountdownUnit(value = countdown.minutes, label = "Mins")
                        CountdownColon()
                        CountdownUnit(value = countdown.seconds, label = "Secs")
                    }
                } else {
                    // LIVE CELEBRATION EFFECTS
                    LiveConfettiBanner()
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats below timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BoxWithLabel(topText = "🏏 U14 | U16 | U19", bottomText = "Categories")
                    BoxWithLabel(topText = "🏆 ₹1 Crore+", bottomText = "Prize Pool")
                    BoxWithLabel(topText = "🌏 Pan India", bottomText = "Tournament")
                    BoxWithLabel(topText = "🎯 T20 Format", bottomText = "Official Rule")
                }

                // Interactive Simulator Toggle
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = SlateTextMuted.copy(alpha = 0.2f), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyDeepDusk, RoundedCornerShape(8.dp))
                        .clickable { viewModel.toggleForceLiveState() }
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Interactive Dev Preview Panel",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldBright
                        )
                        Text(
                            text = if (isLiveOverride) "Double-tap to restore timer" else "Simulate Live League & Scores!",
                            fontSize = 10.sp,
                            color = SlateTextMuted
                        )
                    }
                    Switch(
                        checked = isLiveOverride,
                        onCheckedChange = { viewModel.toggleForceLiveState() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GoldBright,
                            checkedTrackColor = NavyLightBlue
                        )
                    )
                }
            }
        }

        // --- SIMULATED LIVE MATCH SCORE PANEL ---
        if (countdown.isFinished) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(NavyCardBlue, Color(0xFF07142E))
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .border(1.5.dp, GoldBright, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color.Red, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LIVE SCORING DESK",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextLight
                            )
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = GoldBright),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "SEASON 1 MATCH 4",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = NavyDeepDusk,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${liveMatch.teamA} vs ${liveMatch.teamB}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldBright
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "${liveMatch.battingTeam}  ${liveMatch.runs}/${liveMatch.wickets}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = SlateTextLight
                            )
                            Text(
                                text = "Overs: ${liveMatch.overs}.${liveMatch.balls}",
                                fontSize = 14.sp,
                                color = SlateTextMuted
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Target: 147 runs",
                                fontSize = 12.sp,
                                color = SlateTextMuted
                            )
                            Text(
                                text = liveMatch.highlights,
                                fontSize = 11.sp,
                                color = GoldDust,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyDeepDusk, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "🎤 COMMENTARY: \"${liveMatch.commentary}\"",
                            fontSize = 12.sp,
                            color = SlateTextLight,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }

        // --- REGISTRATION ENTRANCE (PROMPT CARD) ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NavyCardBlue, NavyDeepDusk),
                            radius = 400f
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(2.dp, GoldBright.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(GoldBright, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⚡ OPEN FOR REGISTRATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = NavyDeepDusk
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Secure Your Direct Trials & Profile Draft",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextLight,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Official Match Fee: ₹599 (Includes customized training kit, trial caps & profile logging)",
                    fontSize = 12.sp,
                    color = SlateTextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Registration Grid Shortcuts to Form Tabs
                RegistrationGrid(onNavigateToRegistration)
            }
        }

        // --- REAL-TIME LIVE DATABASE STATS ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyCardBlue, RoundedCornerShape(12.dp))
                    .border(0.5.dp, SlateTextMuted.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "📊 ISPL CORE REAL-TIME METRICS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldBright,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatMetric(icon = "🏏", value = "$totalPlayers", label = "Registered Players")
                    StatMetric(icon = "🏫", value = "$totalSchools", label = "Registered Schools")
                    StatMetric(icon = "📍", value = "$totalDistricts", label = "Districts Covered")
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatMetric(icon = "🇮🇳", value = "$totalStates", label = "States Engaged")
                    StatMetric(icon = "🏢", value = "$totalFranchises", label = "State Franchises")
                    StatMetric(icon = "🤝", value = "$totalSponsors", label = "Sponsors Locked")
                }
            }
        }

        // --- PREMIER SPONSORS ACCENT BAND ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sponsor Alliance Network",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextMuted,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TATA", color = SlateTextMuted, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontStyle = FontStyle.Italic)
                    Text("Reliance JIO", color = SlateTextMuted, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("MRF", color = SlateTextMuted, fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 2.sp)
                    Text("CEAT", color = SlateTextMuted, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontStyle = FontStyle.Italic)
                }
            }
        }

        item {
            SocialConnectFooter()
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun StadiumLightsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(0xFF02091A), RoundedCornerShape(16.dp))
            .border(0.5.dp, GoldBright.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
    ) {
        // High fidelity custom Canvas drawing illustrating stadium floodlight overlays & glowing rays
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            // Stadium grass gradient baseline
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0xFF042111)),
                    startY = height * 0.7f,
                    endY = height
                )
            )

            // Dynamic floodlight points
            val leftLightCenter = Offset(width * 0.2f, height * 0.2f)
            val rightLightCenter = Offset(width * 0.8f, height * 0.2f)

            // Floodlight structures
            drawCircle(color = Color(0xFF1E3A8A), radius = 16f, center = leftLightCenter)
            drawCircle(color = Color(0xFF1E3A8A), radius = 16f, center = rightLightCenter)

            // Floodlight golden glowing cone
            drawCircle(color = GoldBright.copy(alpha = 0.6f), radius = 11f, center = leftLightCenter)
            drawCircle(color = GoldBright.copy(alpha = 0.6f), radius = 11f, center = rightLightCenter)

            // Diagonal Light rays
            drawLine(
                color = GoldBright.copy(alpha = 0.15f),
                start = leftLightCenter,
                end = Offset(width * 0.5f, height),
                strokeWidth = 32f
            )
            drawLine(
                color = GoldBright.copy(alpha = 0.15f),
                start = rightLightCenter,
                end = Offset(width * 0.5f, height),
                strokeWidth = 32f
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🇮🇳",
                fontSize = 24.sp
            )
            Text(
                text = "ISPL STADIUM ARENA",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldBright,
                letterSpacing = 2.sp
            )
            Text(
                text = "Season 1 Scouting Championship Draft",
                fontSize = 12.sp,
                color = SlateTextLight
            )
        }
    }
}

@Composable
fun CountdownUnit(value: Long, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(NavyDeepDusk, RoundedCornerShape(8.dp))
            .border(0.5.dp, GoldBright.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .width(60.dp)
            .padding(vertical = 8.dp)
    ) {
        val displayVal = String.format("%02d", value)
        Text(
            text = displayVal,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = GoldBright
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = SlateTextMuted,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CountdownColon() {
    Text(
        text = ":",
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        color = GoldBright
    )
}

@Composable
fun BoxWithLabel(topText: String, bottomText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = topText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextLight
        )
        Text(
            text = bottomText,
            fontSize = 10.sp,
            color = SlateTextMuted
        )
    }
}

@Composable
fun LiveConfettiBanner() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(GoldBright.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .border(1.dp, GoldBright, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = GoldBright,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(rotationZ = rotation)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "⚡ ISPL SEASON 1 IS NOW LIVE! ⚡",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = GoldBright
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = GoldBright,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(rotationZ = -rotation)
            )
        }
    }
}

@Composable
fun RegistrationGrid(onNavigate: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Player Registration Action Card (Gold design matching primary CTA)
            Button(
                onClick = { onNavigate("Player") },
                colors = ButtonDefaults.buttonColors(containerColor = GoldBright),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = NavyDeepDusk, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PLAYER ENTRY",
                            fontSize = 11.sp,
                            color = NavyDeepDusk,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "FEE: ₹599",
                        fontSize = 8.sp,
                        color = NavyDeepDusk.copy(alpha = 0.75f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // School Portal Action Card (Translucent design)
            OutlinedButton(
                onClick = { onNavigate("School") },
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.20f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White.copy(alpha = 0.1f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SCHOOL ENTRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // State Franchise Business Card (Transparent design)
            OutlinedButton(
                onClick = { onNavigate("Franchise") },
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White.copy(alpha = 0.05f),
                    contentColor = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "FRANCHISE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Sponsorship Business Card (Transparent design)
            OutlinedButton(
                onClick = { onNavigate("Sponsor") },
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White.copy(alpha = 0.05f),
                    contentColor = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SPONSORSHIP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatMetric(icon: String, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = GoldBright
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = SlateTextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp
        )
    }
}

@Composable
fun SocialConnectFooter() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyCardBlue, RoundedCornerShape(16.dp))
            .border(1.dp, GoldBright.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Section Title
        Text(
            text = "🤝 CONNECT & SUPPORT HELPLINE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            letterSpacing = 1.sp
        )
        Text(
            text = "Have questions about trials, registration fee payment, or school codes? Get in touch with our live desk or follow official feeds.",
            fontSize = 11.sp,
            color = SlateTextMuted,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            lineHeight = 15.sp
        )

        // Official Website Card
        Card(
            colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldBright, RoundedCornerShape(12.dp))
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://indianschoolcricketleague.com/"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Opening website: https://indianschoolcricketleague.com/", Toast.LENGTH_SHORT).show()
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("🌐", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "OFFICIAL ISPL WEBSITE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldBright,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "indianschoolcricketleague.com",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextLight
                        )
                    }
                }
                Text(
                    text = "VISIT ➔",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldBright
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Helpline and Email Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // WhatsApp Support Card
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0xFF25D366).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable {
                        try {
                            clipboardManager.setText(AnnotatedString("+91 91522 34455"))
                            Toast.makeText(context, "Helpline copied to clipboard!", Toast.LENGTH_SHORT).show()
                            
                            val uri = Uri.parse("https://api.whatsapp.com/send?phone=919152234455&text=Hello%20ISPL%20Support%2C%20I%20have%20a%20query%20about%20the%20scouting%20trials.")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Opening browser support link...", Toast.LENGTH_SHORT).show()
                        }
                    }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("💬", fontSize = 16.sp)
                        Text(
                            text = "WHATSAPP HELPDESK",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF25D366)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "+91 91522 34455",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextLight
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Response: Minutes (10 AM - 7 PM)",
                        fontSize = 9.sp,
                        color = SlateTextMuted
                    )
                }
            }

            // Email Support Card
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDeepDusk),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable {
                        try {
                            clipboardManager.setText(AnnotatedString("support@isplcricket.in"))
                            Toast.makeText(context, "Email copied to clipboard!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:support@isplcricket.in")
                                putExtra(Intent.EXTRA_SUBJECT, "ISPL Season 1 Inquiry")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No email app found. Copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("✉️", fontSize = 16.sp)
                        Text(
                            text = "EMAIL SUPPORT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldBright
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "support@isplcricket.in",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextLight
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Response: Under 4 Hours",
                        fontSize = 9.sp,
                        color = SlateTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = SlateTextMuted.copy(alpha = 0.15f), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Social Media Row
        Text(
            text = "📱 OFFICIAL SOCIAL CHANNELS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = SlateTextMuted,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialChip(
                platform = "Instagram",
                handle = "@OfficialISPL",
                icon = "📸",
                accentColor = Color(0xFFE1306C),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/OfficialISPL"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Instagram handle: @OfficialISPL", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )

            SocialChip(
                platform = "Facebook",
                handle = "Indian School Premier League",
                icon = "👥",
                accentColor = Color(0xFF1877F2),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/IndianSchoolPremierLeague"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Facebook: Indian School Premier League", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialChip(
                platform = "Twitter / X",
                handle = "@OfficialISPL",
                icon = "🐦",
                accentColor = Color(0xFF1DA1F2),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/OfficialISPL"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Twitter: @OfficialISPL", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )

            SocialChip(
                platform = "YouTube",
                handle = "@OfficialISPL",
                icon = "▶️",
                accentColor = Color(0xFFFF0000),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/@OfficialISPL"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "YouTube: @OfficialISPL", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialChip(
                platform = "WhatsApp Channel",
                handle = "ISPL Official",
                icon = "🟢",
                accentColor = Color(0xFF25D366),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://whatsapp.com/channel/ISPLOfficial"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "WhatsApp Channel: ISPL Official", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )

            SocialChip(
                platform = "Telegram",
                handle = "ISPL Official",
                icon = "📢",
                accentColor = Color(0xFF0088CC),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/ispl_official"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Telegram: ISPL Official", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        
        // Security Banner inside custom card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3B1E1E), RoundedCornerShape(8.dp))
                .border(0.5.dp, Color(0xFFEF4444).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠️", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SECURITY WARNING: ISPL administration never requests OTPs, banking logins, or payment outside this verified app. Avoid fraudulent trial agents.",
                    fontSize = 9.sp,
                    color = Color(0xFFFCA5A5),
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SocialChip(
    platform: String,
    handle: String,
    icon: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(NavyDeepDusk, RoundedCornerShape(8.dp))
            .border(0.5.dp, SlateTextMuted.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(icon, fontSize = 12.sp)
            Column {
                Text(
                    text = platform,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = handle,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = SlateTextLight,
                    maxLines = 1
                )
            }
        }
    }
}

