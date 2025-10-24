package com.unh.personal_health_buddy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.unh.personal_health_buddy.database.Article
import db

@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val uid = FirebaseFirestore.getInstance().collection("users").document().id

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Article", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.padding(top = 10.dp)
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.padding(top = 10.dp)
        )

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    val article = Article(
                        uid = uid,
                        name = title,
                        articles = listOf(content),
                    )

                    db.collection("Articles")
                        .add(article)
                        .addOnSuccessListener {
                            android.util.Log.d("Firestore", "Article saved with ID: ${it.id}")
                            title = ""
                            content = ""
                            focusManager.clearFocus()
                        }
                        .addOnFailureListener { e ->
                            android.util.Log.e("Firestore", "Error saving Article", e)
                        }
                }
            },
            modifier = Modifier.padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("Save Article")
        }
    }
}
