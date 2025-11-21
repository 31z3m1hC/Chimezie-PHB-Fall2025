package com.unh.personal_health_buddy.navigations

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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.database.BottomNavItem
import com.unh.personal_health_buddy.ui.theme.ButtonBlue
import com.unh.personal_health_buddy.ui.theme.MediumGray



// -------------------- BOTTOM NAV DATA --------------------
val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Filled.Home, "Home"),
    BottomNavItem("map", Icons.Filled.LocationOn, "Map"),
    BottomNavItem("notifications", Icons.Filled.Notifications, "Notification"),
    BottomNavItem("profile", Icons.Filled.Person, "Profile")
)


// -------------------- SCREENS THAT SHOULD HIDE BOTTOM NAV --------------------
val screensWithoutBottomNav = setOf(
    "welcome",
    "sign-in",
    "sign-up",
    "reset-password",
)

// -------------------- BOTTOM NAV BAR COMPOSABLE --------------------
@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
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



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomNavBar(){
    BottomNavBar(onItemClick = {}, currentRoute = "home")
}