package com.unh.personal_health_buddy.navigation

import AccountFormScreen
import AccountScreen
import GoogleMapScreen
import LogoutConfirmationDialog
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.features.BloodGroupScreen
import com.unh.personal_health_buddy.screens.*

import com.unh.personal_health_buddy.ui.theme.ButtonBlue
import com.unh.personal_health_buddy.ui.theme.MediumGray

// -------------------- DATA CLASS --------------------
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// -------------------- BOTTOM NAV ITEMS --------------------
val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Filled.Home, "Home"),
    BottomNavItem("map", Icons.Filled.LocationOn, "Map"),
    BottomNavItem("notifications", Icons.Filled.Notifications, "Notification"),
    BottomNavItem("profile", Icons.Filled.Person, "Profile")
)

// -------------------- BOTTOM NAV BAR --------------------
@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) },
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

// -------------------- WRAPPER --------------------
@Composable
fun ScreenWithBottomNav(
    navController: NavHostController,
    content: @Composable (NavHostController) -> Unit
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content(navController)
        }
    }
}

// -------------------- APP NAVIGATION --------------------
@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>,
) {
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // Auth screens (no bottom nav)
        composable("welcome") { WelcomeScreen(navController) }
        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher) }
        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }
        composable("reset-password") {
            ResetPasswordDialog(navController, onDismiss = { navController.popBackStack() })
        }

        // Screens WITH bottom nav
        composable("home") {
            ScreenWithBottomNav(navController) { innerNav ->
                HomeScreen(innerNav)
            }
        }
        composable("map") {
            ScreenWithBottomNav(navController) { innerNav ->
                GoogleMapScreen(innerNav)
            }
        }
        composable("notifications") {
            ScreenWithBottomNav(navController) { innerNav ->
                NotificationScreen(innerNav)
            }
        }
        composable("profile") {
            ScreenWithBottomNav(navController) { innerNav ->
                ProfileScreen(innerNav, profileItems, "profile")
            }
        }
        composable("blood_group_screen") {
            ScreenWithBottomNav(navController) { innerNav ->
                BloodGroupScreen(innerNav)
            }
        }
        composable("medicates_screen") {
            ScreenWithBottomNav(navController) { innerNav ->
                MedicateScreen(innerNav)
            }
        }
        composable("emergency-contacts") {
            ScreenWithBottomNav(navController) { innerNav ->
                EmergencyContactScreen(innerNav)
            }
        }
        composable("account") { AccountScreen(navController) }
        composable("account-form") { AccountFormScreen(navController) }



        // Other feature screens (no bottom nav)
        //composable("chat_ai_screen") { ChatAIScreen(navController) }
        //composable("faqs") { FaqsScreen(navController) }
        composable("logout") { LogoutConfirmationDialog(onConfirm = { navController.popBackStack() }, onCancel = {}) }
        composable("notifications") { NotificationScreen(navController) }
    }
}
