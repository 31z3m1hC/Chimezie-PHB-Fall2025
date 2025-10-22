package com.unh.personal_health_buddy


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

// Logout section code
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    //defining topbar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Successfully logout",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = auth.currentUser?.email ?: "Unknown User",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp, top = 4.dp)
            )

            Button(
                onClick = {
                    auth.signOut()
                    // Navigate to login screen after sign-out
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true } // clear back stack
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoutScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        LogoutScreen(navController = navController)
    }
}
