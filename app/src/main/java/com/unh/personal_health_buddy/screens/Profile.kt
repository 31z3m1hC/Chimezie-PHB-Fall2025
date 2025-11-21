package com.unh.personal_health_buddy.screens

import LogoutConfirmationDialog
import TempProfileStorage
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.http.SslCertificate.restoreState
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unh.personal_health_buddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


// ------------------- Profile Items -------------------
sealed class ProfileItem(val title: String, val icon: ImageVector, val route: String) {
    object Account : ProfileItem("Account", Icons.Filled.Favorite, "account")
    object Appointment : ProfileItem("Appointment", Icons.Filled.Event, "appointment")
    object FAQS : ProfileItem("FAQS", Icons.Filled.Chat, "faqs")
    object Logout : ProfileItem("Logout", Icons.AutoMirrored.Filled.ExitToApp, "logout")
}

val profileItems = listOf(
    ProfileItem.Account,
    ProfileItem.Appointment,
    ProfileItem.FAQS,
    ProfileItem.Logout
)

// ------------------- Profile Screen -------------------
@Composable
fun ProfileScreen(
    navController: NavHostController,
    items: List<ProfileItem>,
    currentRoute: String
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("User") }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    // Fetch firstname and profile image URL from Firestore
    LaunchedEffect(true) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    firstName = document.getString("firstname") ?: "User"
                    profileImageUrl = document.getString("profileImageUrl")
                }
        }
    }

    // Load profile image from temp storage or Firestore URL
    LaunchedEffect(profileImageUrl, TempProfileStorage.tempProfileBitmap) {
        val tempBitmap = TempProfileStorage.tempProfileBitmap
        if (tempBitmap != null) {
            profileBitmap = tempBitmap
        } else {
            profileImageUrl?.let { url ->
                try {
                    withContext(Dispatchers.IO) {
                        val stream = URL(url).openStream()
                        profileBitmap = BitmapFactory.decodeStream(stream)
                    }
                } catch (e: Exception) {
                    Log.e("ProfileScreen", "Error loading image: ${e.message}")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- Top Profile Card (UI untouched) ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp)
                    .height(300.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 60.dp)
                ) {
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
                        if (profileBitmap != null) {
                            Image(
                                bitmap = profileBitmap!!.asImageBitmap(),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .border(0.dp, Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .size(110.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .border(0.dp, Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .size(110.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = firstName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF6F8FF))
                            .clickable {
                                when (item) {
                                    is ProfileItem.Account -> {
                                        navController.navigate("account") {
                                            launchSingleTop = true
                                        }
                                    }

                                    is ProfileItem.Appointment -> {
                                        navController.navigate("appointment") {
                                            launchSingleTop = true
                                        }
                                    }

                                    is ProfileItem.FAQS -> {
                                        navController.navigate("faqs") {
                                            launchSingleTop = true
                                        }
                                    }

                                    is ProfileItem.Logout -> {
                                        showLogoutDialog = true
                                    }
                                }
                            }
                            .padding(horizontal = 1.dp, vertical = 4.dp),
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

                        Spacer(modifier = Modifier.width(8.dp))

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
}