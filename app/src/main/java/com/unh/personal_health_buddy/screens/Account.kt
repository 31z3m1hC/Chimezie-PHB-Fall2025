package com.unh.personal_health_buddy.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.storage.FirebaseStorage
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.User
import kotlinx.coroutines.tasks.await

// Convert ImageBitmap to Bitmap safely
//fun ImageBitmap.asAndroidBitmap(): Bitmap {
//    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//}
//
//@Composable
//fun UserAccount(
//    navController: NavHostController
//) {
//    var user by remember { mutableStateOf<User?>(null) }
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var pickedImage by remember { mutableStateOf<ImageBitmap?>(null) }
//
//
//    // Restore picked image if coming back from AccountForm
//    val savedImage = navController.currentBackStackEntry
//        ?.savedStateHandle
//        ?.get<ImageBitmap>("pickedImage")
//    if (savedImage != null) pickedImage = savedImage
//
//    // Read user only once
//    LaunchedEffect(Unit) {
//        isLoading = true
//        user = FirestoreHelper.readUser()
//
//        user?.profileImageUrl?.let { imageUrl ->
//            try {
//                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
//                val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
//                profileBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//            } catch (_: Exception) {}
//        }
//        isLoading = false
//    }
//
//    if (isLoading) {
//        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
//        return
//    }
//
//    user?.let { currentUser ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .background(MaterialTheme.colorScheme.background),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(Modifier.height(16.dp))
//
//            // Top Bar with Backward & Forward arrows
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                contentAlignment = Alignment.TopStart
//            ) {
//                Row(
//                    Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // Backward arrow
//                    IconButton(
//                        onClick = { navController.navigate("profile") })
//                    {
//                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
//                    }
//
//                    IconButton(
//                        onClick = {
//                        // Pass current user to AccountForm using currentBackStackEntry
//                        //navController.previousBackStackEntry
//                        //    ?.savedStateHandle
//                        //    ?.set("userToEdit", currentUser)
//                        // Navigate to AccountForm
//                        navController.navigate("account-form")
//                    }) {
//                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Edit Account")
//                    }
//                }
//            }
//
//            Spacer(Modifier.height(50.dp))
//
//            Text("My Account", style = MaterialTheme.typography.headlineSmall)
//
//            Spacer(Modifier.height(16.dp))
//
//            // Profile Image
//            Box(
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.surfaceVariant),
//                contentAlignment = Alignment.Center
//            ) {
//                val finalImage = pickedImage ?: profileBitmap?.asImageBitmap()
//                if (finalImage != null) {
//                    Image(
//                        bitmap = finalImage,
//                        contentDescription = "Profile Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                } else {
//                    Icon(
//                        Icons.Default.Person,
//                        contentDescription = "Profile Placeholder",
//                        modifier = Modifier.size(48.dp)
//                    )
//                }
//            }
//
//            Spacer(Modifier.height(24.dp))
//
//            // Profile Info Rows (Text only)
//            ProfileRow("First Name", currentUser.firstname)
//            ProfileRow("Last Name", currentUser.lastname)
//            ProfileRow("Date of Birth", currentUser.dateOfBirth)
//            ProfileRow("Address", currentUser.homeAddress)
//            ProfileRow("Gender", currentUser.gender.name)
//            ProfileRow("Medication", currentUser.medication)
//            ProfileRow("Allergies", currentUser.allergies ?: "")
//        }
//    }
//}
//
//@Composable
//fun ProfileRow(label: String, value: String) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp)
//    ) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.labelSmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//        Text(text = value, style = MaterialTheme.typography.bodyLarge)
//        Divider(modifier = Modifier.padding(top = 4.dp))
//    }
//}

