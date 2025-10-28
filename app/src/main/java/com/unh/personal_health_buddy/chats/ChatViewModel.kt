package com.unh.personal_health_buddy.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.unh.personal_health_buddy.database.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel(
    private val conversationId: String,
    private val currentUserId: String
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private var listenerRegistration: ListenerRegistration? = null

    init {
        listenForMessages()
    }

    private fun listenForMessages() {
        listenerRegistration = db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val list = snapshot.documents.mapNotNull { it.toObject(ChatMessage::class.java)?.copy(id = it.id) }
                _messages.value = list
            }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val msg = hashMapOf(
            "conversationId" to conversationId,
            "senderId" to currentUserId,
            "text" to text,
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .add(msg)
            .addOnFailureListener { Log.e("ChatViewModel", "send failed", it) }
    }

    override fun onCleared() {
        listenerRegistration?.remove()
        super.onCleared()
    }
}
