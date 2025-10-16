package com.unh.personal_health_buddy.navigation

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.screens.*


@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "emergency-contacts" else "sign-in"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable ("home") { HomeScreen(navController) }
        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher) }
        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }
        composable("reset-password") { ResetPasswordScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("notification") { NotificationScreen(navController) }
        composable("emergency-contacts") { EmergencyContactScreen(navController) }
        composable("map") { MapScreen(navController) }
    }
}

