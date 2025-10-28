package com.unh.personal_health_buddy.chats

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.database.Chats
import com.unh.personal_health_buddy.database.FirestoreHelper
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun MessageListScreen(
    navController: NavController,
    cid: String,
    currentUserName: String = "You"
) {
    val scope = rememberCoroutineScope()
    val authUser = FirebaseAuth.getInstance().currentUser
    val userId = authUser?.uid ?: return // Exit early if not authenticated

    var messages by remember { mutableStateOf(listOf<Chats>()) }
    var inputValue by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        try {
            messages = FirestoreHelper.readChats()
        } catch (e: Exception) {
            Log.e("MessageListScreen", "Failed to load messages", e)
        }
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(messages) { chat ->
                Text("${chat.senderName}: ${chat.message}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = inputValue,
                onValueChange = { inputValue = it },
                placeholder = { Text("Type a message...") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    scope.launch {
                        sendMessage(currentUserName, inputValue) { chat ->
                            messages = messages + chat
                            inputValue = ""
                        }
                    }
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        sendMessage(currentUserName, inputValue) { chat ->
                            messages = messages + chat
                            inputValue = ""
                        }
                    }
                },
                enabled = inputValue.isNotBlank(),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Send")
            }
        }
    }
}

private suspend fun sendMessage(
    senderName: String,
    messageText: String,
    onSuccess: (Chats) -> Unit
) {
    if (messageText.isBlank()) return

    val authUser = FirebaseAuth.getInstance().currentUser
    val userId = authUser?.uid ?: run {
        Log.e("MessageInput", "No authenticated user found")
        return
    }

    val chat = Chats(
        chatId = UUID.randomUUID().toString(),
        senderName = senderName,
        message = messageText,
        chatTime = Timestamp.now(),
        avatarURL = ""
    )

    try {
        FirestoreHelper.writeChat(chat)
        onSuccess(chat)
    } catch (e: Exception) {
        Log.e("MessageInput", "Failed to send message", e)
    }
}

@Preview(showBackground = true)
@Composable
fun MessageListScreenPreview() {
    val navController = rememberNavController()
    MessageListScreen(navController = navController, cid = "preview_cid")
}
