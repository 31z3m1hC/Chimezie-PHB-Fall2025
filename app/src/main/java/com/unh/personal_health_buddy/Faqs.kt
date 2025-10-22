package com.unh.personal_health_buddy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

data class FAQItem(val question: String, val answer: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen() {
    val faqList = listOf(
        FAQItem(
            "What is the Personal Health Buddy app?",
            "It is a centralized platform to manage your personal health data, including medical history, BMI calculation, blood group compatibility, and emergency contacts."
        ),
        FAQItem(
            "How do I create my health profile?",
            "You can enter your personal details, blood group, allergies, medications, and emergency contacts from the Account section."
        ),
        FAQItem(
            "How is my data kept secure?",
            "We use Firebase Authentication along with biometric 2FA to protect your information."
        ),
        FAQItem(
            "Can I see nearby health services?",
            "Yes, by enabling location services, the app will show nearby hospitals and clinics on a dynamic Google Map."
        ),
        FAQItem(
            "Will I receive reminders?",
            "Yes, push notifications will remind you about low medications, upcoming appointments, and health checks."
        ),
        FAQItem(
            "Can I upload health documents or photos?",
            "Yes, you can upload profile pictures or securely store images of medical documents."
        ),
        FAQItem(
            "Can I delete my account?",
            "Yes. You can delete your account and all stored data anytime through the Account Settings section."
        ),
        FAQItem(
            "Does the app support biometric login?",
            "Yes, Personal Health Buddy integrates biometric authentication such as fingerprint or face unlock for faster and more secure sign-in."
        ),
        FAQItem(
            "What type of notifications will I get?",
            "Notifications include medication reminders, appointment alerts, health tips, and suggestions to update your health data."
        ),

        FAQItem(
            "Can I use multiple devices with one account?",
            "Yes, once logged in with your Firebase account, your data will sync across all your devices automatically."
        ),
        FAQItem(
            "Will there be AI health chat support?",
            "This feature is planned for future updates, where a chatbot will provide symptom-based feedback."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQs") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(faqList) { item ->
                FAQItemCard(item = item)
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
            .padding(vertical = 4.dp),
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
                    modifier = Modifier.padding(top = 4.dp)
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


