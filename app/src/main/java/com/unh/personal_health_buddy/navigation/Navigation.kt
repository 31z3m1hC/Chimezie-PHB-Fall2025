package com.unh.personal_health_buddy.navigation

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.screens.AccountScreen
import com.unh.personal_health_buddy.screens.AppointmentScreen
import com.unh.personal_health_buddy.screens.ContactScreen
import com.unh.personal_health_buddy.screens.EmergencyContactScreen
import com.unh.personal_health_buddy.screens.FAQScreen
import com.unh.personal_health_buddy.screens.GoogleMapScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.LogOutScreen
import com.unh.personal_health_buddy.screens.LogoutScreen
import com.unh.personal_health_buddy.screens.MainScreen
import com.unh.personal_health_buddy.screens.MapScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.ResetPasswordScreen
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.screens.WelcomeScreen
import com.unh.personal_health_buddy.screens.profileItems

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "profile" else "welcome"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("account") { AccountScreen(navController) }
        composable("appointment") { AppointmentScreen(navController) }
        composable("faqs") { FAQScreen(navController) }
        composable("contacts") { ContactScreen(navController) }
        composable("logout") { LogOutScreen(navController) }
        composable("profile") { ProfileScreen(navController, profileItems, currentRoute = "profile") }
        composable("welcome") { WelcomeScreen(navController) }


        composable("sign-in") {
            SignInScreen(
                navController = navController,
                googleSignInClient = googleSignInClient,
                launcher = launcher
            )
        }
        composable("sign-up") {
            SignUpScreen(
                navController = navController,
                googleSignInClient = googleSignInClient,
                launcher = launcher
            )
        }
        composable("map") { GoogleMapScreen(navController) }

        composable("reset-password") { ResetPasswordScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("notification") { NotificationScreen(navController) }
        composable("emergency-contacts") { EmergencyContactScreen(navController) }
//        composable("map") {
//            MapScreen()
//        }
        composable("profile") {
            val currentRoute = ""
            ProfileScreen(navController, profileItems, currentRoute)
        }
        composable ("account" ) {AccountScreen(navController)}
    }
    Log.d("AppNavigation", "Navigation setup")
}
