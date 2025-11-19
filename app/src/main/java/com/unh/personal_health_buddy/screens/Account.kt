import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.unh.personal_health_buddy.R
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
//    // Primary data states (reflecting stored data)
//    var user by remember { mutableStateOf<User?>(null) }
//    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
//    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // State for in-screen editing
//    var isEditing by remember { mutableStateOf(false) }
//
//    // *** Editable Local States ***
//    var editableFirstname by remember { mutableStateOf("") }
//    var editableLastname by remember { mutableStateOf("") }
//    var editableDateOfBirth by remember { mutableStateOf("") }
//    var editableGender by remember { mutableStateOf("") } // String for UI
//    var editableEmail by remember { mutableStateOf("") }
//    var editablePhoneNumber by remember { mutableStateOf("") }
//    var editableHomeAddress by remember { mutableStateOf("") }
//    var editableCity by remember { mutableStateOf("") }
//    val editableEmergencyContacts = remember { mutableStateListOf<EmergencyContact>() }
//    var editableBloodGroup by remember { mutableStateOf("") }
//    var editableAllergies by remember { mutableStateOf("") }
//    var editableMedication by remember { mutableStateOf("") }
//    // *****************************
//
//    // Delete account dialog states
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var showPasswordDialog by remember { mutableStateOf(false) }
//    var passwordInput by remember { mutableStateOf("") }
//    var isDeleting by remember { mutableStateOf(false) }
//    var deleteError by remember { mutableStateOf<String?>(null) }
//    var expandedDropdown by remember { mutableStateOf(false) }
//
//
//    // Function to initialize editable states from primary states
//    val initializeEditableStates: (User?, List<EmergencyContact>, HealthInformation?) -> Unit = { loadedUser, loadedContacts, loadedHealth ->
//        loadedUser?.let { u ->
//            editableFirstname = u.firstname
//            editableLastname = u.lastname
//            editableDateOfBirth = u.dateOfBirth
//            editableGender = u.gender.name
//            editableEmail = u.email
//            editablePhoneNumber = u.phoneNumber
//            editableHomeAddress = u.homeAddress
//            editableCity = u.city
//        }
//        editableEmergencyContacts.clear()
//        editableEmergencyContacts.addAll(loadedContacts.map { it.copy() })
//        loadedHealth?.let { h ->
//            editableBloodGroup = h.bloodGroup
//            editableAllergies = h.allergies
//            editableMedication = h.medication
//        } ?: run {
//            editableBloodGroup = ""
//            editableAllergies = ""
//            editableMedication = ""
//        }
//    }
//
//    // Load data and initialize states
//    LaunchedEffect(userId) {
//        isLoading = true
//        try {
//            val loadedUser = withContext(Dispatchers.IO) {
//                FirestoreHelper.getUser(userId)
//            }
//            val loadedContacts = withContext(Dispatchers.IO) {
//                FirestoreHelper.readAllEmergencyContacts()
//            }
//            val loadedHealth = withContext(Dispatchers.IO) {
//                FirestoreHelper.getHealthInformation()
//            }
//
//            user = loadedUser
//            emergencyContacts = loadedContacts
//            healthInfo = loadedHealth
//            initializeEditableStates(loadedUser, loadedContacts, loadedHealth)
//
//        } catch (e: Exception) {
//            Log.e("AccountScreen", "Error fetching data: ${e.message}")
//        } finally {
//            isLoading = false
//        }
//    }
//
//    // *** SAVE LOGIC IMPLEMENTATION ***
//    val onSaveClick: () -> Unit = {
//        if (!isSaving) {
//            isSaving = true
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val parsedGender = try {
//                        Gender.valueOf(editableGender.uppercase())
//                    } catch (e: IllegalArgumentException) {
//                        Log.w("AccountScreen", "Invalid gender string: $editableGender. Defaulting to OTHER.")
//                        Gender.OTHER
//                    }
//
//                    val updatedUser = user?.copy(
//                        firstname = editableFirstname,
//                        lastname = editableLastname,
//                        dateOfBirth = editableDateOfBirth,
//                        gender = parsedGender,
//                        email = editableEmail,
//                        phoneNumber = editablePhoneNumber,
//                        homeAddress = editableHomeAddress,
//                        city = editableCity
//                    ) ?: User(
//                        firstname = editableFirstname,
//                        lastname = editableLastname,
//                        gender = parsedGender,
//                        email = editableEmail
//                    )
//
//                    val updatedHealth = HealthInformation(
//                        bloodGroup = editableBloodGroup,
//                        allergies = editableAllergies,
//                        medication = editableMedication
//                    ).takeIf { it.bloodGroup.isNotBlank() || it.allergies.isNotBlank() || it.medication.isNotBlank() }
//
//                    FirestoreHelper.updateUserData(
//                        userId,
//                        updatedUser,
//                        editableEmergencyContacts.toList(),
//                        updatedHealth
//                    )
//
//                    withContext(Dispatchers.Main) {
//                        user = updatedUser
//                        emergencyContacts = editableEmergencyContacts.toList()
//                        healthInfo = updatedHealth
//
//                        Toast.makeText(context, "Changes saved successfully", Toast.LENGTH_SHORT).show()
//                        isEditing = false
//                    }
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        Log.e("AccountScreen", "Save error: ${e.message}")
//                        Toast.makeText(context, "Error saving changes: ${e.message}", Toast.LENGTH_LONG).show()
//                    }
//                } finally {
//                    isSaving = false
//                }
//            }
//        }
//    }
//
//    // --- Delete Confirmation Dialog ---
//    if (showDeleteDialog) {
//        AlertDialog(
//            onDismissRequest = { showDeleteDialog = false },
//            icon = {
//                Icon(
//                    Icons.Default.Warning,
//                    contentDescription = "Warning",
//                    tint = Color(0xFFFF9800))
//            },
//            title = { Text(text = "Delete Account?") },
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
//                TextButton(onClick = { showDeleteDialog = false; showPasswordDialog = true }) {
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
//    // --- Password Re-authentication Dialog ---
//    if (showPasswordDialog) {
//        AlertDialog(
//            onDismissRequest = { if (!isDeleting) { showPasswordDialog = false; passwordInput = ""; deleteError = null } },
//            title = { Text(text = "Confirm Password") },
//            text = {
//                Column {
//                    Text("Please enter your password to confirm account deletion:")
//                    Spacer(modifier = Modifier.height(12.dp))
//                    OutlinedTextField(
//                        value = passwordInput,
//                        onValueChange = { passwordInput = it; deleteError = null },
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
//                            CoroutineScope(Dispatchers.IO).launch {
//                                try {
//                                    val currentUser = FirebaseAuth.getInstance().currentUser
//                                    if (currentUser != null) {
//                                        val success = FirestoreHelper.deleteUserAccountWithReauth(email, passwordInput)
//
//                                        withContext(Dispatchers.Main) {
//                                            if (success) {
//                                                Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_LONG).show()
//                                                TempProfileStorage.tempProfileBitmap = null
//                                                navController.navigate("welcome") { popUpTo(0) { inclusive = true } }
//                                            } else {
//                                                deleteError = "Authentication failed or data deletion error."
//                                                isDeleting = false
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
//                                            e.message?.contains("password", ignoreCase = true) == true -> "Incorrect password. Please try again."
//                                            e.message?.contains("network", ignoreCase = true) == true -> "Network error. Please check your connection."
//                                            else -> "Error: ${e.message}"
//                                        }
//                                        isDeleting = false
//                                    }
//                                }
//                            }
//                        }
//                    },
//                    enabled = passwordInput.isNotBlank() && !isDeleting,
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
//                ) {
//                    if (isDeleting) {
//                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
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
//    // --- Main UI Structure ---
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            // FIXED TOP SECTION (doesn't scroll)
//            AccountTopSection(
//                navController = navController,
//                user = user,
//                expandedDropdown = expandedDropdown,
//                onOptionsClick = { expandedDropdown = true },
//                onDismissDropdown = { expandedDropdown = false },
//                onDeleteClick = { showDeleteDialog = true }
//            )
//
//            // SCROLLABLE BOTTOM SECTION
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState)
//                    .padding(16.dp)
//            ) {
//                Spacer(modifier = Modifier.height(16.dp))
//
//                BottomActionSection(
//                    isEditing = isEditing,
//                    onEditClick = {
//                        initializeEditableStates(user, emergencyContacts, healthInfo)
//                        isEditing = true
//                    },
//                    onSaveClick = onSaveClick,
//                    isLoading = isSaving
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                BottomSection(
//                    user = user,
//                    emergencyContacts = emergencyContacts,
//                    healthInfo = healthInfo,
//                    isLoading = isLoading,
//                    isEditing = isEditing,
//
//                    editableFirstname = editableFirstname,
//                    editableLastname = editableLastname,
//                    editableDateOfBirth = editableDateOfBirth,
//                    editableGender = editableGender,
//                    editableEmail = editableEmail,
//                    editablePhoneNumber = editablePhoneNumber,
//                    editableHomeAddress = editableHomeAddress,
//                    editableCity = editableCity,
//                    editableEmergencyContacts = editableEmergencyContacts,
//                    editableBloodGroup = editableBloodGroup,
//                    editableAllergies = editableAllergies,
//                    editableMedication = editableMedication,
//
//                    onFirstnameChange = { editableFirstname = it },
//                    onLastnameChange = { editableLastname = it },
//                    onDateOfBirthChange = { editableDateOfBirth = it },
//                    onGenderChange = { editableGender = it },
//                    onEmailChange = { editableEmail = it },
//                    onPhoneNumberChange = { editablePhoneNumber = it },
//                    onHomeAddressChange = { editableHomeAddress = it },
//                    onCityChange = { editableCity = it },
//                    onBloodGroupChange = { editableBloodGroup = it },
//                    onAllergiesChange = { editableAllergies = it },
//                    onMedicationChange = { editableMedication = it }
//                )
//
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//
//        // Loading overlay when saving or initially loading
//        if (isLoading || isSaving) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clickable(enabled = false) {}
//                    .background(Color.Black.copy(alpha = 0.4f)),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(color = Color.White)
//            }
//        }
//    }
//}


@Composable
fun AccountScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val context = LocalContext.current

    // Primary data states (reflecting stored data)
    var user by remember { mutableStateOf<User?>(null) }
    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // State for in-screen editing
    var isEditing by remember { mutableStateOf(false) }

    // *** Editable Local States ***
    var editableFirstname by remember { mutableStateOf("") }
    var editableLastname by remember { mutableStateOf("") }
    var editableDateOfBirth by remember { mutableStateOf("") }
    var editableGender by remember { mutableStateOf("") } // String for UI
    var editableEmail by remember { mutableStateOf("") }
    var editablePhoneNumber by remember { mutableStateOf("") }
    var editableHomeAddress by remember { mutableStateOf("") }
    var editableCity by remember { mutableStateOf("") }
    val editableEmergencyContacts = remember { mutableStateListOf<EmergencyContact>() }
    var editableBloodGroup by remember { mutableStateOf("") }
    var editableAllergies by remember { mutableStateOf("") }
    var editableMedication by remember { mutableStateOf("") }
    // *****************************

    // Delete account dialog states
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var passwordInput by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }
    var deleteError by remember { mutableStateOf<String?>(null) }
    var expandedDropdown by remember { mutableStateOf(false) }


    // Function to initialize editable states from primary states
    val initializeEditableStates: (User?, List<EmergencyContact>, HealthInformation?) -> Unit = { loadedUser, loadedContacts, loadedHealth ->
        loadedUser?.let { u ->
            editableFirstname = u.firstname
            editableLastname = u.lastname
            editableDateOfBirth = u.dateOfBirth
            editableGender = u.gender.name
            editableEmail = u.email
            editablePhoneNumber = u.phoneNumber
            editableHomeAddress = u.homeAddress
            editableCity = u.city
        }
        editableEmergencyContacts.clear()
        editableEmergencyContacts.addAll(loadedContacts.map { it.copy() })
        loadedHealth?.let { h ->
            editableBloodGroup = h.bloodGroup
            editableAllergies = h.allergies
            editableMedication = h.medication
        } ?: run {
            editableBloodGroup = ""
            editableAllergies = ""
            editableMedication = ""
        }
    }

    // Load data and initialize states
    LaunchedEffect(userId) {
        isLoading = true
        try {
            val loadedUser = withContext(Dispatchers.IO) {
                FirestoreHelper.getUser(userId)
            }
            val loadedContacts = withContext(Dispatchers.IO) {
                FirestoreHelper.readAllEmergencyContacts()
            }
            val loadedHealth = withContext(Dispatchers.IO) {
                FirestoreHelper.getHealthInformation()
            }

            user = loadedUser
            emergencyContacts = loadedContacts
            healthInfo = loadedHealth
            initializeEditableStates(loadedUser, loadedContacts, loadedHealth)

        } catch (e: Exception) {
            Log.e("AccountScreen", "Error fetching data: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // *** SAVE LOGIC IMPLEMENTATION ***
    val onSaveClick: () -> Unit = {
        if (!isSaving) {
            isSaving = true
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val parsedGender = try {
                        Gender.valueOf(editableGender.uppercase())
                    } catch (e: IllegalArgumentException) {
                        Log.w("AccountScreen", "Invalid gender string: $editableGender. Defaulting to OTHER.")
                        Gender.OTHER
                    }

                    val updatedUser = user?.copy(
                        firstname = editableFirstname,
                        lastname = editableLastname,
                        dateOfBirth = editableDateOfBirth,
                        gender = parsedGender,
                        email = editableEmail,
                        phoneNumber = editablePhoneNumber,
                        homeAddress = editableHomeAddress,
                        city = editableCity
                    ) ?: User(
                        firstname = editableFirstname,
                        lastname = editableLastname,
                        gender = parsedGender,
                        email = editableEmail
                    )

                    val updatedHealth = HealthInformation(
                        bloodGroup = editableBloodGroup,
                        allergies = editableAllergies,
                        medication = editableMedication
                    ).takeIf { it.bloodGroup.isNotBlank() || it.allergies.isNotBlank() || it.medication.isNotBlank() }

                    FirestoreHelper.updateUserData(
                        userId,
                        updatedUser,
                        editableEmergencyContacts.toList(),
                        updatedHealth
                    )

                    withContext(Dispatchers.Main) {
                        user = updatedUser
                        emergencyContacts = editableEmergencyContacts.toList()
                        healthInfo = updatedHealth

                        Toast.makeText(context, "Changes saved successfully", Toast.LENGTH_SHORT).show()
                        isEditing = false
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("AccountScreen", "Save error: ${e.message}")
                        Toast.makeText(context, "Error saving changes: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } finally {
                    isSaving = false
                }
            }
        }
    }

    // --- Delete Confirmation Dialog ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFFF9800))
            },
            title = { Text(text = "Delete Account?") },
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
                TextButton(onClick = { showDeleteDialog = false; showPasswordDialog = true }) {
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

    // --- Password Re-authentication Dialog ---
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { if (!isDeleting) { showPasswordDialog = false; passwordInput = ""; deleteError = null } },
            title = { Text(text = "Confirm Password") },
            text = {
                Column {
                    Text("Please enter your password to confirm account deletion:")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it; deleteError = null },
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
                                    val currentUser = FirebaseAuth.getInstance().currentUser
                                    if (currentUser != null) {
                                        val success = FirestoreHelper.deleteUserAccountWithReauth(email, passwordInput)

                                        withContext(Dispatchers.Main) {
                                            if (success) {
                                                Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_LONG).show()
                                                TempProfileStorage.tempProfileBitmap = null
                                                navController.navigate("welcome") { popUpTo(0) { inclusive = true } }
                                            } else {
                                                deleteError = "Authentication failed or data deletion error."
                                                isDeleting = false
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
                                            e.message?.contains("password", ignoreCase = true) == true -> "Incorrect password. Please try again."
                                            e.message?.contains("network", ignoreCase = true) == true -> "Network error. Please check your connection."
                                            else -> "Error: ${e.message}"
                                        }
                                        isDeleting = false
                                    }
                                }
                            }
                        }
                    },
                    enabled = passwordInput.isNotBlank() && !isDeleting,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    if (isDeleting) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
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

    // --- Main UI Structure ---
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // FIXED TOP SECTION (doesn't scroll)
            AccountTopSection(
                navController = navController,
                user = user,
                expandedDropdown = expandedDropdown,
                onOptionsClick = { expandedDropdown = true },
                onDismissDropdown = { expandedDropdown = false },
                onDeleteClick = { showDeleteDialog = true }
            )

            // SCROLLABLE BOTTOM SECTION
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                BottomActionSection(
                    isEditing = isEditing,
                    onEditClick = {
                        initializeEditableStates(user, emergencyContacts, healthInfo)
                        isEditing = true
                    },
                    onSaveClick = onSaveClick,
                    isLoading = isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                BottomSection(
                    user = user,
                    emergencyContacts = emergencyContacts,
                    healthInfo = healthInfo,
                    isLoading = isLoading,
                    isEditing = isEditing,

                    editableFirstname = editableFirstname,
                    editableLastname = editableLastname,
                    editableDateOfBirth = editableDateOfBirth,
                    editableGender = editableGender,
                    editableEmail = editableEmail,
                    editablePhoneNumber = editablePhoneNumber,
                    editableHomeAddress = editableHomeAddress,
                    editableCity = editableCity,
                    editableEmergencyContacts = editableEmergencyContacts,
                    editableBloodGroup = editableBloodGroup,
                    editableAllergies = editableAllergies,
                    editableMedication = editableMedication,

                    // --- INPUT FILTERING START ---
                    onFirstnameChange = { newValue ->
                        // Accept the change only if it's not purely numeric or empty
                        if (newValue.isEmpty() || !newValue.all { it.isDigit() }) {
                            editableFirstname = newValue
                        }
                    },
                    onLastnameChange = { newValue ->
                        // Accept the change only if it's not purely numeric or empty
                        if (newValue.isEmpty() || !newValue.all { it.isDigit() }) {
                            editableLastname = newValue
                        }
                    },
                    onDateOfBirthChange = { editableDateOfBirth = it },
                    onGenderChange = { editableGender = it },
                    onEmailChange = { editableEmail = it },
                    onPhoneNumberChange = { newValue ->
                        // Only allow digits and max 10 characters
                        if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                            editablePhoneNumber = newValue
                        }
                    },
                    onHomeAddressChange = { editableHomeAddress = it },
                    onCityChange = { editableCity = it },
                    onBloodGroupChange = { editableBloodGroup = it },
                    onAllergiesChange = { editableAllergies = it },
                    onMedicationChange = { editableMedication = it }
                    // --- INPUT FILTERING END ---
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Loading overlay when saving or initially loading
        if (isLoading || isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {}
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}







@Composable
fun BottomActionSection(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        if (isEditing) {
            Button(
                onClick = onSaveClick,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.purple_500),
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                } else {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Save",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save Changes")
                }
            }
        } else {
            Text(
                text = "Edit",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(onClick = onEditClick)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun BottomSection(
    user: User?,
    emergencyContacts: List<EmergencyContact>,
    healthInfo: HealthInformation?,
    isLoading: Boolean,
    isEditing: Boolean,
    // Editable States (for reading)
    editableFirstname: String,
    editableLastname: String,
    editableDateOfBirth: String,
    editableGender: String,
    editableEmail: String,
    editablePhoneNumber: String,
    editableHomeAddress: String,
    editableCity: String,
    editableEmergencyContacts: List<EmergencyContact>,
    editableBloodGroup: String,
    editableAllergies: String,
    editableMedication: String,
    // Editable Callbacks (for writing)
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onHomeAddressChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onBloodGroupChange: (String) -> Unit,
    onAllergiesChange: (String) -> Unit,
    onMedicationChange: (String) -> Unit
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
                            color = Color.Black
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        EditableOrInfoRow(
                            label = "First Name",
                            value = if (isEditing) editableFirstname else u.firstname,
                            isEditing = isEditing,
                            onValueChange = onFirstnameChange
                        )
                        EditableOrInfoRow(
                            label = "Last Name",
                            value = if (isEditing) editableLastname else u.lastname,
                            isEditing = isEditing,
                            onValueChange = onLastnameChange
                        )
                        EditableOrInfoRow(
                            label = "Date of Birth",
                            value = if (isEditing) editableDateOfBirth else u.dateOfBirth,
                            isEditing = isEditing,
                            onValueChange = onDateOfBirthChange
                        )

                        // Use editableGender (String) or u.gender.name (String)
                        EditableOrInfoRow(
                            label = "Gender",
                            value = if (isEditing) editableGender else u.gender.name,
                            isEditing = isEditing,
                            onValueChange = onGenderChange
                        )

                        EditableOrInfoRow(
                            label = "Email",
                            value = if (isEditing) editableEmail else u.email,
                            isEditing = isEditing,
                            onValueChange = onEmailChange,
                            readOnly = true
                        )
                        EditableOrInfoRow(
                            label = "Phone Number",
                            value = if (isEditing) editablePhoneNumber else u.phoneNumber,
                            isEditing = isEditing, onValueChange = onPhoneNumberChange
                        )
                        EditableOrInfoRow(
                            label = "Home Address",
                            value = if (isEditing) editableHomeAddress else u.homeAddress,
                            isEditing = isEditing,
                            onValueChange = onHomeAddressChange
                        )
                        EditableOrInfoRow(
                            label = "City",
                            value = if (isEditing) editableCity else u.city,
                            isEditing = isEditing,
                            onValueChange = onCityChange
                        )
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
                    Text("Emergency Contacts",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    val contactsToDisplay = if (isEditing) editableEmergencyContacts else emergencyContacts

                    if (contactsToDisplay.isNotEmpty()) {
                        contactsToDisplay.forEachIndexed { index, c ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(12.dp));
                                Divider(thickness = 0.5.dp, color = Color.LightGray);
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            EditableOrInfoRow(
                                label = "First Name",
                                value = c.firstname,
                                isEditing = isEditing,
                                onValueChange = {
                                    newValue -> if (editableEmergencyContacts.size > index) {
                                        (editableEmergencyContacts as? MutableList<EmergencyContact>)?.set(index, editableEmergencyContacts[index].copy(firstname = newValue))
                                    }
                                }
                            )
                            EditableOrInfoRow(
                                label = "Last Name",
                                value = c.lastname,
                                isEditing = isEditing,
                                onValueChange = {
                                    newValue -> if (editableEmergencyContacts.size > index) {
                                        (editableEmergencyContacts as? MutableList<EmergencyContact>)?.set(index, editableEmergencyContacts[index].copy(lastname = newValue))
                                    }
                                }
                            )
                            EditableOrInfoRow(
                                label = "Phone",
                                value = c.phoneNumber,
                                isEditing = isEditing,
                                onValueChange = {
                                    newValue -> if (editableEmergencyContacts.size > index) {
                                        (editableEmergencyContacts as? MutableList<EmergencyContact>)?.set(index, editableEmergencyContacts[index].copy(phoneNumber = newValue))
                                    }
                                }
                            )
                            EditableOrInfoRow(
                                label = "Relationship",
                                value = c.relationship,
                                isEditing = isEditing,
                                onValueChange = {
                                    newValue -> if (editableEmergencyContacts.size > index) {
                                        (editableEmergencyContacts as? MutableList<EmergencyContact>)?.set(index, editableEmergencyContacts[index].copy(relationship = newValue))
                                    }
                                }
                            )
                        }
                    } else {
                        Text(
                            "No emergency contacts added.",
                            fontStyle = FontStyle.Italic,
                            color = Color.Black
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
                        color = Color.Black
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp)
                    )

                    val currentBloodGroup = if (isEditing) editableBloodGroup else healthInfo?.bloodGroup ?: ""
                    val currentAllergies = if (isEditing) editableAllergies else healthInfo?.allergies ?: ""
                    val currentMedication = if (isEditing) editableMedication else healthInfo?.medication ?: ""

                    if (healthInfo != null || isEditing) {
                        EditableOrInfoRow(
                            label = "Blood Group",
                            value = currentBloodGroup,
                            isEditing = isEditing,
                            onValueChange = onBloodGroupChange
                        )
                        EditableOrInfoRow(
                            label = "Allergies",
                            value = currentAllergies,
                            isEditing = isEditing,
                            onValueChange = onAllergiesChange
                        )
                        EditableOrInfoRow(
                            label = "Medications",
                            value = currentMedication,
                            isEditing = isEditing,
                            onValueChange = onMedicationChange
                        )
                    } else {
                        Text(
                            "No health information added.",
                            fontStyle = FontStyle.Italic,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditableOrInfoRow(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false
) {
    val displayValue = if (value.isBlank()) "N/A" else value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.weight(0.4f)
        )

        if (isEditing && !readOnly) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier
                    .weight(0.6f)
                    .height(50.dp)
                    .padding(vertical = 0.dp),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        } else {
            Text(
                text = displayValue,
                fontWeight = FontWeight.Normal,
                color = if (readOnly && isEditing) Color.Gray else MaterialTheme.colorScheme.onSurface,
                fontStyle = if (displayValue == "N/A") FontStyle.Italic else FontStyle.Normal,
                modifier = Modifier.weight(0.6f)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    EditableOrInfoRow(
        label = label,
        value = value,
        isEditing = false,
        onValueChange = {}
    )
}

//@Composable
//fun AccountTopSection(
//    navController: NavHostController,
//    user: User?,
//    expandedDropdown: Boolean,
//    onOptionsClick: () -> Unit,
//    onDismissDropdown: () -> Unit,
//    onDeleteClick: () -> Unit
//) {
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val firstName = user?.firstname ?: "User"
//
//    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
//        val tempBitmap = TempProfileStorage.tempProfileBitmap
//        if (tempBitmap != null) {
//            profileBitmap = tempBitmap
//        } else {
//            user?.profileImageUrl?.let { url ->
//                try {
//                    withContext(Dispatchers.IO) {
//                        val stream = URL(url).openStream()
//                        profileBitmap = BitmapFactory.decodeStream(stream)
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
//            Spacer(modifier = Modifier.height(32.dp))
//
//            BackHeader(
//                title = "Profile",
//                onBack = { navController.navigate("account-form") }
//            )
//            Text(
//                text = "User Information",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
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
//        Box(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(top = 32.dp, end = 8.dp)
//        ) {
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text(
//                text = "Options",
//                style = MaterialTheme.typography.labelSmall,
//                color = Color.Black,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Normal,
//                modifier = Modifier
//                    .clickable(onClick = onOptionsClick)
//                    .padding(top = 16.dp)
//
//            )
//
//            DropdownMenu(
//                expanded = expandedDropdown,
//                onDismissRequest = onDismissDropdown
//            ) {
//                // --- NEW ITEM: Navigate to Account Form ---
//                DropdownMenuItem(
//                    text = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Edit, // Using Edit icon for form
//                                contentDescription = "Edit User Details",
//                                tint = Color.Black
//                            )
//                            Text("Account Form")
//                        }
//                    },
//                    onClick = {
//                        onDismissDropdown()
//                        // Navigate to the specific account-form route
//                        navController.navigate("account-form")
//                    }
//                )
//                Divider()
//                // --- EXISTING ITEM: Delete Account ---
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
//                        onDismissDropdown()
//                        onDeleteClick()
//                    }
//                )
//            }
//        }
//    }
//}


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

    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
        val tempBitmap = TempProfileStorage.tempProfileBitmap
        if (tempBitmap != null) {
            profileBitmap = tempBitmap
        } else {
            user?.profileImageUrl?.let { url ->
                try {
                    withContext(Dispatchers.IO) {
                        val stream = URL(url).openStream()
                        profileBitmap = BitmapFactory.decodeStream(stream)
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
            Spacer(modifier = Modifier.height(42.dp))

            BackHeader(
                title = "Profile",
                onBack = { navController.navigate("account-form") }
            )
            Text(
                text = "User Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
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

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 32.dp, end = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(onClick = onOptionsClick)
                    .padding(top = 30.dp, start = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = "Options",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Show options",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = onDismissDropdown
            ) {
                // --- NEW ITEM: Navigate to Account Form ---
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit User Details",
                                tint = Color.Black
                            )
                            Text("Account Form")
                        }
                    },
                    onClick = {
                        onDismissDropdown()
                        navController.navigate("account-form")
                    }
                )
                Divider()
                // --- EXISTING ITEM: Delete Account ---
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