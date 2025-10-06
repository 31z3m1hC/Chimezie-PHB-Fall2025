package com.unh.personal_health_buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.ui.theme.Personal_health_buddyTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Initialize Firebase
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            Personal_health_buddyTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "sign-in",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("welcome") {
                            WelcomeScreen(navController)
                        }
                        composable("sign-up") {
                            SignUpScreen(navController = navController)
                        }
                        composable("sign-in") {
                            SignInScreen(navController = navController)
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("reset-password") {
                            ResetPasswordScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Welcome Screen")
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen")
    }
}

@Composable
fun ResetPasswordScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Reset Password Screen")
    }
}
