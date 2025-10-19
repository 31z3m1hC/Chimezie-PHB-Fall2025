package com.unh.personal_health_buddy.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.R


@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(260.dp)
                .padding(top = 2.dp, bottom = 8.dp)
        )

        Text(
            text = "Personal Health\nBuddy",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.purple_700),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Text(
            text = "Let's get started!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Text(
            text = "Your health, all in one place",
            fontSize = 20.sp,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        )

        Button(
            onClick = { navController.navigate("sign-in") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(),
            border = BorderStroke(2.dp, colorResource(id = R.color.purple_500)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("sign-up") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(),
            //border = BorderStroke(2.dp, colorResource(id = R.color.teal_700)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White

            )
        ) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    Log.d("WelcomeScreen", "Welcome screen displayed")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    val navController = rememberNavController()
    WelcomeScreen(navController = navController)
}