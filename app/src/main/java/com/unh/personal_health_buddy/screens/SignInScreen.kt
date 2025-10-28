package com.unh.personal_health_buddy.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.R
import com.unh.personal_health_buddy.firebase.performGoogleAuthentication
import com.unh.personal_health_buddy.firebase.performSignIn

@Composable
fun SignInScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?,
    launcher: ActivityResultLauncher<Intent>
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
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Forgot Password?",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 8.dp)
                .clickable { navController.navigate("reset-password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("OR")
        Spacer(modifier = Modifier.height(12.dp))


        OutlinedButton(
            onClick = {
                performGoogleAuthentication(
                    launcher = launcher,
                    context = context
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text("Sign in with Google")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Don't have an account? Sign Up",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { navController.navigate("sign-up") }
        )
    }
    Log.d("SignInScreen", "Sign in screen displayed")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSignInScreen() {
    val navController = rememberNavController()
    val fakeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    SignInScreen(
        navController = navController,
        googleSignInClient = null,
        launcher = fakeLauncher
    )
}
