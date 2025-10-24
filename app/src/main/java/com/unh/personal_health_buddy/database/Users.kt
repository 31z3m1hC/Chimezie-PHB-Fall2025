package com.unh.personal_health_buddy.database
import com.google.firebase.firestore.ServerTimestamp
import java.security.Timestamp
import java.util.Date


data class User(
    val userId: String = "",
    val name: String = "",
    val gender: String = "",
    val email: String = "",
    val avatar: String = ""
)


data class HealthInfo(
    val bodyMassIndex: List<String> = listOf(),
    val bloodInformation: String = "",
    val notifications: List<String>? = null,
    val reports: List<String>? = null,
    val appointments: List<String>? = null
)


data class Chat(
    val avatarUrl: String = "",
    val chats: List<String>? = null,
    @ServerTimestamp
    val postedTime: Date? = null
)

data class Contact(
    val name: String = "",
    val phone: List<String> = listOf(),
    val relationship: String = ""
)

data class Article(
    val uid: String = "",
    val name: String = "",
    val articles: List<String> = listOf(),
    @ServerTimestamp
    val postedTime: Date? = null
)


data class FAQ(
    val uid: String = "",
    val name: String = "",
    val questions: List<String>? = null,
    val answers: List<String>? = null,
    @ServerTimestamp
    val postedTime: Date? = null
)