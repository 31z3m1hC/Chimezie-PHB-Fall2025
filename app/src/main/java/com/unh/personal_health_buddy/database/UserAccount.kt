package com.unh.personal_health_buddy.database

import com.google.firebase.Timestamp

data class User(
    val id: String = "",
    val name: String = "",
    val gender: Gender = Gender.MALE,
    val email: String = "",
    val avatar: String = ""
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

data class Report(
    val title: String,
    val content: String,
    val date: Timestamp,
)

data class Appointment(
    val date: String,
    val time: Timestamp,
)

data class Notification(
    val message: List<String>? = emptyList(),
    val date: String,
)

data class Articles(
    val title: String,
    val content: String,
    val date: String,
)

data class FAQs(
    val question: String,
    val answer: String,
    val date: Timestamp,
)

data class EmergencyContact(
    val contactId: String = "",
    val name: String = "",
    val phone: List<String> = emptyList(),
    val relationship: String = ""
)

data class Quantity(
    val value: String = "",
)

data class HealthInfo(
    val bmi: Map<String, Quantity> = mapOf(
        "height" to Quantity(""),
        "weight" to Quantity(""),
        "calculatedBMI" to Quantity("")
    ),
    val bloodInfo: String = "",
    val notifications: List<String>? = emptyList(),
    val reports: List<String>? = emptyList(),
    val appointments: List<String>? = emptyList()
)
