package com.unh.personal_health_buddy.chats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.chat.android.ui.feature.messages.list.adapter.MessageListItem

@Composable
fun MessageCard(messageItem: MessageListItem.MessageItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = if (messageItem.isMine) Alignment.End else Alignment.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = cardShapeFor(messageItem),
            colors = CardDefaults.cardColors(
                containerColor = if (messageItem.isMine)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = messageItem.message.text,
                color = if (messageItem.isMine)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSecondary
            )
        }

        if (!messageItem.isMine) {
            Text(
                text = messageItem.message.user.name,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun cardShapeFor(message: MessageListItem.MessageItem): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return if (message.isMine) {
        roundedCorners.copy(bottomEnd = CornerSize(0))
    } else {
        roundedCorners.copy(bottomStart = CornerSize(0))
    }
}
