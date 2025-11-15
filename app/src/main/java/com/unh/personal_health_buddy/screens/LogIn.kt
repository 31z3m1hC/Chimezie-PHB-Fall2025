package com.unh.personal_health_buddy.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.unh.personal_health_buddy.R
import com.unh.personal_health_buddy.firebase.performSignIn

//@Composable
//fun SignInScreen(
//    navController: NavHostController,
//    googleSignInClient: GoogleSignInClient?,
//    launcher: ActivityResultLauncher<Intent> // unused for One Tap now
//) {
//    val context = LocalContext.current
//    val email = remember { mutableStateOf("") }
//    val password = remember { mutableStateOf("") }
//    val emailErrorState = remember { mutableStateOf(false) }
//    val passwordErrorState = remember { mutableStateOf(false) }
//    var passwordVisible by remember { mutableStateOf(false) }
//
//    // ---------------- One Tap launcher ----------------
//    val oneTapLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            try {
//                val credential: SignInCredential = Identity.getSignInClient(context)
//                    .getSignInCredentialFromIntent(result.data)
//                val idToken = credential.googleIdToken
//                if (idToken != null) {
//                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                    FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                navController.navigate("home") { popUpTo("sign-in") { inclusive = true } }
//                            } else {
//                                Toast.makeText(
//                                    context,
//                                    task.exception?.localizedMessage ?: "Sign-In failed",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                }
//            } catch (e: Exception) {
//                Toast.makeText(context, "One Tap error: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 16.dp),
//            contentAlignment = Alignment.TopStart
//        ) {
//            IconButton(onClick = { navController.navigate("welcome") }) {
//                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(50.dp))
//
//        Text(
//            text = "Sign In",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            isError = emailErrorState.value,
//            label = { Text("Email") },
//            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth(0.9f)
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        OutlinedTextField(
//            value = password.value,
//            onValueChange = { password.value = it },
//            isError = passwordErrorState.value,
//            label = { Text("Password") },
//            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
//            trailingIcon = {
//                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
//                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                    Icon(imageVector = icon, contentDescription = "Toggle Password")
//                }
//            },
//            singleLine = true,
//            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth(0.9f)
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Text(
//            text = "Forgot Password?",
//            fontSize = 14.sp,
//            color = MaterialTheme.colorScheme.primary,
//            textAlign = TextAlign.End,
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .padding(top = 8.dp)
//                .clickable { navController.navigate("reset-password") }
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//                performSignIn(
//                    email.value,
//                    password.value,
//                    emailErrorState,
//                    passwordErrorState,
//                    context,
//                    navController
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = colorResource(id = R.color.purple_500),
//                contentColor = Color.White
//            )
//        ) {
//            Text("Sign In")
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//        Text("OR")
//        Spacer(modifier = Modifier.height(12.dp))
//
//        OutlinedButton(
//            onClick = {
//                // ---------------- Google One Tap ----------------
//                val auth = FirebaseAuth.getInstance()
//                val existingUser = auth.currentUser
//
//                if (existingUser != null && existingUser.email != null) {
//                    // Already signed in
//                    val emails = arrayOf(existingUser.email!!)
//                    AlertDialog.Builder(context)
//                        .setTitle("Choose verified account")
//                        .setItems(emails) { _, _ ->
//                            navController.navigate("home") { popUpTo("sign-in") { inclusive = true } }
//                        }
//                        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
//                        .show()
//                    return@OutlinedButton
//                }
//
//                // Launch One Tap
//                val oneTapClient: SignInClient = Identity.getSignInClient(context)
//                val signInRequest = BeginSignInRequest.builder()
//                    .setGoogleIdTokenRequestOptions(
//                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                            .setSupported(true)
//                            .setServerClientId(context.getString(R.string.default_web_client_id))
//                            .setFilterByAuthorizedAccounts(false)
//                            .build()
//                    )
//                    .setAutoSelectEnabled(true)
//                    .build()
//
//                oneTapClient.beginSignIn(signInRequest)
//                    .addOnSuccessListener { result ->
//                        try {
//                            val intentSenderRequest =
//                                androidx.activity.result.IntentSenderRequest.Builder(result.pendingIntent.intentSender)
//                                    .build()
//                            oneTapLauncher.launch(intentSenderRequest)
//                        } catch (e: Exception) {
//                            Toast.makeText(context, "Failed to launch One Tap: ${e.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(context, "Google One Tap failed: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//            },
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .height(50.dp),
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Image(
//                    painter = painterResource(id = R.drawable.google),
//                    contentDescription = "Google Icon",
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Sign in with Google")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Text(
//            text = "Don't have an account? Sign Up",
//            fontSize = 14.sp,
//            color = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.clickable { navController.navigate("sign-up") }
//        )
//    }
//
//    Log.d("SignInScreen", "Sign in screen displayed")
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewSignInScreen() {
//    val navController = rememberNavController()
//    val fakeLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { }
//
//    SignInScreen(
//        navController = navController,
//        googleSignInClient = null,
//        launcher = fakeLauncher
//    )
//}
@Composable
fun SignInScreen(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient?, // Can be null if not used
    launcher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    val activity = context as Activity
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // State for showing the account selection dialog
    var showAccountDialog by remember { mutableStateOf(false) }
    var existingUserEmail by remember { mutableStateOf<String?>(null) }

    // ---------------- One Tap launcher ----------------
    val oneTapLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential: SignInCredential =
                    Identity.getSignInClient(context).getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (!idToken.isNullOrEmpty()) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("sign-in") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    task.exception?.localizedMessage ?: "Sign-In failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "One Tap error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Account selection dialog
    if (showAccountDialog && existingUserEmail != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAccountDialog = false },
            title = {
                Text(
                    text = "Choose verified account",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            showAccountDialog = false
                            navController.navigate("home") {
                                popUpTo("sign-in") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = existingUserEmail!!,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
                val icon =
                    if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password")
                }
            },
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                // ----------- SAFE Google Sign-In -----------
                val existingUser = FirebaseAuth.getInstance().currentUser
                val userEmail = existingUser?.email

                if (!userEmail.isNullOrEmpty()) {
                    // Show Compose dialog instead of traditional AlertDialog
                    existingUserEmail = userEmail
                    showAccountDialog = true
                    return@OutlinedButton
                }

                // Launch Google One Tap
                val oneTapClient = Identity.getSignInClient(activity)
                val signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(activity.getString(R.string.default_web_client_id))
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .setAutoSelectEnabled(true)
                    .build()

                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        oneTapLauncher.launch(intentSenderRequest)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Google Sign-In failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google")
            }
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
