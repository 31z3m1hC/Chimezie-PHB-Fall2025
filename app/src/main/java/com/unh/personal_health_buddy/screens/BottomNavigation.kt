package com.unh.personal_health_buddy.navigations

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Surface
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
import com.unh.personal_health_buddy.navigation.bottomNavItems
import com.unh.personal_health_buddy.ui.theme.BottomNavBar


@Composable
fun BottomNavBar(
    currentRoute: String?,
    innerNavController: NavHostController
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        tonalElevation = 3.dp
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute?.startsWith(item.route) == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            innerNavController.navigate(item.route) {
                                popUpTo("home") { saveState = true }
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
                        unselectedTextColor = MediumGray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomNavBar() {
    val navController = rememberNavController()
    BottomNavBar(
        currentRoute = "home",
        innerNavController = navController
    )
}