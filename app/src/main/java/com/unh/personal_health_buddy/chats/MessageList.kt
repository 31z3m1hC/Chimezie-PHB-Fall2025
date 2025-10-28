package com.unh.personal_health_buddy.chats

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.getstream.chat.android.ui.feature.messages.list.adapter.MessageListItem
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModelFactory


@Composable
fun MessageList(
    navController: NavController,
    factory: MessageListViewModelFactory,
    modifier: Modifier = Modifier,
    messageListViewModel: MessageListViewModel = viewModel(factory = factory),
) {
    val state by messageListViewModel.state.observeAsState()
    val messageState = state ?: return

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (messageState) {
            is MessageListViewModel.State.Loading    -> CircularProgressIndicator()
            is MessageListViewModel.State.NavigateUp -> navController.popBackStack()
            is MessageListViewModel.State.Result     -> {
                val messageItems = messageState.messageListItem.items
                    .filterIsInstance<MessageListItem.MessageItem>()
                    .filter { it.message.text.isNotBlank() }
                    .asReversed()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = true
                ) {
                    items(messageItems) { message ->
                        MessageCard(message)
                    }
                }
            }
        }
    }
    Log.d("MessageList", "Message list screen displayed")
}
