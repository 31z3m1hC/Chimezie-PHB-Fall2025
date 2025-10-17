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
    val startDestination = if (auth.currentUser != null) "main" else "emergency-contacts"

    NavHost(
        navController,
        startDestination = startDestination
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher, navController.context) }
        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }
        composable("reset-password") { ResetPasswordScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("home") { HomeScreen() }
        composable("map") { MapScreen() }
        composable("notification") { NotificationScreen() }
    }
}
