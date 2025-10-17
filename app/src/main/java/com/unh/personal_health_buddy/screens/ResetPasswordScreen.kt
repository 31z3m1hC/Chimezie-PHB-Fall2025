package com.unh.personal_health_buddy.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.R
import com.unh.personal_health_buddy.firebase.performResetPassword

@Composable
fun ResetPasswordScreen(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val emailErrorState = remember { mutableStateOf(false) }
    val resetMessage = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = {
                if (navController.currentDestination?.route != "sign-in") {
                    navController.navigate("sign-in")
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back to Sign In"
                )
            }
        }

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp, top = 16.dp)
        )


        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
                emailErrorState.value = false
            },
            label = { Text("Enter your email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            isError = emailErrorState.value,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.9f)
        )

        if (emailErrorState.value) {
            Text(
                text = "Invalid email format. Please enter a valid email.",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                performResetPassword(email.value, emailErrorState)
                if (!emailErrorState.value) {
                    resetMessage.value =
                        "If this email is registered, a password reset link has been sent."
                    email.value = ""
                    focusManager.clearFocus()
                } else {
                    resetMessage.value = "Please enter a valid email address."
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
            Text(text = "Send Reset Password")
        }

        if (resetMessage.value.isNotEmpty()) {
            Text(
                text = resetMessage.value,
                color = if (resetMessage.value.contains("sent", ignoreCase = true)) Color.Green else Color.Red,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.9f),
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Remember your password? ")
            Text(
                text = "Sign in",
                color = colorResource(id = R.color.teal_700),
                modifier = Modifier.clickable {
                    navController.navigate("sign-in")
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(rememberNavController())
}
