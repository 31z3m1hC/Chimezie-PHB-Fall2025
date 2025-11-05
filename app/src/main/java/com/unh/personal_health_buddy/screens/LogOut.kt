package com.unh.personal_health_buddy.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unh.personal_health_buddy.R

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,

        title = {
            Text(
                text = "Want to log out?",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },

//        text = {
//            Text(
//                text = "Are you sure you want to log out?",
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center
//            )
//        },

        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.purple_500),
                        contentColor = Color.White
                    )
                ) {
                    Text("Logout")
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.purple_500)
                    )
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LogoutConfirmationDialogPreview() {
    LogoutConfirmationDialog(onConfirm = {}, onCancel = {})
}
