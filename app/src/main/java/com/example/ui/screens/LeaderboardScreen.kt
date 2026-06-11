package com.example.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LeaderboardScreen() {
    var selectedTab by remember { mutableStateOf("Rankings") } // Rankings, Media, Sponsors, Revenue

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeepDusk)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- MODULE HEADER ---
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ISPL LEAGUE DESK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldBright,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Rankings, Sponsors & Board Hub",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextLight,
                    textAlign = TextAlign.Center
                )
            }
        }

        // --- SUB-TABS INTERACTIVE NAV BAR ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyCardBlue, RoundedCornerShape(8.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val tabs = listOf("Rankings", "Media", "Sponsors", "Revenue")
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) GoldBright else Color.Transparent, RoundedCornerShape(6.dp))
                            .clickable { selectedTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NavyDeepDusk else SlateTextMuted
                        )
                    }
                }
            }
        }

        // --- DYNAMIC RENDERING ---
        when (selectedTab) {
            "Rankings" -> {
                // Players & schools rankings
                item { PlayerStatsLeaderboardSection() }
                item { SchoolRankingsSection() }
            }
            "Media" -> {
                // News, match highlights
                item { LiveNewsSection() }
                item { MediaGallerySection() }
            }
            "Sponsors" -> {
                // Sponsorship tiers as requested
                item { SponsorshipTiersSection() }
            }
            "Revenue" -> {
                // Revenue model & corporate Vision 2030
                item { RevenueFrameworkSection() }
                item { VisionMissionBlock() }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// --- TAB SUB-SECTIONS ---

@Composable
fun PlayerStatsLeaderboardSection() {
    Column {
        Text(
            text = "🏆 INDEPENDENT PLAYER LEADERBOARD (U16/U19)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                LeaderboardRow(rank = "1", name = "Ishaan Sharma", school = "DPS R.K. Puram", spec = "312 Runs (Avg: 62.4)", icon = "🏏")
                LeaderboardRow(rank = "2", name = "Siddharth Sen", school = "Don Bosco School", spec = "14 Wickets (Econ: 4.2)", icon = "⚡")
                LeaderboardRow(rank = "3", name = "Arjun Tendulkar Jr.", school = "Cathedral Mumbai", spec = "196 Runs / 11 Wickets", icon = "⭐")
                LeaderboardRow(rank = "4", name = "Yuvraj Singh Jr.", school = "DAV Chandigarh", spec = "284 Runs (SR: 165.2)", icon = "🏏")
                LeaderboardRow(rank = "5", name = "Kedar Jadhav Jr.", school = "St. Joseph Pune", spec = "18 Dismissals (9 Catches)", icon = "🧤")
            }
        }
    }
}

@Composable
fun LeaderboardRow(rank: String, name: String, school: String, spec: String, icon: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    when(rank) {
                        "1" -> GoldBright
                        "2" -> Color(0xFFC0C0C0)
                        "3" -> Color(0xFFCD7F32)
                        else -> NavyDeepDusk
                    }, CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (rank == "1" || rank == "2" || rank == "3") NavyDeepDusk else SlateTextLight
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
            Text(text = school, fontSize = 10.sp, color = SlateTextMuted)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = spec, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldBright)
            Text(text = icon, fontSize = 12.sp)
        }
    }
}

@Composable
fun SchoolRankingsSection() {
    Column {
        Text(
            text = "🏫 TOP REGISTERED CRICKET SCHOOLS (PAN INDIA)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                SchoolRankRow(rank = "1", name = "Delhi Public School", city = "New Delhi", points = "1,420 pts", form = "🏆 🏆 🥇")
                SchoolRankRow(rank = "2", name = "Cathedral & John Connon", city = "Mumbai", points = "1,290 pts", form = "🏆 🥇 🥈")
                SchoolRankRow(rank = "3", name = "La Martiniere College", city = "Lucknow", points = "1,110 pts", form = "🥇 🥈 🥉")
                SchoolRankRow(rank = "4", name = "St. Xavier's International", city = "Pune", points = "980 pts", form = "🥈 🥈")
            }
        }
    }
}

@Composable
fun SchoolRankRow(rank: String, name: String, city: String, points: String, form: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$rank",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = GoldBright,
            modifier = Modifier.width(30.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
            Text(text = city, fontSize = 10.sp, color = SlateTextMuted)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = points, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
            Text(text = form, fontSize = 10.sp)
        }
    }
}

@Composable
fun LiveNewsSection() {
    Column {
        Text(
            text = "📰 ISPL RECENT PRESS & EDITORIALS",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            NewsCard(
                title = "Tata Group seals official Season 1 Title Sponsorship",
                body = "The mega Indian conglomerate signs ₹50L+ core multi-year deal as title presenter of the league, ensuring grassroot training kits.",
                date = "June 2026"
            )
            NewsCard(
                title = "Sourav Ganguly welcomes ISPL scouting pathways",
                body = "Former national captain welcomes ISPL tech trials system. 'This is the exact profiling tool schools have required for a decade.'",
                date = "May 2026"
            )
        }
    }
}

@Composable
fun NewsCard(title: String, body: String, date: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = date, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldBright)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextLight)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = body, fontSize = 11.sp, color = SlateTextMuted, lineHeight = 15.sp)
        }
    }
}

