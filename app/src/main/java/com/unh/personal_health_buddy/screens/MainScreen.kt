package com.unh.personal_health_buddy.screens
import AccountFormScreen
import EmergencyContactScreen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.BottomBar
import com.unh.personal_health_buddy.screens.ProfileScreen

import com.unh.personal_health_buddy.screens.profileItems


@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Show BottomBar only on home or profile
            if (currentRoute == "profile" || currentRoute == "home") {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                "profile" -> ProfileScreen(
                    navController = navController,
                    items = profileItems,
                    currentRoute = currentRoute
                )

                "account-form" -> AccountFormScreen(navController)

                "chats" -> MessageListScreen(navController = navController, cid = "preview_cid")
                "notification" -> NotificationScreen(navController)
                "faqs" -> FAQScreen(navController)
                "logout" -> LogoutScreen(navController)
                "appointment" -> AppointmentScreen(navController)
                "faqs" -> FAQScreen(navController)
                "logout" -> LogoutScreen(navController)
                "home" -> HomeScreen(navController)
                "map" -> MapScreen(navController)
               // "user-account" -> UserAccount(navController = navController)
                "emergency-contacts" -> EmergencyContactScreen(navController)
                else -> WelcomeScreen(navController)
            }
        }
    }
}

@Composable
fun LogoutScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "You have logged out successfully.", style = MaterialTheme.typography.headlineSmall)
    }

    BackButton(navController = navController, returnRoute = "profile")
}

@Composable
fun AppointmentScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Appointment Screen", style = MaterialTheme.typography.headlineSmall)
    }
    BackButton(navController = navController, returnRoute = "profile")
}

@Composable
fun FAQScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "FAQs Screen", style = MaterialTheme.typography.headlineSmall)
    }
    BackButton(navController = navController, returnRoute = "profile")
}




@Composable
fun NotificationScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Notification Screen", style = MaterialTheme.typography.headlineSmall)
    }
    BackButton(navController = navController, returnRoute = "home")
}

@Composable
fun MapScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Map Screen", style = MaterialTheme.typography.headlineSmall)
    }
    BackButton(navController = navController, returnRoute = "home")
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(navController: NavController) {
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { /* your FAB action */ },
//                containerColor = MaterialTheme.colorScheme.tertiary,
//                contentColor = Color.White,
//                modifier = Modifier.offset(y = (-10).dp),
//                shape = RoundedCornerShape(50)
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Add")
//            }
//        },
//        bottomBar = { BottomBar(navController = navController) }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Home Screen", style = MaterialTheme.typography.headlineSmall)
//        }
//    }
//}

@Composable
fun BackButton(navController: NavController, returnRoute: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 32.dp),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = { navController.navigate(returnRoute) }) {
            Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewMainScreen() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
