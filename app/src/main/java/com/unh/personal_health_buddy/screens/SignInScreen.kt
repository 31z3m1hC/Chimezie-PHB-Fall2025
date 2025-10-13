package com.unh.personal_health_buddy.screens

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignInScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val signInMessage = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.navigate("welcome") }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back to Welcome"
                )
            }
        }

        // Title
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp, top = 16.dp)
        )

        // Email field
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Enter your email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
        )

        // Password field
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Enter your password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(0.9f)
        )

        // Forgot password link
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot password?",
                color = colorResource(id = R.color.teal_700),
                modifier = Modifier.clickable {
                    navController.navigate("reset-password")
                }
            )
        }

        // Error / success message
        if (signInMessage.value.isNotEmpty()) {
            Text(
                text = signInMessage.value,
                color = if (signInMessage.value.contains("successful")) Color.Green else Color.Red,
                modifier = Modifier
                    .padding(start = 32.dp, top = 4.dp, bottom = 8.dp)
                    .fillMaxWidth(0.9f),
                textAlign = TextAlign.Start
            )
        }

        // Sign In button (Email/Password)
        Button(
            onClick = {
                coroutineScope.launch {
                    if (email.value.isBlank() || password.value.isBlank()) {
                        signInMessage.value = "Please enter both email and password."
                        return@launch
                    }
                    try {
                        auth.signInWithEmailAndPassword(email.value, password.value).await()
                        signInMessage.value = "Sign in successful! Welcome back."
                        email.value = ""
                        password.value = ""
                        passwordVisible = false
                        navController.navigate("home") {
                            popUpTo("sign-in") { inclusive = true }
                        }
                    } catch (e: Exception) {
                        signInMessage.value = e.message ?: "Sign in failed. Please try again."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 8.dp),
            border = BorderStroke(1.dp, colorResource(id = R.color.purple_500)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text(text = "Sign In")
        }

        // OR separator
        Spacer(modifier = Modifier.height(24.dp))
        Text("OR", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Google Sign-In button — only launches; navigation handled in MainActivity
        OutlinedButton(
            onClick = {
                val intent = googleSignInClient?.signInIntent
                if (intent != null && launcher != null) {
                    launcher.launch(intent)
                } else {
                    signInMessage.value = "Google Sign-In is unavailable right now."
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            border = BorderStroke(1.dp, colorResource(id = R.color.teal_700)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.teal_700))
        ) {
            Text("Sign in with Google")
        }

        // Don't have an account? Sign up
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account? ")
            Text(
                text = "Sign up",
                color = colorResource(id = R.color.teal_700),
                modifier = Modifier.clickable { navController.navigate("sign-up") }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    val navController = rememberNavController()
    SignInScreen(navController = navController, googleSignInClient = null, launcher = null)
}