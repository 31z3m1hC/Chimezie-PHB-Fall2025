package com.unh.personal_health_buddy.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class NavigationItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(navController: NavHostController) {
    val items = listOf(
        NavigationItem("home", Icons.Filled.Home, "Home"),
        NavigationItem("map", Icons.Filled.Map, "Map"),
        NavigationItem("notification", Icons.Filled.Notifications, "Notification"),
        NavigationItem("profile", Icons.Filled.Person, "Profile"),
        NavigationItem("emergency-contacts", Icons.Filled.Call, "Emergency")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("") })
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        EmergencyContactsDisplayScreen(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}

@Composable
fun EmergencyContactsDisplayScreen(modifier: Modifier = Modifier) {
    val contacts = listOf(
        "Nabin Bamma" to "(555) 123-4567",
        "Rajesh Kumar" to "(555) 987-6543",
        "Chimezie Onwuegbuchulem" to "(555) 246-8101"
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "In case of emergency, contact:",
            style = MaterialTheme.typography.titleMedium,

            modifier = Modifier.padding(bottom = 16.dp)

        )

        contacts.forEach { (name, number) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Contact Icon",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(32.dp)
                    )

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = number,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmergencyContactScreen() {
    val navController = rememberNavController()
    EmergencyContactScreen(navController = navController)
}
