package com.unh.personal_health_buddy.navigation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.screens.ResetPasswordScreen
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.screens.WelcomeScreen
import androidx.activity.result.ActivityResult
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.HomeScreen
import com.unh.personal_health_buddy.screens.EmergencyContactScreen
import com.unh.personal_health_buddy.screens.GoogleSignInScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "sign-in" else "home"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }
        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher) }
        composable("reset-password") { ResetPasswordScreen(navController) }
        composable("emergency-contacts") { EmergencyContactScreen(navController) }
        composable("home"){ HomeScreen(navController) }
    }
}