package com.unh.personal_health_buddy.firebase

import AppNavigation
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.unh.personal_health_buddy.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await




fun isValidUsername(name: String): Boolean {
    return name.isNotBlank() &&
            name.any { it.isLetter() } &&
            name.all { it.isLetterOrDigit() }
}


fun performResetPassword(
    email: String,
    emailErrorState: MutableState<Boolean>,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    emailErrorState.value = !Patterns.EMAIL_ADDRESS.matcher(email).matches()

    if (emailErrorState.value) return

    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
                Log.d("ResetPassword", "Reset link sent successfully")
            } else {
                onFailure(task.exception ?: Exception("Unknown error"))
            }
        }
}




// ---------------- EMAIL AUTH --------------------

fun isValidEmail(email: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean =
    password.length >= 6 &&
            password.any { it.isLetterOrDigit() } &&
            password.any { !it.isLetterOrDigit() }

fun performSignUp(
    email: String,
    password: String,
    emailErrorState: MutableState<Boolean>,
    passwordErrorState: MutableState<Boolean>,
    navController: NavController
) {
    emailErrorState.value = !isValidEmail(email)
    passwordErrorState.value = !isValidPassword(password)

    if (!emailErrorState.value && !passwordErrorState.value) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("sign-in")
                    Log.d("SignUp", "User created successfully")
                } else {
                    Log.e("SignUp", "Sign up failed", task.exception)
                }
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
    emailErrorState.value = email.isBlank()
    passwordErrorState.value = password.isBlank()

    if (emailErrorState.value || passwordErrorState.value) return

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("home") { popUpTo("sign-in") { inclusive = true } }
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
}

// ---------------- LOGOUT --------------------

fun performLogOut(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    Log.d("LogOut", "User signed out")
    navController.navigate("sign-in")
}

// ---------------- GOOGLE AUTH --------------------

fun createGoogleSignInOptions(googleClientId: String): GoogleSignInOptions =
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(googleClientId)
        .requestEmail()
        .build()


@Composable
fun rememberFirebaseAuthLauncher(
    onSignInSuccess: () -> Unit,
    onSignInFailure: (Exception) -> Unit
): ActivityResultLauncher<Intent> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK || result.data == null) {
            onSignInFailure(IllegalStateException("Google Sign-In canceled"))
            return@rememberLauncherForActivityResult
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken.isNullOrBlank()) {
                onSignInFailure(IllegalStateException("Missing ID token"))
                return@rememberLauncherForActivityResult
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            scope.launch {
                try {
                    FirebaseAuth.getInstance().signInWithCredential(credential).await()
                    onSignInSuccess()
                } catch (e: Exception) {
                    onSignInFailure(e)
                }
            }
        } catch (e: ApiException) {
            onSignInFailure(e)
        }
    }
}



//@Composable
//fun rememberFirebaseAuthLauncher(
//    onSignInSuccess: () -> Unit,
//    onSignInFailure: (Exception) -> Unit
//): ActivityResultLauncher<Intent> {
//    val scope = rememberCoroutineScope()
//    return rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode != Activity.RESULT_OK || result.data == null) {
//            onSignInFailure(IllegalStateException("Google Sign-In canceled"))
//            return@rememberLauncherForActivityResult
//        }
//
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)
//            val idToken = account.idToken
//            if (idToken.isNullOrBlank()) {
//                onSignInFailure(IllegalStateException("Missing ID token"))
//                return@rememberLauncherForActivityResult
//            }
//
//            val credential = GoogleAuthProvider.getCredential(idToken, null)
//            scope.launch {
//                try {
//                    FirebaseAuth.getInstance().signInWithCredential(credential).await()
//                    onSignInSuccess()
//                } catch (e: Exception) {
//                    onSignInFailure(e)
//                }
//            }
//        } catch (e: ApiException) {
//            onSignInFailure(e)
//        }
//    }
//}

//fun performGoogleAuthentication(
//    launcher: ActivityResultLauncher<Intent>,
//    googleSignInClient: GoogleSignInClient,
//    context: Context,
//    onSuccessNav: () -> Unit,
//    onFailureToast: (String) -> Unit
//) {
//    try {
//        launcher.launch(googleSignInClient.signInIntent)
//    } catch (e: Exception) {
//        Log.e("GoogleAuth", "Failed to launch Google Sign-In", e)
//        onFailureToast(e.message ?: "Google Sign-In failed")
//        Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
//    }
//}

fun performGoogleAuthentication(
    launcher: ActivityResultLauncher<Intent>,
    context: Context,
    onSuccessNav: () -> Unit,
    onFailureToast: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val existingUser = auth.currentUser

    // If already signed in, just navigate
    if (existingUser != null && existingUser.email != null) {
        val emails = arrayOf(existingUser.email!!)
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Choose verified account")
            .setItems(emails) { _, _ ->
                onSuccessNav()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
        return
    }

    // Launch Google Sign-In
    try {
        launcher.launch((GoogleSignIn.getClient(context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        ).signInIntent))
    } catch (e: Exception) {
        onFailureToast(e.message ?: "Google Sign-In failed")
    }
}


// ---------------- SETUP AUTH --------------------

@Composable
fun SetupAuthentication(navController: NavHostController, activity: Activity) {
    val gso = createGoogleSignInOptions(activity.getString(R.string.default_web_client_id))
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    val launcher = rememberFirebaseAuthLauncher(
        onSignInSuccess = {
            navController.navigate("home") { popUpTo("sign-in") { inclusive = true } }
        },
        onSignInFailure = { e ->
            Log.e("Auth", "Google Sign-In failed", e)
        }
    )

    AppNavigation(
        navController = navController,
        googleSignInClient = googleSignInClient,
        launcher = launcher
    )
    Log.d("SetupAuthentication", "Authentication setup completed")
}