package com.unh.personal_health_buddy.navigation

import addUserToFirestore
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.FAQScreen

import com.unh.personal_health_buddy.screens.AppointmentScreen
import com.unh.personal_health_buddy.screens.ArticleScreen
import com.unh.personal_health_buddy.screens.ContactScreen
import com.unh.personal_health_buddy.screens.EmergencyContactScreen
import com.unh.personal_health_buddy.screens.GoogleMapScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.LogOutScreen
import com.unh.personal_health_buddy.screens.LogoutScreen
import com.unh.personal_health_buddy.screens.MainScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ResetPasswordScreen
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.screens.UserScreen
import com.unh.personal_health_buddy.screens.WelcomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "faqs" else "home"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("appointment") { AppointmentScreen(navController) }
        composable("faqs") { FAQScreen(navController) }
        composable("contacts") { ContactScreen(navController) }
        composable("logout") { LogOutScreen(navController) }
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
        composable("profile") {
            val currentRoute = ""
        }
        composable("account") { UserScreen(navController, onSubmitClicked = {})}

        composable("logout") { LogoutScreen(navController) }
        composable("faq") { FAQScreen(navController) }
        composable("appointment") { AppointmentScreen(navController) }
        composable("article") { ArticleScreen() }

    }
    Log.d("AppNavigation", "Navigation setup")
}
