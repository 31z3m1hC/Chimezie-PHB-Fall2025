package com.unh.personal_health_buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.unh.personal_health_buddy.firebase.SetupAuthentication
import com.unh.personal_health_buddy.ui.theme.Personal_health_buddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            Personal_health_buddyTheme {
                SetupAuthentication(navController, this)
            }
        }
    }
}
