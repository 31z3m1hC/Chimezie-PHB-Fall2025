import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import com.unh.personal_health_buddy.screens.GoogleMapScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.profileItems

import com.unh.personal_health_buddy.ui.theme.LightBlueBackground
import com.unh.personal_health_buddy.ui.theme.ReportsCyan


// -------------------- DATA CLASS + ITEMS --------------------
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Default.Home, "Home"),
    BottomNavItem("map", Icons.Default.LocationOn, "Map"),
    BottomNavItem("notifications", Icons.Default.Notifications, "Notification"),
    BottomNavItem("profile", Icons.Default.Person, "Profile"),
)

@Composable
fun MainScreen(navController: NavHostController) {
    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(listOf(ReportsCyan, LightBlueBackground))
            ),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            // ✅ FIX: Proper navigation with correct popUpTo
                            innerNavController.navigate(item.route) {
                                // Pop up to the start destination (home) to avoid stack buildup
                                popUpTo(innerNavController.graph.findStartDestination().id) {
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
    ) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("map") { GoogleMapScreen(navController) }
            composable("notifications") { NotificationScreen(navController) }
            composable("profile") { ProfileScreen(navController, profileItems, "profile") }
            composable("account") { AccountScreen(navController) }
            composable("account-form") { AccountFormScreen(navController) }
        }
    }
}