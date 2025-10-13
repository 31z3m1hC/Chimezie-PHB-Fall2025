package com.unh.personal_health_buddy



import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DialerSip
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    object Map : NavigationItem("map", Icons.Filled.Map, "Map")
    object Notification : NavigationItem("notification", Icons.Filled.Notifications, "Notification")
    object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")

    object EmergencyContacts : NavigationItem("emergency-contacts", Icons.Filled.DialerSip, "Emergency Contacts")

    object back : NavigationItem("back", Icons.Filled.ArrowBackIosNew, "Back")


}
