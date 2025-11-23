package com.unh.personal_health_buddy.database

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import java.util.UUID


data class User(
    val firstname: String = "",
    val lastname: String = "",
    val dateOfBirth: String = "",
    val homeAddress: String = "",
    val gender: Gender = Gender.OTHER,
    val email: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String? = null,
    val city: String = ""
)


data class HealthInformation(
    val medication: String = "",
    val allergies: String = "",
    val bloodGroup: String = ""
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}


data class ChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)


data class Chats(
    val chatId: String = "",
    val avatarURL: String = "",
    val chatTime: Timestamp = Timestamp.now(),
    val senderName: String = "",
    val message: String = ""
)



data class Prescription(
    val id: String = "",
    val name: String = "",
    val dosage: String = "",
    val frequency: String = "",
    val form: String = "",
    val timeOfDay: String = "",
    val withFood: String = "",
    val notes: String = ""
)


data class Report(
    val title: String = "",
    val content: String = "",
    val date: Timestamp = Timestamp.now()
)


data class Appointment(
    val date: String = "",
    val time: Timestamp = Timestamp.now()
)


data class Notification(
    val message: List<String>? = emptyList(),
    val date: String = ""
)


data class Articles(
    val title: String = "",
    val content: String = "",
    val date: String = ""
)


data class FAQs(
    val question: String = "",
    val answer: String = "",
    val date: Timestamp = Timestamp.now()
)


// Data class for EmergencyContact
data class EmergencyContact(
    val contactId: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val phoneNumber: String = "",
    val relationship: String = ""
)

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val title: String
)