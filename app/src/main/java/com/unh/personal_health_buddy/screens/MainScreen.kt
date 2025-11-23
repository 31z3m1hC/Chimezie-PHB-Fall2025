import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.unh.personal_health_buddy.navigation.CustomStatusBar
import com.unh.personal_health_buddy.navigations.BottomNavBar
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.MedicateScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.profileItems
import com.unh.personal_health_buddy.ui.theme.ChatGreen

//import android.net.http.SslCertificate.restoreState
//import android.net.http.SslCertificate.saveState
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.SideEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import androidx.wear.compose.material3.Icon
//import androidx.wear.compose.material3.Text
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
////import com.unh.personal_health_buddy.screens.GoogleMapScreen
//import com.unh.personal_health_buddy.screens.HomeScreen
//import com.unh.personal_health_buddy.screens.NotificationScreen
//import com.unh.personal_health_buddy.screens.ProfileScreen
//import com.unh.personal_health_buddy.screens.profileItems
//
//import com.unh.personal_health_buddy.ui.theme.LightBlueBackground
//import com.unh.personal_health_buddy.ui.theme.ReportsCyan
//
//// -------------------- DATA CLASS + ITEMS --------------------
//data class BottomNavItem(
//    val route: String,
//    val icon: ImageVector,
//    val label: String
//)
//
//val bottomNavItems = listOf(
//    BottomNavItem("home", Icons.Default.Home, "Home"),
//    BottomNavItem("map", Icons.Default.LocationOn, "Map"),
//    BottomNavItem("notifications", Icons.Default.Notifications, "Notification"),
//    BottomNavItem("profile", Icons.Default.Person, "Profile"),
//)
//
//// -------------------- MAIN SCREEN --------------------
//@Composable
//fun MainScreen(navController: NavHostController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(listOf(ReportsCyan, LightBlueBackground))
//            ),
//        bottomBar = {
//            NavigationBar(
//                containerColor = Color.White,
//                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
//            ) {
//                bottomNavItems.forEach { item ->
//                    NavigationBarItem(
//                        icon = { Icon(item.icon, contentDescription = item.label) },
//                        label = { Text(item.label) },
//                        selected = currentRoute == item.route,
//                        onClick = {
//                            // Recommended bottom nav pattern: single controller, save/restore state
//                            navController.navigate(item.route) {
//                                // Pop to the start destination of the graph to avoid building up a large stack
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                // Avoid multiple copies of the same destination
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                                restoreState = true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = "home",
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            composable("home") { HomeScreen(navController) }
//            composable("map") { GoogleMapScreen(navController) }
//            composable("notifications") { NotificationScreen(navController) }
//            composable("profile") { ProfileScreen(navController, profileItems, "profile") }
//            composable("account") { AccountScreen(navController) }
//            composable("account-form") { AccountFormScreen(navController) }
//        }
//    }
//}


private val screensWithoutBottomNav = setOf(
    "account-form",
    "emergency-contacts",
    "logout"
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNav = currentRoute != null && !screensWithoutBottomNav.contains(currentRoute)

    Column(modifier = Modifier.fillMaxSize()) {
        // Custom Status Bar (no statusBarsPadding needed since system bar is hidden)
        CustomStatusBar(
            backgroundColor = ChatGreen,
            contentColor = Color.White
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showBottomNav) {
                    BottomNavBar(
                        currentRoute = currentRoute,
                        innerNavController = navController
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                // MAIN NAV TABS
                composable("home") { HomeScreen(navController) }
                composable("map") { GoogleMapScreen(navController) }
                composable("notifications") { NotificationScreen(navController) }
                composable("profile") { ProfileScreen(navController, profileItems, "profile") }

                // SECONDARY SCREENS
                composable("medicates_screen") { MedicateScreen(navController) }
                composable("account") { AccountScreen(navController) }
                composable("account-form") { AccountFormScreen(navController) }
                composable("blood_group_screen") { BloodGroupScreen(navController) }
            }
        }
    }
}