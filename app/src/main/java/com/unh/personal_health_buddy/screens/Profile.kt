//package com.unh.personal_health_buddy.screens
//
//import LogoutConfirmationDialog
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ExitToApp
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.unh.personal_health_buddy.BottomBar
//import com.unh.personal_health_buddy.R
//
//// ------------------- Profile Items -------------------
//
//sealed class ProfileItem(val title: String, val icon: ImageVector, val route: String) {
//    object Account : ProfileItem("Account", Icons.Filled.Favorite, "user-account")
//    object Appointment : ProfileItem("Appointment", Icons.Filled.Event, "appointment")
//    object EmergencyContacts : ProfileItem("Contacts", Icons.Filled.Contacts, "contacts")
//    object FAQS : ProfileItem("FAQs", Icons.Filled.Chat, "faqs")
//    object Logout : ProfileItem("Logout", Icons.AutoMirrored.Filled.ExitToApp, "logout")
//}
//
//val profileItems = listOf(
//    ProfileItem.Account,
//    ProfileItem.Appointment,
//    ProfileItem.EmergencyContacts,
//    ProfileItem.FAQS,
//    ProfileItem.Logout
//)
//
//// ------------------- Profile Screen -------------------
//
//@Composable
//fun ProfileScreen(
//    navController: NavController,
//    items: List<ProfileItem>,
//    currentRoute: String
//) {
//    var showLogoutDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        bottomBar = { BottomBar(navController = navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .consumeWindowInsets(innerPadding)
//                .safeDrawingPadding()
//                .padding(top = 0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Top Box
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(250.dp)
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
//                        )
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Box(
//                        modifier = Modifier
//                            .background(
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
//                                ),
//                                shape = CircleShape
//                            )
//                            .clip(CircleShape)
//                            .offset(y = (-5).dp)
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture),
//                            contentDescription = "Profile Image",
//                            modifier = Modifier
//                                .border(1.dp, Color.Transparent, CircleShape)
//                                .clip(CircleShape)
//                                .size(110.dp)
//                                .background(Color.Transparent)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "User",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = Color.Black
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Profile Items
//            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
//                items.forEach { item ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp)
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(Color(0xFFF6F8FF))
//                            .padding(horizontal = 1.dp, vertical = 12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFFE0EBFF)),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(item.icon, contentDescription = item.title, tint = Color(0xFF5AA9E6))
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        Text(
//                            text = item.title,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = Color.Black,
//                            modifier = Modifier.weight(1f)
//                        )
//
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowRight,
//                            contentDescription = "Go",
//                            tint = Color.Gray,
//                            modifier = Modifier.clickable {
//                                when (item) {
//                                    is ProfileItem.Account -> {
//                                        navController.navigate("user-account") {
//                                            launchSingleTop = true
//                                        }
//                                    }
//                                    is ProfileItem.Logout -> {
//                                        showLogoutDialog = true
//                                    }
//                                    else -> {
//                                        if (currentRoute != item.route) {
//                                            navController.navigate(item.route) {
//                                                popUpTo(navController.graph.startDestinationId) {
//                                                    saveState = true
//                                                }
//                                                launchSingleTop = true
//                                                restoreState = true
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    if (showLogoutDialog) {
//        LogoutConfirmationDialog(
//            onConfirm = {
//                showLogoutDialog = false
//                navController.navigate("welcome") {
//                    popUpTo(0)
//                }
//            },
//            onCancel = {
//                showLogoutDialog = false
//            }
//        )
//    }
//
//    Log.d("ProfileScreen", "Profile screen displayed")
//}
//
//
//// ------------------- Preview -------------------
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewProfileScreen() {
//    val navController = rememberNavController()
//    ProfileScreen(
//        navController = navController,
//        items = profileItems,
//        currentRoute = ProfileItem.Account.route
//    )
//}

package com.unh.personal_health_buddy.screens

import LogoutConfirmationDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unh.personal_health_buddy.BottomBar
import com.unh.personal_health_buddy.R

//// ------------------- Profile Items -------------------
//
//sealed class ProfileItem(val title: String, val icon: ImageVector, val route: String) {
//    object Account : ProfileItem("Account", Icons.Filled.Favorite, "user-account")
//    object Appointment : ProfileItem("Appointment", Icons.Filled.Event, "appointment")
//    object EmergencyContacts : ProfileItem("EmergencyContacts", Icons.Filled.Contacts, "emergency-contacts")
//    object FAQS : ProfileItem("FAQs", Icons.Filled.Chat, "faqs")
//    object Logout : ProfileItem("Logout", Icons.AutoMirrored.Filled.ExitToApp, "logout")
//}
//
//val profileItems = listOf(
//    ProfileItem.Account,
//    ProfileItem.Appointment,
//    ProfileItem.EmergencyContacts,
//    ProfileItem.FAQS,
//    ProfileItem.Logout
//)
//
//// ------------------- Profile Screen -------------------
//
//@Composable
//fun ProfileScreen(
//    navController: NavController,
//    items: List<ProfileItem>,
//    currentRoute: String
//) {
//    var showLogoutDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        bottomBar = { BottomBar(navController = navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .consumeWindowInsets(innerPadding)
//                .safeDrawingPadding()
//                .padding(top = 0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Top Box
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(250.dp)
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
//                        )
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Box(
//                        modifier = Modifier
//                            .background(
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
//                                ),
//                                shape = CircleShape
//                            )
//                            .clip(CircleShape)
//                            .offset(y = (-5).dp)
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.profile_picture),
//                            contentDescription = "Profile Image",
//                            modifier = Modifier
//                                .border(1.dp, Color.Transparent, CircleShape)
//                                .clip(CircleShape)
//                                .size(110.dp)
//                                .background(Color.Transparent)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "User",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = Color.Black
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Profile Items
//            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
//                items.forEach { item ->
//
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp)
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(Color(0xFFF6F8FF))
//                            .clickable {
//                                when (item) {
//                                    is ProfileItem.Account -> {
//                                        navController.navigate("user-account") {
//                                            launchSingleTop = true
//                                        }
//                                    }
//                                    is ProfileItem.Logout -> {
//                                        showLogoutDialog = true
//                                    }
//                                    else -> {
//                                        if (currentRoute != item.route) {
//                                            navController.navigate(item.route) {
//                                                popUpTo(navController.graph.startDestinationId) {
//                                                    saveState = true
//                                                }
//                                                launchSingleTop = true
//                                                restoreState = true
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            .padding(horizontal = 1.dp, vertical = 12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFFE0EBFF)),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(item.icon, contentDescription = item.title, tint = Color(0xFF5AA9E6))
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        Text(
//                            text = item.title,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = Color.Black,
//                            modifier = Modifier.weight(1f)
//                        )
//
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowRight,
//                            contentDescription = "Go",
//                            tint = Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    if (showLogoutDialog) {
//        LogoutConfirmationDialog(
//            onConfirm = {
//                showLogoutDialog = false
//                navController.navigate("welcome") {
//                    popUpTo(0)
//                }
//            },
//            onCancel = {
//                showLogoutDialog = false
//            }
//        )
//    }
//
//    Log.d("ProfileScreen", "Profile screen displayed")
//}




// ------------------- Profile Items -------------------
sealed class ProfileItem(val title: String, val icon: ImageVector, val route: String) {
    object Account : ProfileItem("Account", Icons.Filled.Favorite, "user-account")
    object Appointment : ProfileItem("Appointment", Icons.Filled.Event, "appointment")
    object EmergencyContacts : ProfileItem("Emergency Contacts", Icons.Filled.Contacts, "emergency-contacts")
    object FAQS : ProfileItem("FAQs", Icons.Filled.Chat, "faqs")
    object Logout : ProfileItem("Logout", Icons.AutoMirrored.Filled.ExitToApp, "logout")
}

val profileItems = listOf(
    ProfileItem.Account,
    ProfileItem.Appointment,
    ProfileItem.EmergencyContacts,
    ProfileItem.FAQS,
    ProfileItem.Logout
)

// ------------------- Profile Screen -------------------
@Composable
fun ProfileScreen(
    navController: NavController,
    items: List<ProfileItem>,
    currentRoute: String
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("User") }

    //Fetch firstname from Firestore
    LaunchedEffect(true) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    firstName = document.getString("firstname") ?: "User"
                }
        }
    }

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .safeDrawingPadding()
        ) {
            // ---------- Top Profile Card ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
                                ),
                                CircleShape
                            )
                            .clip(CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_picture),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .border(1.dp, Color.Transparent, CircleShape)
                                .clip(CircleShape)
                                .size(110.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ✅ Display first name
                    Text(
                        text = firstName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF6F8FF))
                            .clickable {
                                when (item) {
                                    is ProfileItem.Account -> navController.navigate("user-account")
                                    is ProfileItem.Logout -> showLogoutDialog = true
                                    else -> if (currentRoute != item.route) {
                                        navController.navigate(item.route)
                                    }
                                }
                            }
                            .padding(horizontal = 1.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0EBFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = item.title, tint = Color(0xFF5AA9E6))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }

    // ---------- Logout Dialog ----------
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showLogoutDialog = false
                navController.navigate("welcome") { popUpTo(0) }
            },
            onCancel = { showLogoutDialog = false }
        )
    }
}




// ------------------- Preview -------------------

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    val navController = rememberNavController()
    ProfileScreen(
        navController = navController,
        items = profileItems,
        currentRoute = ProfileItem.Account.route
    )
}

