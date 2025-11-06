package com.unh.personal_health_buddy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.firebase.SetupAuthentication
import com.unh.personal_health_buddy.ui.theme.PersonalHealthBuddyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("MainActivity", "onCreate called")

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            PersonalHealthBuddyTheme {
                LaunchedEffect(Unit) {
                    FirebaseApp.initializeApp(context)
                }

                SetupAuthentication(
                    activity = this@MainActivity,
                    navController = navController,
                )
            }
        }
    }
}
