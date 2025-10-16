package com.unh.personal_health_buddy.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.firebase.performGoogleAuthentication
import com.unh.personal_health_buddy.firebase.performSignUp
import com.unh.personal_health_buddy.R

@Composable
fun SignUpScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password")
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .toggleable(value = isChecked, onValueChange = { isChecked = it }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("I agree to the Terms and Conditions")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isChecked) {
                    performSignUp(
                        email.value,
                        password.value,
                        mutableStateOf(false),
                        mutableStateOf(false),
                        navController
                    )
                    navController.navigate("sign-in") {
                        popUpTo("sign-up") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("OR")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                if (googleSignInClient != null && launcher != null) {
                    performGoogleAuthentication(
                        launcher,
                        context,
                        context.getString(R.string.default_web_client_id)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Sign Up with Google")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Already have an account? Sign In",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("sign-in") {
                    popUpTo("sign-up") { inclusive = true }
                }
            }
        )
    }
}