@Composable
fun MediaGallerySection() {
    Column {
        Text(
            text = "🎬 OFFICIAL MEDIA ARCHIVE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
                    .background(Color(0xFF04122E), RoundedCornerShape(8.dp))
                    .border(0.5.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📺", fontSize = 24.sp)
                    Text("Highlights", fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                    Text("U16 Semis 2026", fontSize = 8.sp, color = SlateTextMuted)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp)
                    .background(Color(0xFF04122E), RoundedCornerShape(8.dp))
                    .border(0.5.dp, GoldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 24.sp)
                    Text("Audits Pitch", fontSize = 11.sp, color = SlateTextLight, fontWeight = FontWeight.Bold)
                    Text("Delhi Trials Log", fontSize = 8.sp, color = SlateTextMuted)
                }
            }
        }
    }
}

@Composable
fun SponsorshipTiersSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "🤝 CORPORATE PARTNERSHIP PACKAGES",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "ISPL provides a premium engagement portal with extensive sports branding benefits across digital profile sheets, ground fixtures, and live broadcast frames.",
            fontSize = 12.sp,
            color = SlateTextMuted,
            lineHeight = 16.sp
        )

        SponsorTierCard(tier = "TITLE SPONSOR", amount = "₹5,00,000+", perks = "🏆 Primary Broadcast Branding + Front Jersey + Matchday Title Branding")
        SponsorTierCard(tier = "POWERED BY SPONSOR", amount = "₹2,50,000+", perks = "⚡ Middle Jersey Branding + Ground Hoardings + Dynamic AI Stats Sponsor")
        SponsorTierCard(tier = "ASSOCIATE SPONSOR", amount = "₹1,00,000+", perks = "📍 Sleeve Branding + Social Media Coverage + Trial Caps sponsor")
        SponsorTierCard(tier = "PARTNER SPONSOR", amount = "₹50,000+", perks = "🤝 Ground Banner + Youth Profile Booklet Ads + Shared Desk")

        Card(
            colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldBright.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "📈 ALLIANCE BENEFITS INCLUDE:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldBright
                )
                Spacer(modifier = Modifier.height(6.dp))
                SponsorBenefitRow(benefit = "Ground Branding on boundary boards + sightscreens")
                SponsorBenefitRow(benefit = "Digital Promotion overlay on dynamic player profiles")
                SponsorBenefitRow(benefit = "Social Media Promotion to 1,00,000+ local school networks")
                SponsorBenefitRow(benefit = "Active Youth Engagement workshops in 100+ state cities")
                SponsorBenefitRow(benefit = "Brand Visibility on televised Youtube Live championships")
            }
        }
    }
}

@Composable
fun SponsorTierCard(tier: String, amount: String, perks: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, GoldBright.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tier, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = GoldBright, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = perks, fontSize = 12.sp, color = SlateTextLight, lineHeight = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .background(GoldBright.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = amount, fontSize = 13.sp, fontWeight = FontWeight.Black, color = GoldBright)
            }
        }
    }
}

@Composable
fun SponsorBenefitRow(benefit: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
        Icon(Icons.Default.Star, contentDescription = null, tint = GoldBright, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = benefit, fontSize = 11.sp, color = SlateTextLight)
    }
}

@Composable
fun RevenueFrameworkSection() {
    Column {
        Text(
            text = "💼 REVENUE MODEL & FINANCIAL ENGINE",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "A multi-faced cashflow framework guarantees sustainability, long term profitability, and investor scaling options:",
                    fontSize = 12.sp,
                    color = SlateTextLight,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                RevenueItem(title = "School Registration Fees", desc = "Central entry and management desk auditing")
                RevenueItem(title = "Player Trial Logging (₹599/cap)", desc = "High-volume basic trial ticket entries")
                RevenueItem(title = "State Franchise Lease Fees", desc = "License fees for corporate/celebrity state ownerships")
                RevenueItem(title = "Corporate Sponsorship Alliance", desc = "Multilevel corporate brand associations")
                RevenueItem(title = "Media & Broadcast Rights", desc = "Exclusive TV, streaming & recording rights leases")
                RevenueItem(title = "Official Merchandise & Gear Sales", desc = "Co-branded professional cricket equipment")
                RevenueItem(title = "Local Banner Advertising", desc = "Stadium hoarding slots on tournament matches")
                RevenueItem(title = "Premium Cricket Bootcamps", desc = "Post-league expert training courses")
            }
        }
    }
}

@Composable
fun RevenueItem(title: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "• $title", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldBright)
        Text(text = desc, fontSize = 10.sp, color = SlateTextMuted, modifier = Modifier.padding(start = 12.dp))
    }
}

@Composable
fun VisionMissionBlock() {
    Column {
        Text(
            text = "👁️ VISION 2030 & MISSION STATEMENT",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GoldBright,
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = NavyCardBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "OUR VISION 2030 GOALS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldBright,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                VisionTargetRow(icon = "🏫", target = "10,000+ Onboarded Schools")
                VisionTargetRow(icon = "🏃", target = "100,000+ Profiling Players")
                VisionTargetRow(icon = "🏆", target = "Fully Integrated Pan India Cup Championship")
                VisionTargetRow(icon = "🌏", target = "International School Cricket Circuit")
                VisionTargetRow(icon = "🚀", target = "Direct Talent Gateway to Ranji & IPL Pools")

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = SlateTextMuted.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "OUR CORE MISSION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldBright,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "To build India\'s largest inclusive school cricket platform that identifies, develops, and promotes young sports talent through cutting-edge digital technology, structured scouting camps, and secure franchise auction boards.",
                    fontSize = 12.sp,
                    color = SlateTextLight,
                    lineHeight = 17.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun VisionTargetRow(icon: String, target: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
        Box(
            modifier = Modifier
                .background(GoldBright.copy(alpha = 0.1f), CircleShape)
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = target, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = SlateTextLight)
    }
}
