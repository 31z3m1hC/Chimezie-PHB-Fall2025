package com.unh.personal_health_buddy.screens

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
fun SignUpScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?,
    launcher:  ActivityResultLauncher<Intent>?,

) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }

    var isChecked by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val registrationMessage = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val focusManager = LocalFocusManager.current

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

        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp, top = 16.dp)
        )

        // Username
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(stringResource(R.string.user_name)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
        )

        // Email
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            isError = emailErrorState.value,
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
        )

        // Password
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            isError = passwordErrorState.value,
            label = { Text(stringResource(R.string.password)) },
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
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
        )

        // Terms checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
            Text(
                text = "I agree to the terms and privacy policy",
                modifier = Modifier.padding(start = 8.dp),
                textAlign = TextAlign.Start
            )
        }

        // Registration message
        if (registrationMessage.value.isNotEmpty()) {
            Text(
                text = registrationMessage.value,
                color = if (registrationMessage.value.contains("successful")) Color.Green else Color.Red,
                modifier = Modifier
                    .padding(start = 32.dp, top = 12.dp, bottom = 4.dp)
                    .fillMaxWidth(0.9f),
                textAlign = TextAlign.Start
            )
        }

        // Sign Up button
        Button(
            onClick = {
                emailErrorState.value = email.value.isBlank()
                passwordErrorState.value = password.value.length < 6
                if (emailErrorState.value || passwordErrorState.value || !isChecked) {
                    registrationMessage.value = "Please fill all fields correctly and accept terms."
                    return@Button
                }

                coroutineScope.launch {
                    try {
                        val result = auth.fetchSignInMethodsForEmail(email.value).await()
                        val signInMethods = result.signInMethods
                        if (!signInMethods.isNullOrEmpty()) {
                            registrationMessage.value = "Account already exists. Please sign in."
                            navController.navigate("sign-in") {
                                popUpTo("sign-up") { inclusive = true }
                            }
                        } else {
                            auth.createUserWithEmailAndPassword(email.value, password.value).await()
                            registrationMessage.value = "Registration successful! Redirecting to Sign In..."

                            // Clear fields
                            username.value = ""
                            email.value = ""
                            password.value = ""
                            isChecked = false
                            passwordVisible = false

                            // Delay briefly before navigating
                            kotlinx.coroutines.delay(1500)

                            // Navigate to sign-in screen automatically
                            navController.navigate("sign-in") {
                                popUpTo("sign-up") { inclusive = true }
                            }
                        }
                    } catch (e: Exception) {
                        registrationMessage.value = "Registration failed: ${e.localizedMessage}"
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
            Text(text = stringResource(id = R.string.button_sign_up))
        }

        // Already have account
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ")
            Text(
                text = "Sign in",
                color = colorResource(id = R.color.teal_700),
                modifier = Modifier.clickable { navController.navigate("sign-in") }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController = navController, googleSignInClient = null, launcher = null)
}