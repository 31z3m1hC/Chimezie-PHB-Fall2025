package com.unh.personal_health_buddy.navigation

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.chats.MessageListScreen
import com.unh.personal_health_buddy.database.UserAccountForm
import com.unh.personal_health_buddy.screens.AppointmentScreen
import com.unh.personal_health_buddy.screens.ContactScreen
import com.unh.personal_health_buddy.screens.EmergencyContactScreen
import com.unh.personal_health_buddy.screens.FAQScreen
import com.unh.personal_health_buddy.screens.GoogleMapScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.LogOutScreen
import com.unh.personal_health_buddy.screens.MainScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.ResetPasswordScreen
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.database.UserAccountForm
import com.unh.personal_health_buddy.screens.WelcomeScreen
import com.unh.personal_health_buddy.screens.profileItems

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>
) {

    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "home" else "welcome"
    Log.d("AppNavigation", "Start destination: $startDestination")

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") { WelcomeScreen(navController) }

        composable("messages/{cid}") { backStackEntry ->
            val cid = backStackEntry.arguments?.getString("cid") ?: return@composable
            MessageListScreen(navController, cid)
        }

        composable("chats") {
            MessageListScreen(
            navController = navController,
            cid = "preview_cid",
            currentUserName = "You",
        )}
        composable("userAccountForm") {
            UserAccountForm(
                navController = navController,
                onUserSaved = {}
            )
        }

        composable("home") { HomeScreen(navController) }
        composable("profile") { ProfileScreen(navController, profileItems, "profile") }
        composable("user-details") {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid ?: ""
            UserAccountForm(navController, onUserSaved = {})
        }
        composable("appointment") { AppointmentScreen(navController) }
        composable("faqs") { FAQScreen(navController) }
        composable("contacts") { ContactScreen(navController) }
        composable("logout") { LogOutScreen(navController) }
        composable("sign-in") {
            SignInScreen(navController, googleSignInClient, launcher)
        }
        composable("sign-up") {
            SignUpScreen(navController, googleSignInClient, launcher)
        }
        composable("map") { GoogleMapScreen(navController) }
        composable("reset-password") { ResetPasswordScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("notification") { NotificationScreen(navController) }
        composable("emergency-contacts") { EmergencyContactScreen(navController) }
    }

    Log.d("AppNavigation", "Navigation setup complete")
}
