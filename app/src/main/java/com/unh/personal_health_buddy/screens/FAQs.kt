package com.unh.personal_health_buddy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

// Simple FAQ data class
data class FAQItem(val question: String, val answer: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen() {
    // Base FAQ list
    // these question are static and simple
    val faqList = listOf(
        FAQItem("What is the Personal Health Buddy app?", "It is a centralized platform to manage your personal health data."),
        FAQItem("How do I create my health profile?", "Enter personal details, blood group, allergies, medications, and emergency contacts in Account."),
        FAQItem("How is my data kept secure?", "We use Firebase Authentication and biometric options to protect your information."),
        FAQItem("Can I see nearby health services?", "Yes — with location enabled, nearby hospitals/clinics appear on the map."),
        FAQItem("Will I receive reminders?", "Push notifications remind you about meds, appointments, and health checks."),
        FAQItem("Can I upload health documents or photos?", "Yes — upload and store medical documents securely."),
        FAQItem("Can I delete my account?", "Yes — delete your account and its data within Account Settings."),
        FAQItem("Does the app support biometric login?", "Yes — fingerprint and Face Unlock are supported where available.")
    )

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("FAQs") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // FAQ list (scrollable if needed)
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(faqList) { item ->
                    FAQItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun FAQItemCard(item: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.question,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = item.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FAQScreenPreview() {
    MaterialTheme {
        FAQScreen()
    }
}