//
//// Convert ImageBitmap to Bitmap safely
//fun ImageBitmap.asAndroidBitmap(): Bitmap {
//    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//}
//
//@Composable
//fun UserAccount(
//    navController: NavHostController
//) {
//    var user by remember { mutableStateOf<User?>(null) }
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var pickedImage by remember { mutableStateOf<ImageBitmap?>(null) }
//
//    // Restore picked image if coming back from AccountForm
//    val savedImage = navController.currentBackStackEntry
//        ?.savedStateHandle
//        ?.get<ImageBitmap>("pickedImage")
//    if (savedImage != null) pickedImage = savedImage
//
//    // Read user only once
//    LaunchedEffect(Unit) {
//        isLoading = true
//        user = FirestoreHelper.readUser()
//
//        user?.profileImageUrl?.let { imageUrl ->
//            try {
//                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
//                val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
//                profileBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//            } catch (_: Exception) {}
//        }
//        isLoading = false
//    }
//
//    if (isLoading) {
//        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
//        return
//    }
//
//    user?.let { currentUser ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .background(MaterialTheme.colorScheme.background),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(Modifier.height(16.dp))
//
//            // Top Bar with Backward & Forward arrows
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                contentAlignment = Alignment.TopStart
//            ) {
//                Row(
//                    Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // Backward arrow
//                    IconButton(onClick = { navController.navigate("profile") }) {
//                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
//                    }
//
//                    // Forward arrow to edit
//                    IconButton(onClick = {
//                        navController.navigate("account-form")
//                    }) {
//                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Edit Account")
//                    }
//                }
//            }
//
//            Spacer(Modifier.height(50.dp))
//
//            Text("My Account", style = MaterialTheme.typography.headlineSmall)
//
//            Spacer(Modifier.height(16.dp))
//
//            // Profile Image
//            Box(
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.surfaceVariant),
//                contentAlignment = Alignment.Center
//            ) {
//                val finalImage = pickedImage ?: profileBitmap?.asImageBitmap()
//                if (finalImage != null) {
//                    Image(
//                        bitmap = finalImage,
//                        contentDescription = "Profile Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                } else {
//                    Icon(
//                        Icons.Default.Person,
//                        contentDescription = "Profile Placeholder",
//                        modifier = Modifier.size(48.dp)
//                    )
//                }
//            }
//
//            Spacer(Modifier.height(24.dp))
//
//            // Profile Info Rows (Text only)
//            ProfileRow("First Name", currentUser.firstname)
//            ProfileRow("Last Name", currentUser.lastname)
//            ProfileRow("Date of Birth", currentUser.dateOfBirth)
//            ProfileRow("Address", currentUser.homeAddress)
//            ProfileRow("City", currentUser.city)
//            ProfileRow("Email", currentUser.email)
//            ProfileRow("Phone", currentUser.phoneNumber)
//            ProfileRow("Gender", currentUser.gender.name)
//            ProfileRow("Medication", currentUser.medication)
//            ProfileRow("Allergies", currentUser.allergies ?: "")
//        }
//    }
//}
//
//@Composable
//fun ProfileRow(label: String, value: String) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp)
//    ) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
//            color = MaterialTheme.colorScheme.outline
//        )
//        Text(
//            text = value,
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//        Divider(modifier = Modifier.padding(top = 4.dp))
//    }
//}

// Convert ImageBitmap to Bitmap safely
fun ImageBitmap.asAndroidBitmap(): Bitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
}

@Composable
fun UserAccount(
    navController: NavHostController
) {
    var user by remember { mutableStateOf<User?>(null) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var pickedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    // Restore picked image if coming back from AccountForm
    val savedImage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<ImageBitmap>("pickedImage")
    if (savedImage != null) pickedImage = savedImage

    // Read user only once
    LaunchedEffect(Unit) {
        isLoading = true
        user = FirestoreHelper.readUser()

        user?.profileImageUrl?.let { imageUrl ->
            try {
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
                profileBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (_: Exception) {}
        }
        isLoading = false
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        return
    }

    user?.let { currentUser ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // ✅ Make screen scrollable
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Top Bar with Backward & Forward arrows
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Backward arrow
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                    }

                    // Forward arrow to edit
                    IconButton(onClick = {
                        navController.navigate("account-form")
                    }) {
                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Edit Account")
                    }
                }
            }

            Spacer(Modifier.height(50.dp))

            Text("My Account", style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(16.dp))

            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                val finalImage = pickedImage ?: profileBitmap?.asImageBitmap()
                if (finalImage != null) {
                    Image(
                        bitmap = finalImage,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile Placeholder",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Profile Info Rows (Text only)
            ProfileRow("First Name", currentUser.firstname)
            ProfileRow("Last Name", currentUser.lastname)
            ProfileRow("Date of Birth", currentUser.dateOfBirth)
            ProfileRow("Address", currentUser.homeAddress)
            ProfileRow("City", currentUser.city)
            ProfileRow("Email", currentUser.email)
            ProfileRow("Phone", currentUser.phoneNumber)
            ProfileRow("Gender", currentUser.gender.name)
            ProfileRow("Medication", currentUser.medication)
            ProfileRow("Allergies", currentUser.allergies ?: "")

        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Divider(modifier = Modifier.padding(top = 4.dp))
    }
}
