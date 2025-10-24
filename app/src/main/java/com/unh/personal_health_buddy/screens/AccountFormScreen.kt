package com.unh.personal_health_buddy.screens

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.unh.personal_health_buddy.database.User

@Composable
fun UserScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onSubmitClicked: (User) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val uid = FirebaseFirestore.getInstance().collection("users").document().id

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create User", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.padding(top = 10.dp)
        )
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.padding(top = 10.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.padding(top = 10.dp)
        )
        OutlinedTextField(
            value = avatar,
            onValueChange = { avatar = it },
            label = { Text("Avatar URL") },
            modifier = Modifier.padding(top = 10.dp)
        )

        Button(
            onClick = {
                val user = User(
                    userId = uid,
                    name = name,
                    gender = gender,
                    email = email,
                    avatar = avatar,
                )
                onSubmitClicked(user)
                name = ""; gender = ""; email = ""; avatar = ""
                focusManager.clearFocus()
            },
            modifier = Modifier.padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)
        ) {
            Text("Save User")
        }
    }
    Log.d("UserScreen", "User screen created")
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserScreenPreview() {
    UserScreen(onSubmitClicked = {}, navController = NavController(LocalContext.current))
}
