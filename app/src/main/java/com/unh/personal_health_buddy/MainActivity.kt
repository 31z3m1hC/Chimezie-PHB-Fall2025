package com.unh.personal_health_buddy

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.UserDataCache
import com.unh.personal_health_buddy.firebase.SetupAuthentication
import com.unh.personal_health_buddy.ui.theme.PersonalHealthBuddyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        Log.d("MainActivity", "onCreate called")

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            PersonalHealthBuddyTheme {
                LaunchedEffect(Unit) {
                    FirebaseApp.initializeApp(context)
                }

                // ✅ Optional: Fetch data if user is already logged in (app restart case)
                LaunchedEffect(Unit) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null && !UserDataCache.isDataLoaded) {
                        withContext(Dispatchers.IO) {
                            try {
                                Log.d("MainActivity", "User already logged in, fetching data...")

                                UserDataCache.user = FirestoreHelper.getUser(uid)
                                UserDataCache.emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
                                UserDataCache.healthInfo = FirestoreHelper.getHealthInformation()

                                // Load profile image
                                val tempBitmap = TempProfileStorage.tempProfileBitmap
                                if (tempBitmap != null) {
                                    UserDataCache.profileBitmap = tempBitmap
                                } else {
                                    UserDataCache.user?.profileImageUrl?.let { url ->
                                        try {
                                            val stream = URL(url).openStream()
                                            UserDataCache.profileBitmap = BitmapFactory.decodeStream(stream)
                                        } catch (e: Exception) {
                                            Log.e("MainActivity", "Error loading profile image: ${e.message}")
                                        }
                                    }
                                }

                                UserDataCache.isDataLoaded = true
                                Log.d("MainActivity", "User data cached successfully")
                            } catch (e: Exception) {
                                Log.e("MainActivity", "Error loading user data: ${e.message}")
                            }
                        }
                    }
                }

                SetupAuthentication(
                    activity = this@MainActivity,
                    navController = navController,
                )
            }
        }
    }
}