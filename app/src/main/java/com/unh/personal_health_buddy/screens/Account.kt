package com.unh.personal_health_buddy.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
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

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

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
//// Convert ImageBitmap to Bitmap safely
//fun ImageBitmap.asAndroidBitmapFixed(): Bitmap {
//    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//    val canvas = android.graphics.Canvas(bitmap)
//    val intArray = IntArray(width * height)
//    this.readPixels(intArray, startX = 0, startY = 0, width = width, height = height)
//    bitmap.setPixels(intArray, 0, width, 0, 0, width, height)
//    return bitmap
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
//    //Reload when returning to this screen
//    LaunchedEffect(navController.currentBackStackEntry) {
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
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//
//                .background(MaterialTheme.colorScheme.background),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Spacer to push top bar down
//            Spacer(Modifier.height(32.dp))
//
//            // Top Bar (Icon + Text)
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp),
//                contentAlignment = Alignment.TopStart
//            ) {
//                Row(
//                    Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // Back arrow + Text
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.clickable { navController.navigate("profile") }
//                    ) {
//                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
//                        Spacer(Modifier.width(4.dp))
//                        Text("Profile")
//                    }
//
//                    // Edit arrow + Text
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.clickable { navController.navigate("account-form") }
//                    ) {
//                        Text("Account Form")
//                        Spacer(Modifier.width(4.dp))
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
//            // Profile Info Rows
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
//    Log.d("UserAccount", "User account screen displayed")
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



//// Convert ImageBitmap to Bitmap safely
//fun ImageBitmap.asAndroidBitmapFixed(): Bitmap {
//    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//    val canvas = android.graphics.Canvas(bitmap)
//    val intArray = IntArray(width * height)
//    this.readPixels(intArray, startX = 0, startY = 0, width = width, height = height)
//    bitmap.setPixels(intArray, 0, width, 0, 0, width, height)
//    return bitmap
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
//    LaunchedEffect(navController.currentBackStackEntry) {
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
//                .verticalScroll(rememberScrollState())
//                .background(MaterialTheme.colorScheme.background),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            // GRADIENT HEADER — NO PADDING, PROFILE PIC CENTERED
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
//                        )
//                    ),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Spacer(Modifier.height(50.dp))
//
//                // Top Bar
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.clickable { navController.navigate("profile") }
//                    ) {
//                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
//                        Spacer(Modifier.width(4.dp))
//                        //Text("Profile")
//                    }
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.clickable { navController.navigate("account-form") }
//                    ) {
//                        Text("Form")
//                        Spacer(Modifier.width(4.dp))
//                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Edit Account")
//                    }
//                }
//
//                Spacer(Modifier.height(18.dp))
//
//                Text("My Account", style = MaterialTheme.typography.headlineSmall)
//
//                Spacer(Modifier.height(16.dp))
//
//                // Centered Profile Image
//                Box(
//                    modifier = Modifier
//                        .size(120.dp)
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.surfaceVariant),
//                    contentAlignment = Alignment.Center
//                ) {
//                    val finalImage = pickedImage ?: profileBitmap?.asImageBitmap()
//                    if (finalImage != null) {
//                        Image(
//                            bitmap = finalImage,
//                            contentDescription = "Profile Image",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    } else {
//                        Icon(
//                            Icons.Default.Person,
//                            contentDescription = "Profile Placeholder",
//                            modifier = Modifier.size(48.dp)
//                        )
//                    }
//                }
//
//                Spacer(Modifier.height(50.dp))
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp, vertical = 12.dp)
//            ) {
//                ProfileRow("First Name", currentUser.firstname)
//                ProfileRow("Last Name", currentUser.lastname)
//                ProfileRow("Date of Birth", currentUser.dateOfBirth)
//                ProfileRow("Address", currentUser.homeAddress)
//                ProfileRow("City", currentUser.city)
//                ProfileRow("Email", currentUser.email)
//                ProfileRow("Phone", currentUser.phoneNumber)
//                ProfileRow("Gender", currentUser.gender.name)
//                ProfileRow("Medication", currentUser.medication)
//                ProfileRow("Allergies", currentUser.allergies ?: "")
//            }
//
//        }
//    }
//
//    Log.d("UserAccount", "User account screen displayed")
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
fun ImageBitmap.asAndroidBitmapFixed(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    val intArray = IntArray(width * height)
    this.readPixels(intArray, startX = 0, startY = 0, width = width, height = height)
    bitmap.setPixels(intArray, 0, width, 0, 0, width, height)
    return bitmap
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

    LaunchedEffect(navController.currentBackStackEntry) {
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
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --------- HEADER WITH GRADIENT ---------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(50.dp))

                // Top Bar
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.navigate("profile") }
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                        Spacer(Modifier.width(4.dp))
                        //Text("Profile")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.navigate("account-form") }
                    ) {
                        Text("Form")
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Edit Account")
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text("My Account", style = MaterialTheme.typography.headlineSmall)

                Spacer(Modifier.height(16.dp))

                // Centered Profile Image
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

                Spacer(Modifier.height(50.dp))
            }

            // --------- BOTTOM CONTENT WITH ROUNDED TOP ---------
            Surface(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = (-24).dp),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 40.dp)
                ) {
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
    }

    Log.d("UserAccount", "User account screen displayed")
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




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileRowPreview() {
    UserAccount(
        navController = rememberNavController()
    )
}

