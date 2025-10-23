package com.unh.personal_health_buddy.screens

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignInScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?
) {
    val email = remember { mutableStateOf("") }
    val password = _root_ide_package_.androidx.compose.runtime.remember {
        _root_ide_package_.androidx.compose.runtime.mutableStateOf("")
    }
    var passwordVisible by _root_ide_package_.androidx.compose.runtime.remember {
        _root_ide_package_.androidx.compose.runtime.mutableStateOf(
            false
        )
    }
    val signInMessage = _root_ide_package_.androidx.compose.runtime.remember {
        _root_ide_package_.androidx.compose.runtime.mutableStateOf("")
    }

    val coroutineScope = _root_ide_package_.androidx.compose.runtime.rememberCoroutineScope()
    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    _root_ide_package_.androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier.Companion
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.Companion.CenterHorizontally
    ) {
        // Back button
        _root_ide_package_.androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth()
                .padding(start = 16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Companion.TopStart
        ) {
            _root_ide_package_.androidx.compose.material3.IconButton(onClick = {
                navController.navigate(
                    "welcome"
                )
            }) {
                _root_ide_package_.androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Welcome"
                )
            }
        }

        // Title
        _root_ide_package_.androidx.compose.material3.Text(
            text = "Sign In",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = androidx.compose.ui.Modifier.Companion.padding(bottom = 30.dp, top = 16.dp)
        )

        // Email field
        _root_ide_package_.androidx.compose.material3.OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { _root_ide_package_.androidx.compose.material3.Text("Enter your email") },
            leadingIcon = {
                _root_ide_package_.androidx.compose.material3.Icon(
                    androidx.compose.material.icons.Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            singleLine = true,
            keyboardOptions = _root_ide_package_.androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Companion.Email,
                imeAction = androidx.compose.ui.text.input.ImeAction.Companion.Next
            ),
            modifier = androidx.compose.ui.Modifier.Companion
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
        )

        // Password field
        _root_ide_package_.androidx.compose.material3.OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { _root_ide_package_.androidx.compose.material3.Text("Enter your password") },
            leadingIcon = {
                _root_ide_package_.androidx.compose.material3.Icon(
                    androidx.compose.material.icons.Icons.Default.Lock,
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                val image =
                    if (passwordVisible) androidx.compose.material.icons.Icons.Filled.VisibilityOff else androidx.compose.material.icons.Icons.Filled.Visibility
                _root_ide_package_.androidx.compose.material3.IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    _root_ide_package_.androidx.compose.material3.Icon(
                        imageVector = image,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.Companion.None else _root_ide_package_.androidx.compose.ui.text.input.PasswordVisualTransformation(),
            keyboardOptions = _root_ide_package_.androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Companion.Password,
                imeAction = androidx.compose.ui.text.input.ImeAction.Companion.Done
            ),
            modifier = androidx.compose.ui.Modifier.Companion
                .padding(bottom = 8.dp)
                .fillMaxWidth(0.9f)
        )

        // Forgot password link
        _root_ide_package_.androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
        ) {
            _root_ide_package_.androidx.compose.material3.Text(
                text = "Forgot password?",
                color = _root_ide_package_.androidx.compose.ui.res.colorResource(id = com.unh.personal_health_buddy.R.color.teal_700),
                modifier = androidx.compose.ui.Modifier.Companion.clickable {
                    navController.navigate("reset-password")
                }
            )
        }

        // Error / success message
        if (signInMessage.value.isNotEmpty()) {
            _root_ide_package_.androidx.compose.material3.Text(
                text = signInMessage.value,
                color = if (signInMessage.value.contains("successful")) androidx.compose.ui.graphics.Color.Companion.Green else androidx.compose.ui.graphics.Color.Companion.Red,
                modifier = androidx.compose.ui.Modifier.Companion
                    .padding(start = 32.dp, top = 4.dp, bottom = 8.dp)
                    .fillMaxWidth(0.9f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Companion.Start
            )
        }

        // Sign In button (Email/Password)
        _root_ide_package_.androidx.compose.material3.Button(
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
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth(0.9f)
                .padding(top = 8.dp),
            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
                1.dp,
                _root_ide_package_.androidx.compose.ui.res.colorResource(id = com.unh.personal_health_buddy.R.color.purple_500)
            ),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = _root_ide_package_.androidx.compose.ui.res.colorResource(id = com.unh.personal_health_buddy.R.color.purple_500),
                contentColor = androidx.compose.ui.graphics.Color.Companion.White
            )
        ) {
            _root_ide_package_.androidx.compose.material3.Text(text = "Sign In")
        }

        // OR separator
        _root_ide_package_.androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.Companion.height(
                24.dp
            )
        )
        _root_ide_package_.androidx.compose.material3.Text(
            "OR",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        _root_ide_package_.androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.Companion.height(
                24.dp
            )
        )

        // Google Sign-In button — only launches; navigation handled in MainActivity
        _root_ide_package_.androidx.compose.material3.OutlinedButton(
            onClick = {
                val intent = googleSignInClient?.signInIntent
                if (intent != null && launcher != null) {
                    launcher.launch(intent)
                } else {
                    signInMessage.value = "Google Sign-In is unavailable right now."
                }
            },
            modifier = androidx.compose.ui.Modifier.Companion.fillMaxWidth(0.9f),
            border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
                1.dp,
                _root_ide_package_.androidx.compose.ui.res.colorResource(id = com.unh.personal_health_buddy.R.color.teal_700)
            ),
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                contentColor = _root_ide_package_.androidx.compose.ui.res.colorResource(
                    id = com.unh.personal_health_buddy.R.color.teal_700
                )
            )
        ) {
            _root_ide_package_.androidx.compose.material3.Text("Sign in with Google")
        }

        // Don't have an account? Sign up
        _root_ide_package_.androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.Companion
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            _root_ide_package_.androidx.compose.material3.Text(text = "Don't have an account? ")
            _root_ide_package_.androidx.compose.material3.Text(
                text = "Sign up",
                color = _root_ide_package_.androidx.compose.ui.res.colorResource(id = com.unh.personal_health_buddy.R.color.teal_700),
                modifier = androidx.compose.ui.Modifier.Companion.clickable {
                    navController.navigate(
                        "sign-up"
                    )
                }
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@androidx.compose.runtime.Composable
fun SignInScreenPreview() {
    val navController = _root_ide_package_.androidx.navigation.compose.rememberNavController()
    SignInScreen(navController = navController, googleSignInClient = null, launcher = null)
}