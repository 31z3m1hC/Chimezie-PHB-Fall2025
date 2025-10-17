package com.unh.personal_health_buddy.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.NavigationItem
import com.unh.personal_health_buddy.BottomBar

@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* your FAB action */ },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White,
                modifier = Modifier.offset(y =(-100).dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (currentRoute) {
                NavigationItem.Home.route -> HomeScreen(navController)
                NavigationItem.Map.route -> MapScreen()
                NavigationItem.Notification.route -> NotificationScreen()
                else -> HomeScreen(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("welcome")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "welcome"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Home Screen", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun MapScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Map Screen", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun NotificationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Notification Screen", style = MaterialTheme.typography.headlineSmall)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
