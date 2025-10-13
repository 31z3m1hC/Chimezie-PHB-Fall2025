package com.unh.personal_health_buddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.unh.personal_health_buddy.screens.*
import com.unh.personal_health_buddy.ui.theme.Personal_health_buddyTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            Personal_health_buddyTheme {
                val navController = rememberNavController()

                // Configure Google Sign-In
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)

                // Launcher for Google Sign-In
                val launcher = rememberFirebaseAuthLauncher(
                    onAuthComplete = {
                        navController.navigate("home") {
                            popUpTo("sign-in") { inclusive = true }
                        }
                        Log.d("Auth", "User: ${FirebaseAuth.getInstance().currentUser?.uid}")
                    },
                    onAuthError = {
                        Log.e("MainActivity", "Authentication failed: ${it.message}", it)
                    }
                )
                val auth = FirebaseAuth.getInstance()
                val startDestination = if (auth.currentUser != null) "sign-in" else "home"
                Log.d("Auth", "Start destination: $startDestination")


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("welcome") {
                            WelcomeScreen(navController)
                        }
                        composable("sign-up") {
                            SignUpScreen(
                                navController = navController,
                                googleSignInClient = googleSignInClient,
                                launcher = launcher
                            )
                        }
                        composable("sign-in") {
                            SignInScreen(
                                navController = navController,
                                googleSignInClient = googleSignInClient,
                                launcher = launcher
                            )
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("reset-password") {
                            ResetPasswordScreen(navController = navController)
                        }
                        composable("emergency-contacts") {
                            EmergencyContactScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = "🏠 Home Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        // Button that navigates to Emergency Contact screen
        Button(
            onClick = { navController.navigate("emergency-contacts") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Emergency Contacts",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Go to Emergency Contacts",
                color = Color.White
            )
        }
    }
}


@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != android.app.Activity.RESULT_OK || result.data == null) {
            onAuthError(IllegalStateException("Google Sign-In canceled or no data returned"))
            return@rememberLauncherForActivityResult
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken.isNullOrBlank()) {
                onAuthError(IllegalStateException("Missing ID token from Google account"))
                return@rememberLauncherForActivityResult
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            scope.launch {
                try {
                    val authResult = FirebaseAuth.getInstance()
                        .signInWithCredential(credential)
                        .await()
                    onAuthComplete(authResult)
                } catch (firebaseError: Exception) {
                    onAuthError(firebaseError)
                }
                Log.d("Auth", "User: ${FirebaseAuth.getInstance().currentUser?.uid}")
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}
