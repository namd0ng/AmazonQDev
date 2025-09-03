package com.example.amazonqdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.amazonqdev.nav.AppScreen
import com.example.amazonqdev.ui.calendar.CalendarScreen
import com.example.amazonqdev.ui.home.HomeScreen
import com.example.amazonqdev.ui.settings.SettingsScreen
import com.example.amazonqdev.ui.theme.AlcoLookTheme
import com.example.amazonqdev.ui.theme.Elevation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoLookTheme {
                AlcoLookApp()
            }
        }
    }
}

@Composable
fun AlcoLookApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = Elevation.BottomNav
            ) {
                AppScreen.entries.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Text(
                                text = screen.icon,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.HOME.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.HOME.route) {
                HomeScreen()
            }
            composable(AppScreen.CALENDAR.route) {
                CalendarScreen()
            }
            composable(AppScreen.SETTINGS.route) {
                SettingsScreen()
            }
        }
    }
}