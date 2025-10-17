package com.unh.personal_health_buddy.screens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.firebase.performGoogleAuthentication
import com.unh.personal_health_buddy.firebase.performSignIn
import com.unh.personal_health_buddy.R

@Composable
fun SignInScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

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
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
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
            isError = emailErrorState.value,
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password field
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            isError = passwordErrorState.value,
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password")
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        // Forgot password link
        Text(
            text = "Forgot Password?",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 8.dp)
                .clickable {
                    navController.navigate("reset-password")
                }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sign in button
        Button(
            onClick = {
                performSignIn(
                    email.value,
                    password.value,
                    emailErrorState,
                    passwordErrorState,
                    context,
                    navController
                )
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("OR")
        Spacer(modifier = Modifier.height(24.dp))

        // Google sign-in button
        OutlinedButton(
            onClick = {
                if (googleSignInClient != null && launcher != null) {
                    performGoogleAuthentication(
                        launcher,
                        context,
                        context.getString(R.string.default_web_client_id),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Sign in with Google")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sign up link
        Text(
            text = "Don't have an account? Sign Up",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("sign-up")
            }
        )
    }
}
