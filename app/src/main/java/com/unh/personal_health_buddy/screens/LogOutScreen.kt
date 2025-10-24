package com.unh.personal_health_buddy.screens

import android.R.attr.onClick
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.R

fun performLogOut(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    Log.d("LogOut", "User signed out")
    navController.navigate("welcome") {
        popUpTo(0)
    }
}

@Composable
fun LogOutScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Are you sure you want to log out?",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { performLogOut(navController) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
                )
            ) {
                Text("Log Out")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.purple_500),
                    contentColor = Color.White
                )
            ) {
                Text("Cancel")
            }
        }
    }
    Log.d("LogOutScreen", "Log out screen displayed")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogOutScreen() {
    val navController = NavController(LocalContext.current)
    LogOutScreen(navController)
}

