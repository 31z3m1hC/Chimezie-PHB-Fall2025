package com.unh.personal_health_buddy.screens

import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.BottomBar

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val title: String
)


private val bottomNavItems = listOf(
    NavigationItem("home", Icons.Filled.Home, "Home"),
    NavigationItem("map", Icons.Filled.Map, "Map"),
    NavigationItem("notification", Icons.Filled.Notifications, "Notification"),
    NavigationItem("profile", Icons.Filled.Person, "Profile"),
    NavigationItem("emergency-contacts", Icons.Filled.Call, "Emergency")
)

@Composable
fun MainScreen(navController: NavHostController, startDestination: String = "sign-in") {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {
                composable("home") { HomeScreen(navController) }
                composable("map") { MapScreen(navController) }
                composable("notification") { NotificationScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
                composable("emergency-contacts") { EmergencyContactScreenContent() }
            }
        }
    }
}


@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(

        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Profile Screen")
        }
    }
}


@Composable
fun MapScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Map Screen")
        }
    }
}

@Composable
fun NotificationScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Notification Screen")
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Home Screen")
        }
    }
}

@Composable
fun EmergencyContactScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Emergency Contact Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    MainScreen(navController = navController, startDestination = "home")
}
