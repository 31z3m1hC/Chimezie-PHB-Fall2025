package com.unh.personal_health_buddy

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.BuildConfig



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
                    Log.d("SignUp", "createUserWithEmail:success")
                    navController.navigate("sign-in")
                } else {
                    Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SignUp", "Exception during sign-up", exception)
            }
    }
}


fun performLogOut(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    auth.signOut()
    Log.d("LogOut", "User signed out successfully")
    navController.navigate("sign-in")
}


fun performSignIn(
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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignIn", "signInWithEmail:success")
                    navController.navigate("home")
                } else {
                    Log.w("SignIn", "signInWithEmail:failure", task.exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SignIn", "Exception during sign-in", exception)
            }
    }
}


fun performResetPassword(
    email: String,
    emailErrorState: MutableState<Boolean>
) {
    val isEmailValid = isValidEmail(email)
    emailErrorState.value = !isEmailValid

    if (isEmailValid) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ResetPassword", "Password reset email sent successfully")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ResetPassword", "Failed to send reset email", exception)
            }
    }
}

fun isValidEmail(email: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean =
    password.length >= 6

fun performGoogleAuthentication(
    launcher: ManagedActivityResultLauncher<Intent, Instrumentation.ActivityResult>,
    context: Context
) {
    val token = BuildConfig.GOOGLE_CLIENT_ID  // <- correct BuildConfig
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
}
