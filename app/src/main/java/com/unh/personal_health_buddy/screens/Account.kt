import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URL

//@Composable
//fun AccountScreen(navController: NavHostController) {
//    val scrollState = rememberScrollState()
//    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
//    val context = LocalContext.current
//
//    var user by remember { mutableStateOf<User?>(null) }
//    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
//    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    // Delete account dialog states
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var showPasswordDialog by remember { mutableStateOf(false) }
//    var passwordInput by remember { mutableStateOf("") }
//    var isDeleting by remember { mutableStateOf(false) }
//    var deleteError by remember { mutableStateOf<String?>(null) }
//
//    // Dropdown menu state
//    var expandedDropdown by remember { mutableStateOf(false) }
//
//    LaunchedEffect(userId) {
//        isLoading = true
//        try {
//            withContext(Dispatchers.IO) {
//                user = FirestoreHelper.getUser(userId)
//                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
//                healthInfo = FirestoreHelper.getHealthInformation()
//            }
//        } catch (e: Exception) {
//            Log.e("AccountScreen", "Error fetching data: ${e.message}")
//        } finally {
//            isLoading = false
//        }
//    }
//
//    // Delete Confirmation Dialog
//    if (showDeleteDialog) {
//        AlertDialog(
//            onDismissRequest = { showDeleteDialog = false },
//            icon = {
//                Icon(
//                    Icons.Default.Warning,
//                    contentDescription = "Warning",
//                    tint = Color(0xFFFF9800)
//                )
//            },
//            title = {
//                Text(text = "Delete Account?")
//            },
//            text = {
//                Column {
//                    Text(text = "This action cannot be undone. All your data will be permanently deleted including:")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("• Personal information", style = MaterialTheme.typography.bodySmall)
//                    Text("• Emergency contacts", style = MaterialTheme.typography.bodySmall)
//                    Text("• Health information", style = MaterialTheme.typography.bodySmall)
//                    Text("• Profile pictures", style = MaterialTheme.typography.bodySmall)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "Are you absolutely sure?",
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Red
//                    )
//                }
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showDeleteDialog = false
//                        showPasswordDialog = true
//                    }
//                ) {
//                    Text("Continue", color = Color.Red)
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDeleteDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    // Password Re-authentication Dialog
//    if (showPasswordDialog) {
//        AlertDialog(
//            onDismissRequest = {
//                if (!isDeleting) {
//                    showPasswordDialog = false
//                    passwordInput = ""
//                    deleteError = null
//                }
//            },
//            title = {
//                Text(text = "Confirm Password")
//            },
//            text = {
//                Column {
//                    Text("Please enter your password to confirm account deletion:")
//                    Spacer(modifier = Modifier.height(12.dp))
//                    OutlinedTextField(
//                        value = passwordInput,
//                        onValueChange = {
//                            passwordInput = it
//                            deleteError = null
//                        },
//                        label = { Text("Password") },
//                        visualTransformation = PasswordVisualTransformation(),
//                        singleLine = true,
//                        enabled = !isDeleting,
//                        isError = deleteError != null,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    if (deleteError != null) {
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = deleteError!!,
//                            color = MaterialTheme.colorScheme.error,
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    }
//                }
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        if (passwordInput.isNotBlank() && !isDeleting) {
//                            isDeleting = true
//                            val email = user?.email ?: FirebaseAuth.getInstance().currentUser?.email ?: ""
//
//                            CoroutineScope(Dispatchers.IO).launch {
//                                try {
//                                    // Get current user
//                                    val currentUser = FirebaseAuth.getInstance().currentUser
//
//                                    if (currentUser != null) {
//                                        // Re-authenticate
//                                        val credential = EmailAuthProvider.getCredential(email, passwordInput)
//                                        currentUser.reauthenticate(credential).await()
//
//                                        // Delete all user data (ignore errors if collections don't exist)
//                                        try {
//                                            FirestoreHelper.deleteAllUserData(currentUser.uid)
//                                        } catch (e: Exception) {
//                                            Log.e("AccountScreen", "Error deleting data: ${e.message}")
//                                            // Continue with account deletion even if data deletion fails
//                                        }
//
//                                        // Delete Firebase Auth account
//                                        currentUser.delete().await()
//
//                                        withContext(Dispatchers.Main) {
//                                            Toast.makeText(
//                                                context,
//                                                "Account deleted successfully",
//                                                Toast.LENGTH_LONG
//                                            ).show()
//
//                                            // Clear temp storage
//                                            TempProfileStorage.tempProfileBitmap = null
//
//                                            // Navigate to welcome screen
//                                            navController.navigate("welcome") {
//                                                popUpTo(0) { inclusive = true }
//                                            }
//                                        }
//                                    } else {
//                                        withContext(Dispatchers.Main) {
//                                            deleteError = "No user found. Please try again."
//                                            isDeleting = false
//                                        }
//                                    }
//                                } catch (e: Exception) {
//                                    withContext(Dispatchers.Main) {
//                                        deleteError = when {
//                                            e.message?.contains("password", ignoreCase = true) == true ->
//                                                "Incorrect password. Please try again."
//                                            e.message?.contains("network", ignoreCase = true) == true ->
//                                                "Network error. Please check your connection."
//                                            else -> "Error: ${e.message}"
//                                        }
//                                        isDeleting = false
//                                    }
//                                }
//                            }
//                        }
//                    },
//                    enabled = passwordInput.isNotBlank() && !isDeleting,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Red,
//                        contentColor = Color.White
//                    )
//                ) {
//                    if (isDeleting) {
//                        CircularProgressIndicator(
//                            modifier = Modifier.size(16.dp),
//                            color = Color.White
//                        )
//                    } else {
//                        Text("Delete Account")
//                    }
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        showPasswordDialog = false
//                        passwordInput = ""
//                        deleteError = null
//                    },
//                    enabled = !isDeleting
//                ) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//                .padding(16.dp)
//        ) {
//            // Top section: profile pic and first name
//            AccountTopSection(
//                navController = navController,
//                user = user,
//                onOptionsClick = { expandedDropdown = true }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Bottom section: personal info, emergency contacts, health info
//            BottomSection(
//                user = user,
//                emergencyContacts = emergencyContacts,
//                healthInfo = healthInfo,
//                isLoading = isLoading
//            )
//
//            // Extra space at bottom for better scrolling experience
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//
//        // Options Dropdown Menu (positioned at top right)
//        Box(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(top = 16.dp, end = 16.dp)
//        ) {
//            DropdownMenu(
//                expanded = expandedDropdown,
//                onDismissRequest = { expandedDropdown = false }
//            ) {
//                DropdownMenuItem(
//                    text = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Edit,
//                                contentDescription = "Edit Account",
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                            Text("Edit Account")
//                        }
//                    },
//                    onClick = {
//                        expandedDropdown = false
//                        navController.navigate("account-form")
//                    }
//                )
//                Divider()
//                DropdownMenuItem(
//                    text = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Delete,
//                                contentDescription = "Delete",
//                                tint = Color.Red
//                            )
//                            Text("Delete Account", color = Color.Red)
//                        }
//                    },
//                    onClick = {
//                        expandedDropdown = false
//                        showDeleteDialog = true
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun BottomSection(
//    user: User?,
//    emergencyContacts: List<EmergencyContact>,
//    healthInfo: HealthInformation?,
//    isLoading: Boolean
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(32.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            // Personal Information Card
//            user?.let { u ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text(
//                            "Personal Information",
//                            fontWeight = FontWeight.Bold,
//                            style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        Divider(modifier = Modifier.padding(vertical = 8.dp))
//                        InfoRow("Full Name", "${u.firstname} ${u.lastname}")
//                        InfoRow("Date of Birth", u.dateOfBirth)
//                        InfoRow("Gender", u.gender.toString())
//                        InfoRow("Email", u.email)
//                        InfoRow("Phone Number", u.phoneNumber)
//                        InfoRow("Home Address", u.homeAddress)
//                        InfoRow("City", u.city)
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Emergency Contacts Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        "Emergency Contacts",
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//                    if (emergencyContacts.isNotEmpty()) {
//                        emergencyContacts.forEachIndexed { index, c ->
//                            if (index > 0) {
//                                Spacer(modifier = Modifier.height(12.dp))
//                                Divider(thickness = 0.5.dp, color = Color.LightGray)
//                                Spacer(modifier = Modifier.height(12.dp))
//                            }
//                            InfoRow("First Name", c.firstname)
//                            InfoRow("Last Name", c.lastname)
//                            InfoRow("Phone", c.phoneNumber)
//                            InfoRow("Relationship", c.relationship)
//                        }
//                    } else {
//                        Text(
//                            "No emergency contacts added.",
//                            fontStyle = FontStyle.Italic,
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Health Information Card
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        "Health Information",
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//                    healthInfo?.let { h ->
//                        InfoRow("Blood Group", h.bloodGroup)
//                        InfoRow("Allergies", h.allergies)
//                        InfoRow("Medications", h.medication)
//                    } ?: run {
//                        Text(
//                            "No health information added.",
//                            fontStyle = FontStyle.Italic,
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun InfoRow(label: String, value: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = label,
//            fontWeight = FontWeight.Medium,
//            color = Color.DarkGray,
//            modifier = Modifier.weight(0.4f)
//        )
//        Text(
//            text = value,
//            fontWeight = FontWeight.Normal,
//            modifier = Modifier.weight(0.6f)
//        )
//    }
//}
//
//@Composable
//fun AccountTopSection(
//    navController: NavHostController,
//    user: User?,
//    onOptionsClick: () -> Unit
//) {
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val firstName = user?.firstname ?: "User"
//
//    // UPDATED: Check temp storage first, then Firebase
//    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
//        // Priority 1: Check temp storage first
//        val tempBitmap = TempProfileStorage.tempProfileBitmap
//        if (tempBitmap != null) {
//            profileBitmap = tempBitmap
//            Log.d("AccountTopSection", "Using temp storage image")
//        } else {
//            // Priority 2: Load from Firebase if no temp image
//            user?.profileImageUrl?.let { url ->
//                try {
//                    withContext(Dispatchers.IO) {
//                        val stream = URL(url).openStream()
//                        profileBitmap = BitmapFactory.decodeStream(stream)
//                        Log.d("AccountTopSection", "Loaded image from Firebase")
//                    }
//                } catch (e: Exception) {
//                    Log.e("AccountTopSection", "Error loading image: ${e.message}")
//                }
//            }
//        }
//    }
//
//    val imageBitmap = profileBitmap?.asImageBitmap()
//
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            BackHeader(
//                title = "Profile",
//                onBack = { navController.navigate("profile") }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (imageBitmap != null) {
//                Image(
//                    bitmap = imageBitmap,
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier
//                        .size(140.dp)
//                        .clip(CircleShape)
//                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "Default Profile",
//                    modifier = Modifier.size(120.dp),
//                    tint = Color.Gray
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = firstName, fontWeight = FontWeight.SemiBold)
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//
//        // Options button positioned at top right
//        Column(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(top = 8.dp, end = 8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            IconButton(
//                onClick = onOptionsClick,
//                modifier = Modifier.size(40.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Options Menu",
//                    modifier = Modifier.size(32.dp)
//                )
//            }
//            Text(
//                text = "Options",
//                style = MaterialTheme.typography.labelSmall,
//                color = Color.Gray,
//                fontSize = 10.sp
//            )
//        }
//    }
//}

@Composable
fun AccountScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val context = LocalContext.current

    var user by remember { mutableStateOf<User?>(null) }
    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Delete account dialog states
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var passwordInput by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }
    var deleteError by remember { mutableStateOf<String?>(null) }

    // Dropdown menu state
    var expandedDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        isLoading = true
        try {
            withContext(Dispatchers.IO) {
                user = FirestoreHelper.getUser(userId)
                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
                healthInfo = FirestoreHelper.getHealthInformation()
            }
        } catch (e: Exception) {
            Log.e("AccountScreen", "Error fetching data: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFFF9800)
                )
            },
            title = {
                Text(text = "Delete Account?")
            },
            text = {
                Column {
                    Text(text = "This action cannot be undone. All your data will be permanently deleted including:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Personal information", style = MaterialTheme.typography.bodySmall)
                    Text("• Emergency contacts", style = MaterialTheme.typography.bodySmall)
                    Text("• Health information", style = MaterialTheme.typography.bodySmall)
                    Text("• Profile pictures", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Are you absolutely sure?",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        showPasswordDialog = true
                    }
                ) {
                    Text("Continue", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Password Re-authentication Dialog
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isDeleting) {
                    showPasswordDialog = false
                    passwordInput = ""
                    deleteError = null
                }
            },
            title = {
                Text(text = "Confirm Password")
            },
            text = {
                Column {
                    Text("Please enter your password to confirm account deletion:")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = {
                            passwordInput = it
                            deleteError = null
                        },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        enabled = !isDeleting,
                        isError = deleteError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (deleteError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = deleteError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (passwordInput.isNotBlank() && !isDeleting) {
                            isDeleting = true
                            val email = user?.email ?: FirebaseAuth.getInstance().currentUser?.email ?: ""

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    // Get current user
                                    val currentUser = FirebaseAuth.getInstance().currentUser

                                    if (currentUser != null) {
                                        // Re-authenticate
                                        val credential = EmailAuthProvider.getCredential(email, passwordInput)
                                        currentUser.reauthenticate(credential).await()

                                        // Delete all user data (ignore errors if collections don't exist)
                                        try {
                                            FirestoreHelper.deleteAllUserData(currentUser.uid)
                                        } catch (e: Exception) {
                                            Log.e("AccountScreen", "Error deleting data: ${e.message}")
                                            // Continue with account deletion even if data deletion fails
                                        }

                                        // Delete Firebase Auth account
                                        currentUser.delete().await()

                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                "Account deleted successfully",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            // Clear temp storage
                                            TempProfileStorage.tempProfileBitmap = null

                                            // Navigate to welcome screen
                                            navController.navigate("welcome") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            deleteError = "No user found. Please try again."
                                            isDeleting = false
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        deleteError = when {
                                            e.message?.contains("password", ignoreCase = true) == true ->
                                                "Incorrect password. Please try again."
                                            e.message?.contains("network", ignoreCase = true) == true ->
                                                "Network error. Please check your connection."
                                            else -> "Error: ${e.message}"
                                        }
                                        isDeleting = false
                                    }
                                }
                            }
                        }
                    },
                    enabled = passwordInput.isNotBlank() && !isDeleting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    if (isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Delete Account")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPasswordDialog = false
                        passwordInput = ""
                        deleteError = null
                    },
                    enabled = !isDeleting
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Top section: profile pic and first name
            AccountTopSection(
                navController = navController,
                user = user,
                expandedDropdown = expandedDropdown,
                onOptionsClick = { expandedDropdown = true },
                onDismissDropdown = { expandedDropdown = false },
                onDeleteClick = { showDeleteDialog = true }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Bottom section: personal info, emergency contacts, health info
            BottomSection(
                user = user,
                emergencyContacts = emergencyContacts,
                healthInfo = healthInfo,
                isLoading = isLoading
            )

            // Extra space at bottom for better scrolling experience
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BottomSection(
    user: User?,
    emergencyContacts: List<EmergencyContact>,
    healthInfo: HealthInformation?,
    isLoading: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Personal Information Card
            user?.let { u ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Personal Information",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow("Full Name", "${u.firstname} ${u.lastname}")
                        InfoRow("Date of Birth", u.dateOfBirth)
                        InfoRow("Gender", u.gender.toString())
                        InfoRow("Email", u.email)
                        InfoRow("Phone Number", u.phoneNumber)
                        InfoRow("Home Address", u.homeAddress)
                        InfoRow("City", u.city)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Emergency Contacts Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Emergency Contacts",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    if (emergencyContacts.isNotEmpty()) {
                        emergencyContacts.forEachIndexed { index, c ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(thickness = 0.5.dp, color = Color.LightGray)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            InfoRow("First Name", c.firstname)
                            InfoRow("Last Name", c.lastname)
                            InfoRow("Phone", c.phoneNumber)
                            InfoRow("Relationship", c.relationship)
                        }
                    } else {
                        Text(
                            "No emergency contacts added.",
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Health Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Health Information",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    healthInfo?.let { h ->
                        InfoRow("Blood Group", h.bloodGroup)
                        InfoRow("Allergies", h.allergies)
                        InfoRow("Medications", h.medication)
                    } ?: run {
                        Text(
                            "No health information added.",
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
fun AccountTopSection(
    navController: NavHostController,
    user: User?,
    expandedDropdown: Boolean,
    onOptionsClick: () -> Unit,
    onDismissDropdown: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val firstName = user?.firstname ?: "User"

    // UPDATED: Check temp storage first, then Firebase
    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
        // Priority 1: Check temp storage first
        val tempBitmap = TempProfileStorage.tempProfileBitmap
        if (tempBitmap != null) {
            profileBitmap = tempBitmap
            Log.d("AccountTopSection", "Using temp storage image")
        } else {
            // Priority 2: Load from Firebase if no temp image
            user?.profileImageUrl?.let { url ->
                try {
                    withContext(Dispatchers.IO) {
                        val stream = URL(url).openStream()
                        profileBitmap = BitmapFactory.decodeStream(stream)
                        Log.d("AccountTopSection", "Loaded image from Firebase")
                    }
                } catch (e: Exception) {
                    Log.e("AccountTopSection", "Error loading image: ${e.message}")
                }
            }
        }
    }

    val imageBitmap = profileBitmap?.asImageBitmap()

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            BackHeader(
                title = "Profile",
                onBack = { navController.navigate("profile") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default Profile",
                    modifier = Modifier.size(120.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = firstName, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Options text positioned at top right - now clickable
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 8.dp)
        ) {

            Text(
                text = "Options",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(onClick = onOptionsClick)
                    .padding(16.dp)
            )

            DropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = onDismissDropdown
            ) {
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Account",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("Edit Account")
                        }
                    },
                    onClick = {
                        onDismissDropdown()
                        navController.navigate("account-form")
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                            Text("Delete Account", color = Color.Red)
                        }
                    },
                    onClick = {
                        onDismissDropdown()
                        onDeleteClick()
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = rememberNavController())
}