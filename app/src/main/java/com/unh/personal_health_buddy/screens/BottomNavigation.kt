package com.unh.personal_health_buddy.navigations

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.database.BottomNavItem
import com.unh.personal_health_buddy.ui.theme.ButtonBlue
import com.unh.personal_health_buddy.ui.theme.MediumGray
import com.unh.personal_health_buddy.database.*




// --- Data Classes and Colors (Required for the code to run) ---
// Define these at the top of your file or import them.

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val title: String // Not used in NavBar, but required by your data class
)

val ButtonBlue = Color(0xFF5AA9E6) // Example Color
val MediumGray = Color.Gray         // Example Color

// -------------------- BOTTOM NAV DATA --------------------

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Filled.Home, "Home", "Home"),
    BottomNavItem("map", Icons.Filled.LocationOn, "Map", "Map"),
    BottomNavItem("notifications", Icons.Filled.Notifications, "Notifications", "Notifications"),
    BottomNavItem("profile", Icons.Filled.Person, "Profile", "Profile")
)



@Composable
fun BottomNavBar(
    currentRoute: String?,
    innerNavController: NavHostController
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        innerNavController.navigate(item.route) {
                            popUpTo("bottom_root") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ButtonBlue,
                    selectedTextColor = ButtonBlue,
                    unselectedIconColor = MediumGray,
                    unselectedTextColor = MediumGray
                )
            )
        }
    }
}





// --- Preview remains the same ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomNavBar() {
    val navController = rememberNavController()
    BottomNavBar(
        currentRoute = "home",
        innerNavController = navController
    )
}