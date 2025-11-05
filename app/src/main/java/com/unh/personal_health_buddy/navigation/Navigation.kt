package com.unh.personal_health_buddy.navigation

import SignUpScreen
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.database.AccountForm
import com.unh.personal_health_buddy.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>,
    activity: Activity
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "welcome" else "sign-in"
    Log.d("AppNavigation", "Start destination: $startDestination")

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("welcome") { WelcomeScreen(navController) }

        composable("faqs") { FAQScreen(navController) }

        composable("messages/{cid}") { backStackEntry ->
            val cid = backStackEntry.arguments?.getString("cid") ?: return@composable
            MessageListScreen(navController, cid)
        }

        composable("chats") {
            MessageListScreen(
                navController = navController,
                cid = "preview_cid",
                currentUserName = "You"
            )
        }

        composable("account-form") {
            AccountForm(
                navController,
                onSave = {},
                )
        }

        composable("home") { HomeScreen(navController) }
        composable("profile") { ProfileScreen(navController, profileItems, "profile") }


        composable("appointment") { AppointmentScreen(navController) }
        composable("contacts") { ContactScreen(navController) }



        composable("sign-in") {
            SignInScreen(
                navController = navController,
                googleSignInClient = googleSignInClient,
                launcher = launcher,
                activity = activity
            )
        }

        composable("sign-up") {
            SignUpScreen(
                navController = navController,
                googleSignInClient = googleSignInClient,
                launcher = launcher,
                activity = activity
            )
        }

        composable("map") { GoogleMapScreen(navController) }
        composable("reset-password") { ResetPasswordScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("notification") { NotificationScreen(navController) }
        composable("emergency-contacts") { EmergencyContactScreen(navController) }
    }

    Log.d("AppNavigation", "Navigation setup complete")
}
