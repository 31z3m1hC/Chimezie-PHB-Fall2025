import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.ui.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModelFactory

@Composable
fun MessageInput(
    factory: MessageListViewModelFactory? = null,
    messageComposerViewModel: MessageComposerViewModel? = null,
    isPreview: Boolean = false
) {
    var inputValue by remember { mutableStateOf("") }

    fun sendMessage() {
        if (inputValue.isNotBlank()) {
            if (!isPreview && messageComposerViewModel != null) {
                // Real app
                // messageComposerViewModel.sendMessage(...)
            }
            inputValue = ""
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(1f),
                value = inputValue,
                onValueChange = { inputValue = it },
                placeholder = { Text("Type a message...") },
                enabled = true,
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions(onSend = { sendMessage() })
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { sendMessage() },
                enabled = inputValue.isNotBlank(),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Send")
            }
        }
    }
    Log.d("MessageInput", "Message input displayed")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MessageInputPreview() {
    MessageInput(factory = null, messageComposerViewModel = null, isPreview = true)
}
