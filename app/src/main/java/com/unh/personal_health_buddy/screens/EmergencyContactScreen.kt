package com.unh.personal_health_buddy.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {

            // Back button at top-left
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, bottom = 32.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(
                    onClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate("home") {
                                launchSingleTop = true
                            }
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back to Home"
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "Emergency Contacts",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }

            }

            // Emergency contacts content
            EmergencyContactsDisplayScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp) // Push content below back button
            )
        }
    }
    Log.d("EmergencyContactScreen", "Emergency contact screen displayed")
}

@Composable
fun EmergencyContactsDisplayScreen(modifier: Modifier = Modifier) {
    val contacts = listOf(
        "Nabin Bamma" to "(555) 123-4567",
        "Raj Lama" to "(555) 987-6543",
        "Chimezie Onwuegbuchulem" to "(555) 246-8101"
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        contacts.forEach { (name, number) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Contact Icon",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(32.dp)
                    )

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = number,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
    Log.d("EmergencyContactsDisplayScreen", "Emergency contacts displayed")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmergencyContactScreen() {
    EmergencyContactScreen(rememberNavController())
}
