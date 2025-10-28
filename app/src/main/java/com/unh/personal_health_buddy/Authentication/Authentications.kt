package com.unh.personal_health_buddy.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.unh.personal_health_buddy.R
import com.unh.personal_health_buddy.navigation.AppNavigation
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun performSignUp(
    email: String,
    password: String,
    emailErrorState: MutableState<Boolean>,
    passwordErrorState: MutableState<Boolean>,
    navController: NavController,
    context: Context
) {
    if (email.isBlank()) {
        emailErrorState.value = true
        Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        return
    }

    if (password.length < 6) {
        passwordErrorState.value = true
        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        return
    }

    FirebaseAuth.getInstance()
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("sign-in") {
                    popUpTo("sign-up") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
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

    FirebaseAuth.getInstance()
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("home") {
                    popUpTo("sign-in") { inclusive = true }
                }
            } else {
                emailErrorState.value = true
                passwordErrorState.value = true
                Toast.makeText(
                    context,
                    task.exception?.localizedMessage ?: "Sign-in failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        .addOnFailureListener { e -> Log.e("SignIn", "Exception: ", e) }
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

fun createGoogleSignInOptions(googleClientId: String): GoogleSignInOptions {
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(googleClientId)
        .requestEmail()
        .build()
}

fun performGoogleAuthentication(
    launcher: ActivityResultLauncher<Intent>,
    context: Context
) {
    val gso = createGoogleSignInOptions(context.getString(R.string.default_web_client_id))
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
    Log.d("GoogleAuth", "Google authentication started")
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit
): ActivityResultLauncher<Intent> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK || result.data == null) {
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
    activity: Activity
) {
    val gso = createGoogleSignInOptions(activity.getString(R.string.default_web_client_id))
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
    Log.d("SetupAuthentication", "Authentication setup")

    LaunchedEffect(Unit) {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account != null && FirebaseAuth.getInstance().currentUser == null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            try {
                val authResult = FirebaseAuth.getInstance()
                    .signInWithCredential(credential)
                    .await()
                navController.navigate("home") {
                    popUpTo("sign-in") { inclusive = true }
                }
                Log.d("SilentSignIn", "Signed in silently")
            } catch (e: Exception) {
                Log.e("SilentSignIn", "Failed", e)
            }
        }
    }

    AppNavigation(
        navController = navController,
        googleSignInClient = googleSignInClient,
        launcher = launcher
    )
    Log.d("SetupAuthentication", "Authentication setup")
}
