package com.unh.personal_health_buddy.screens

import android.R.attr.checked
import android.R.attr.label
import android.R.attr.top
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.R
import androidx.navigation.compose.rememberNavController


@Composable
fun SignUp(navController: NavHostController? = null) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
                           .padding(top = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
                                .padding(top = 10.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController?.navigate("welcome") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Welcome"
                )
            }
        }

        Text(
            text = "Sign Up",
            modifier = Modifier.padding(bottom = 30.dp, start = 24.dp, end = 24.dp, top = 8.dp),
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 30.dp, start = 24.dp, end = 24.dp, top = 8.dp),
            label = { Text(stringResource(R.string.user_name)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            value = username.value,
            onValueChange = { username.value = it }
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 30.dp)
                                .padding(start = 24.dp, end = 24.dp, top = 8.dp),
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            value = email.value,
            onValueChange = { email.value = it }
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 30.dp, start = 24.dp, end = 24.dp, top = 8.dp),
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = password.value,
            onValueChange = { password.value = it }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp,start = 24.dp, end = 24.dp, top = 8.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                modifier = Modifier.padding(start = 30.dp, bottom = 30.dp),
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )

            Text(
                text = "I agree to the health terms of services and privacy policy",
                modifier = Modifier
                    .padding(start = 8.dp, end = 24.dp, bottom = 24.dp)
                    .weight(1f),
                textAlign = TextAlign.Start
            )

        }

        Button(
            onClick = { /* handle sign-up */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp)
                .width(50.dp),
            border = BorderStroke(1.dp, colorResource(id = R.color.purple_500)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.button_sign_up),
                color = Color.White
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account? ")

            Text(
                text = "Sign in",
                color = colorResource(id = R.color.teal_700),
                modifier = Modifier.clickable {
                    navController?.navigate("sign-in")
                }
            )
        }

    }

}



@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUp(rememberNavController())
}


