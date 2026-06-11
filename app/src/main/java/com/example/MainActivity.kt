package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ISPLViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyDeepDusk
import com.example.ui.theme.GoldBright
import com.example.ui.theme.NavyCardBlue

class MainActivity : ComponentActivity() {
    private val viewModel: ISPLViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                // Read current navigation section state from ViewModel
                val currentSection by viewModel.currentSection.collectAsState()
                
                // Keep track of which form was clicked on the Homepage so we can open it directly!
                var preselectedFormType by remember { mutableStateOf("Player") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = NavyCardBlue,
                            tonalElevation = 8.dp,
                            windowInsets = WindowInsets.navigationBars
                        ) {
                            NavigationBarItem(
                                selected = currentSection == "Home",
                                onClick = { viewModel.navigateTo("Home") },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NavyDeepDusk,
                                    selectedTextColor = GoldBright,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    indicatorColor = GoldBright
                                )
                            )

                            NavigationBarItem(
                                selected = currentSection == "Pathway",
                                onClick = { viewModel.navigateTo("Pathway") },
                                icon = { Icon(Icons.Default.Info, contentDescription = "Pathway") },
                                label = { Text("Pathway", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NavyDeepDusk,
                                    selectedTextColor = GoldBright,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    indicatorColor = GoldBright
                                )
                            )

                            NavigationBarItem(
                                selected = currentSection == "Register",
                                onClick = { 
                                    preselectedFormType = "Player"
                                    viewModel.navigateTo("Register") 
                                },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Register") },
                                label = { Text("Register", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NavyDeepDusk,
                                    selectedTextColor = GoldBright,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    indicatorColor = GoldBright
                                )
                            )

                            NavigationBarItem(
                                selected = currentSection == "Dashboard",
                                onClick = { viewModel.navigateTo("Dashboard") },
                                icon = { Icon(Icons.Default.List, contentDescription = "Dashboard") },
                                label = { Text("Dashboard", fontSize = 11.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NavyDeepDusk,
                                    selectedTextColor = GoldBright,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    indicatorColor = GoldBright
                                )
                            )

                            NavigationBarItem(
                                selected = currentSection == "League Table",
                                onClick = { viewModel.navigateTo("League Table") },
                                icon = { Icon(Icons.Default.Star, contentDescription = "League Desk") },
                                label = { Text("League", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NavyDeepDusk,
                                    selectedTextColor = GoldBright,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    indicatorColor = GoldBright
                                )
                            )

                            NavigationBarItem(
                                selected = currentSection == "Admin Desk",
                                onClick = { viewModel.navigateTo("Admin Desk") },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Admin Desk") },
                                label = { Text("Admin", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NavyDeepDusk,
                                    selectedTextColor = GoldBright,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    indicatorColor = GoldBright
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(NavyDeepDusk)
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        when (currentSection) {
                            "Home" -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToRegistration = { formType ->
                                    preselectedFormType = formType
                                    viewModel.navigateTo("Register")
                                }
                            )
                            "Pathway" -> JourneyScreen()
                            "Register" -> RegisterScreen(
                                viewModel = viewModel,
                                initialForm = preselectedFormType
                            )
                            "Dashboard" -> DashboardScreen(viewModel = viewModel)
                            "League Table" -> LeaderboardScreen()
                            "Admin Desk" -> AdminScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
