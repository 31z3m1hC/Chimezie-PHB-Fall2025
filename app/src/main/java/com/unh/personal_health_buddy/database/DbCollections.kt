package com.unh.personal_health_buddy.database

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser


data class User(
    val uid: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val city: String = "",
    val dateOfBirth: String = "",
    val homeAddress: String = "",
    val gender: Gender = Gender.MALE,  // or default
    val email: String = "",
    val medication: String = "",
    val allergies: String? = null,
    val phoneNumber: String = "",
    val profileImageUrl: String? = null // <-- Add this
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


data class EmergencyContact(
    val contactId: String = "",
    val Contactname: String = "",
    val phone: List<String> = emptyList(),
    val relationship: String = ""
)
