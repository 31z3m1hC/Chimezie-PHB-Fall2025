package com.unh.personal_health_buddy.firebase

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.unh.personal_health_buddy.navigation.AppNavigation
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.unh.personal_health_buddy.R


fun performSignUp(
    email: String,
    password: String,
    emailErrorState: MutableState<Boolean>,
    passwordErrorState: MutableState<Boolean>,
    navController: NavController
) {
    val isEmailValid = isValidEmail(email)
    val isPasswordValid = isValidPassword(password)
    emailErrorState.value = !isEmailValid
    passwordErrorState.value = !isPasswordValid

    if (isEmailValid && isPasswordValid) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignUp", "User created successfully")
                    navController.navigate("sign-in")
                } else {
                    Log.w("SignUp", "Sign up failed", task.exception)
                }
            }
            .addOnFailureListener { e -> Log.e("SignUp", "Exception: ", e) }
    }
}

fun performSignIn(
    email: String,
    password: String,
    emailErrorState: MutableState<Boolean>,
    passwordErrorState: MutableState<Boolean>,
    context: Context,
    navController: NavHostController
) {
    if (email.isBlank()) {
        emailErrorState.value = true
        return
    }
    if (password.isBlank()) {
        passwordErrorState.value = true
        return
    }

    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("home") {
                    popUpTo("sign-in") { inclusive = true }
                }
            } else {
                emailErrorState.value = true
                passwordErrorState.value = true
                android.widget.Toast.makeText(
                    context,
                    task.exception?.localizedMessage ?: "Sign-in failed",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
}

fun performResetPassword(email: String, emailErrorState: MutableState<Boolean>) {
    val isEmailValid = isValidEmail(email)
    emailErrorState.value = !isEmailValid

    if (isEmailValid) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) Log.d("ResetPassword", "Reset email sent")
            }
            .addOnFailureListener { e -> Log.e("ResetPassword", "Failed to send reset email", e) }
    }
}

fun performLogOut(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    Log.d("LogOut", "User signed out")
    navController.navigate("sign-in")
}


fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
fun isValidPassword(password: String): Boolean = password.length >= 6


fun performGoogleAuthentication(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context,
    googleClientId: String
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(googleClientId)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return androidx.activity.compose.rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != android.app.Activity.RESULT_OK || result.data == null) {
            onAuthError(IllegalStateException("Google Sign-In canceled"))
            return@rememberLauncherForActivityResult
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken.isNullOrBlank()) {
                onAuthError(IllegalStateException("Missing ID token"))
                return@rememberLauncherForActivityResult
            }
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            scope.launch {
                try {
                    val authResult = FirebaseAuth.getInstance()
                        .signInWithCredential(credential)
                        .await()
                    onAuthComplete(authResult)
                } catch (e: Exception) {
                    onAuthError(e)
                }
                Log.d("Auth", "User: ${FirebaseAuth.getInstance().currentUser?.uid}")
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}


@Composable
fun SetupAuthentication(
    navController: NavHostController,
    activity: android.app.Activity
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = {
            navController.navigate("home") {
                popUpTo("sign-in") { inclusive = true }
            }
        },
        onAuthError = { e ->
            Log.e("Auth", "Google Sign-In failed", e)
        }
    )

    AppNavigation(
        navController = navController,
        googleSignInClient = googleSignInClient,
        launcher = launcher
    )
}
