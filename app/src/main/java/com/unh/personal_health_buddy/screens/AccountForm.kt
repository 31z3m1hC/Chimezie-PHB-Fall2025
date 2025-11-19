import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onPreviewKeyEvent

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face2
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.unh.personal_health_buddy.R
import java.io.File
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

import com.unh.personal_health_buddy.database.Gender
import com.unh.personal_health_buddy.database.HealthInformation
import com.unh.personal_health_buddy.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "profile01.jpg")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    Log.d("saveBitmapToCache", "File path: ${file.absolutePath}")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}




fun validateUserInput(user: User, firebaseEmail: String): Boolean {
    val emailValid = user.email == firebaseEmail
    val phoneValid = user.phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$"))
    val nameValid = user.firstname.isNotBlank()
    Log.d("Validation", "EmailValid: $emailValid, PhoneValid: $phoneValid, NameValid: $nameValid")

    return emailValid && phoneValid && nameValid
}


@Composable
fun SaveButton(
    enabled: Boolean,
    isSaving: Boolean,
    onSave: () -> Unit
) {
    if (isSaving) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    } else {
        Button(
            onClick = onSave,
            enabled = enabled,
            modifier = Modifier.height(36.dp)
        ) {
            Text("Save")
        }
    }
    Log.d("SaveButton", "Enabled: $enabled")
}



@Composable
fun TopBarWithSave(
    title: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    enabled: Boolean,
    isSaving: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {

            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }

        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                strokeWidth = 2.dp
            )
        } else {

            Button(
                onClick = onSave,
                enabled = enabled,
                modifier = Modifier.height(36.dp)
            ) {
                Text("Save")
            }
        }
    }
    Log.d("TopBarWithSave", "Title: $title")
}




@Composable
fun RoundedIcon(
    imageVector: ImageVector,
    backgroundColor: Color = Color(0xFFE0F7FA),
    iconTint: Color = Color(0xFF00796B),
    size: Dp = 36.dp,
    padding: Dp = 8.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.padding(padding)
        )
    }
}


fun isValidImageType(context: Context, uri: Uri): Boolean {
    val mimeType = context.contentResolver.getType(uri)
    return mimeType == "image/jpeg" || mimeType == "image/png"
}

@Composable
fun BackHeader(title: String, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(4.dp)) // Tight spacing
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }
    }
    Log.d("BackHeader", "Title: $title")
}



//@Composable
//fun ProfileImage(capturedBitmap: ImageBitmap?) {
//    val modifier = Modifier
//        .size(140.dp)
//        .clip(CircleShape)
//        .border(1.dp, Color.White, CircleShape)
//
//    if (capturedBitmap != null) {
//        Image(
//            bitmap = capturedBitmap,
//            contentDescription = "Captured Image",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    } else {
//        Image(
//            painter = painterResource(id = R.drawable.profile),
//            contentDescription = "Default Avatar",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    }
//    Log.d("ProfileImage", "CapturedBitmap: $capturedBitmap")
//}
//
//
//
//
//
//
//@Composable
//fun PhotoOptionsMenu(
//    showMenu: Boolean,
//    onToggleMenu: () -> Unit,
//    onTakePhoto: () -> Unit,
//    onUpload: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier.offset(x = (20).dp),
//                text = "Photo Options",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            IconButton(onClick = onToggleMenu) {
//                Icon(
//                    modifier = Modifier.offset(10.dp),
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Show photo options"
//                )
//            }
//        }
//
//        DropdownMenu(
//            expanded = showMenu,
//            onDismissRequest = onToggleMenu
//        ) {
//            DropdownMenuItem(text = { Text("Take Photo") }, onClick = onTakePhoto)
//            DropdownMenuItem(text = { Text("Upload from Gallery") }, onClick = onUpload)
//            DropdownMenuItem(text = { Text("Delete Photo") }, onClick = onDelete)
//        }
//    }
//    Log.d("PhotoOptionsMenu", "ShowMenu: $showMenu")
//}
//
//
//
//
//@Composable
//fun AccountFormBottom(
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>
//) {
//    var genderExpanded by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = firstname.value,
//            onValueChange = { firstname.value = it },
//            label = { Text("First Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = lastname.value,
//            onValueChange = { lastname.value = it },
//            label = { Text("Last Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = dateOfBirth.value,
//            onValueChange = { dateOfBirth.value = it },
//            label = { Text("Date of Birth") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = homeAddress.value,
//            onValueChange = { homeAddress.value = it },
//            label = { Text("Home Address") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = city.value,
//            onValueChange = { city.value = it },
//            label = { Text("City") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = gender.value.name,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Gender") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Face2, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    IconButton(onClick = { genderExpanded = true }) {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            DropdownMenu(
//                expanded = genderExpanded,
//                onDismissRequest = { genderExpanded = false }
//            ) {
//                Gender.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = {
//                            gender.value = option
//                            genderExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Email") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = phoneNumber.value,
//            onValueChange = { phoneNumber.value = it },
//            label = { Text("Phone Number") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//
//
//
//@Composable
//fun HealthInformationSection(
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Health Information", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = medications.value,
//            onValueChange = { medications.value = it },
//            label = { Text("Medication") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = allergies.value,
//            onValueChange = { allergies.value = it },
//            label = { Text("Allergies") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
//            OutlinedTextField(
//                value = bloodGroup.value,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Blood Group") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Bloodtype, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { bloodGroupExpanded = true }
//            )
//
//            DropdownMenu(
//                expanded = bloodGroupExpanded,
//                onDismissRequest = { bloodGroupExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                bloodGroups.forEach { group ->
//                    DropdownMenuItem(
//                        text = { Text(group) },
//                        onClick = {
//                            bloodGroup.value = group
//                            bloodGroupExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//    Log.d("HealthInformationSection", "Blood Group: ${bloodGroup.value}")
//}
//
//
//
//@Composable
//fun EmergencyContactSection(
//    emergencyFirstname: MutableState<String>,
//    emergencyLastname: MutableState<String>,
//    emergencyPhone: MutableState<String>,
//    emergencyRelationship: MutableState<String>
//) {
//    var relationExpanded by remember { mutableStateOf(false) }
//    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
//
//    }
//    //Log.d("EmergencyContactSection", "Recomposing EmergencyContactSection")
//}
//
//
//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    profileBitmap: Bitmap?,
//
//    // User info
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,   // <-- FIXED (was Gender)
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>,
//
//    // Health info
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    val context = LocalContext.current
//
//    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
//    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // ---------------- USER OBJECT ----------------
//    val user by remember {
//        derivedStateOf {
//            User(
//                firstname = firstname.value,
//                lastname = lastname.value,
//                dateOfBirth = dateOfBirth.value,
//                homeAddress = homeAddress.value,
//                gender = gender.value,
//                email = email.value,
//                phoneNumber = phoneNumber.value,
//                city = city.value
//            )
//        }
//    }
//
//    // ---------------- VALIDATION ----------------
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//        } catch (e: Exception) {
//            isValid = false
//        }
//    }
//
//    // ---------------- CAMERA ----------------
//    val cameraLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//            bitmap?.let {
//                capturedBitmap = it
//                previewImage = it.asImageBitmap()
//            }
//        }
//
//    // ---------------- GALLERY ----------------
//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                capturedBitmap = bitmap
//                previewImage = bitmap.asImageBitmap()
//            }
//        }
//
//    // ---------------- UI ----------------
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("profile") },   // <-- FIXED to navigate to profile
//                onSave = {
//                    if (!isValid) return@TopBarWithSave
//
//                    isSaving = true
//
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            val healthInfo = HealthInformation(
//                                bloodGroup = bloodGroup.value,
//                                allergies = allergies.value,
//                                medication = medications.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeHealthInformation(healthInfo)
//                            }
//
//                            // ---------- CLEAR FIELDS ----------
//                            firstname.value = ""
//                            lastname.value = ""
//                            dateOfBirth.value = ""
//                            homeAddress.value = ""
//                            gender.value = Gender.OTHER        // <-- FIXED (gender is String)
//                            email.value = ""
//                            phoneNumber.value = ""
//                            city.value = ""
//
//                            bloodGroup.value = ""
//                            allergies.value = ""
//                            medications.value = ""
//
//                            previewImage = null      // <-- FIXED (was previewImage.value)
//                            capturedBitmap = null
//
//                            Log.d("SaveAction", "Saved successfully")
//                        } catch (e: Exception) {
//                            Log.e("SaveAction", "Save failed: ${e.message}")
//                        } finally {
//                            isSaving = false
//                        }
//                    }
//                },
//                enabled = isValid,
//                isSaving = isSaving
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            ProfileImage(previewImage)
//
//            PhotoOptionsMenu(
//                showMenu = showMenu,
//                onToggleMenu = { showMenu = !showMenu },
//                onTakePhoto = {
//                    showMenu = false
//                    cameraLauncher.launch(null)
//                },
//                onUpload = {
//                    showMenu = false
//                    galleryLauncher.launch("image/*")
//                },
//                onDelete = {
//                    showMenu = false
//                    capturedBitmap = null
//                    previewImage = null
//                }
//            )
//        }
//    }
//
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//
//
//
//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        AccountFormBottom(
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city
//        )
//
//        HealthInformationSection(
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications
//        )
//
//
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}



//@Composable
//fun ProfileImage(capturedBitmap: ImageBitmap?) {
//    val modifier = Modifier
//        .size(140.dp)
//        .clip(CircleShape)
//        .border(1.dp, Color.White, CircleShape)
//
//    if (capturedBitmap != null) {
//        Image(
//            bitmap = capturedBitmap,
//            contentDescription = "Captured Image",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    } else {
//        Image(
//            painter = painterResource(id = R.drawable.profile),
//            contentDescription = "Default Avatar",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    }
//    Log.d("ProfileImage", "CapturedBitmap: $capturedBitmap")
//}
//
//@Composable
//fun PhotoOptionsMenu(
//    showMenu: Boolean,
//    onToggleMenu: () -> Unit,
//    onTakePhoto: () -> Unit,
//    onUpload: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier.offset(x = (20).dp),
//                text = "Photo Options",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            IconButton(onClick = onToggleMenu) {
//                Icon(
//                    modifier = Modifier.offset(10.dp),
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Show photo options"
//                )
//            }
//        }
//
//        DropdownMenu(
//            expanded = showMenu,
//            onDismissRequest = onToggleMenu
//        ) {
//            DropdownMenuItem(text = { Text("Take Photo") }, onClick = onTakePhoto)
//            DropdownMenuItem(text = { Text("Upload from Gallery") }, onClick = onUpload)
//            DropdownMenuItem(text = { Text("Delete Photo") }, onClick = onDelete)
//        }
//    }
//    Log.d("PhotoOptionsMenu", "ShowMenu: $showMenu")
//}
//
//@Composable
//fun AccountFormBottom(
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>
//) {
//    var genderExpanded by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = firstname.value,
//            onValueChange = { firstname.value = it },
//            label = { Text("First Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = lastname.value,
//            onValueChange = { lastname.value = it },
//            label = { Text("Last Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = dateOfBirth.value,
//            onValueChange = { dateOfBirth.value = it },
//            label = { Text("Date of Birth") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = homeAddress.value,
//            onValueChange = { homeAddress.value = it },
//            label = { Text("Home Address") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = city.value,
//            onValueChange = { city.value = it },
//            label = { Text("City") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = gender.value.name,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Gender") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Face2, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    IconButton(onClick = { genderExpanded = true }) {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            DropdownMenu(
//                expanded = genderExpanded,
//                onDismissRequest = { genderExpanded = false }
//            ) {
//                Gender.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = {
//                            gender.value = option
//                            genderExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Email") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = phoneNumber.value,
//            onValueChange = { phoneNumber.value = it },
//            label = { Text("Phone Number") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//@Composable
//fun HealthInformationSection(
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Health Information", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = medications.value,
//            onValueChange = { medications.value = it },
//            label = { Text("Medication") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = allergies.value,
//            onValueChange = { allergies.value = it },
//            label = { Text("Allergies") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
//            OutlinedTextField(
//                value = bloodGroup.value,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Blood Group") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Bloodtype, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { bloodGroupExpanded = true }
//            )
//
//            DropdownMenu(
//                expanded = bloodGroupExpanded,
//                onDismissRequest = { bloodGroupExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                bloodGroups.forEach { group ->
//                    DropdownMenuItem(
//                        text = { Text(group) },
//                        onClick = {
//                            bloodGroup.value = group
//                            bloodGroupExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//    Log.d("HealthInformationSection", "Blood Group: ${bloodGroup.value}")
//}
//
//@Composable
//fun EmergencyContactSection(
//    emergencyFirstname: MutableState<String>,
//    emergencyLastname: MutableState<String>,
//    emergencyPhone: MutableState<String>,
//    emergencyRelationship: MutableState<String>
//) {
//    var relationExpanded by remember { mutableStateOf(false) }
//    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
//    }
//}
//
//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    profileBitmap: Bitmap?,
//
//    // User info
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>,
//
//    // Health info
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    val context = LocalContext.current
//
//    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
//    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // ---------------- USER OBJECT ----------------
//    val user by remember {
//        derivedStateOf {
//            User(
//                firstname = firstname.value,
//                lastname = lastname.value,
//                dateOfBirth = dateOfBirth.value,
//                homeAddress = homeAddress.value,
//                gender = gender.value,
//                email = email.value,
//                phoneNumber = phoneNumber.value,
//                city = city.value
//            )
//        }
//    }
//
//    // ---------------- VALIDATION ----------------
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//        } catch (e: Exception) {
//            isValid = false
//        }
//    }
//
//    // ---------------- CAMERA ----------------
//    val cameraLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//            bitmap?.let {
//                capturedBitmap = it
//                previewImage = it.asImageBitmap()
//            }
//        }
//
//    // ---------------- GALLERY ----------------
//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                capturedBitmap = bitmap
//                previewImage = bitmap.asImageBitmap()
//            }
//        }
//
//    // ---------------- UI ----------------
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("profile") },
//                onSave = {
//                    if (!isValid) return@TopBarWithSave
//
//                    isSaving = true
//
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            val healthInfo = HealthInformation(
//                                bloodGroup = bloodGroup.value,
//                                allergies = allergies.value,
//                                medication = medications.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                // This automatically handles image upload and old image deletion
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeHealthInformation(healthInfo)
//                            }
//
//                            // ---------- CLEAR FIELDS ----------
//                            firstname.value = ""
//                            lastname.value = ""
//                            dateOfBirth.value = ""
//                            homeAddress.value = ""
//                            gender.value = Gender.OTHER
//                            email.value = ""
//                            phoneNumber.value = ""
//                            city.value = ""
//
//                            bloodGroup.value = ""
//                            allergies.value = ""
//                            medications.value = ""
//
//                            previewImage = null
//                            capturedBitmap = null
//
//                            Log.d("SaveAction", "Saved successfully")
//                        } catch (e: Exception) {
//                            Log.e("SaveAction", "Save failed: ${e.message}")
//                        } finally {
//                            isSaving = false
//                        }
//                    }
//                },
//                enabled = isValid,
//                isSaving = isSaving
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            ProfileImage(previewImage)
//
//            PhotoOptionsMenu(
//                showMenu = showMenu,
//                onToggleMenu = { showMenu = !showMenu },
//                onTakePhoto = {
//                    showMenu = false
//                    cameraLauncher.launch(null)
//                },
//                onUpload = {
//                    showMenu = false
//                    galleryLauncher.launch("image/*")
//                },
//                onDelete = {
//                    showMenu = false
//
//                    // Delete from Firebase Storage and Firestore
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.deleteUserProfileImage()
//                            }
//                            capturedBitmap = null
//                            previewImage = null
//                            Log.d("DeletePhoto", "Profile image deleted successfully")
//                        } catch (e: Exception) {
//                            Log.e("DeletePhoto", "Failed to delete: ${e.message}")
//                        }
//                    }
//                }
//            )
//        }
//    }
//
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        AccountFormBottom(
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city
//        )
//
//        HealthInformationSection(
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications
//        )
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}

//
//object TempProfileStorage {
//    var tempProfileBitmap: Bitmap? = null
//}
//
//// ============================================
//// ACCOUNT FORM SCREEN COMPOSABLES
//// ============================================
//
//@Composable
//fun ProfileImage(capturedBitmap: ImageBitmap?) {
//    val modifier = Modifier
//        .size(140.dp)
//        .clip(CircleShape)
//        .border(1.dp, Color.White, CircleShape)
//
//    if (capturedBitmap != null) {
//        Image(
//            bitmap = capturedBitmap,
//            contentDescription = "Captured Image",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    } else {
//        Image(
//            painter = painterResource(id = R.drawable.profile),
//            contentDescription = "Default Avatar",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    }
//    Log.d("ProfileImage", "CapturedBitmap: $capturedBitmap")
//}
//
//@Composable
//fun PhotoOptionsMenu(
//    showMenu: Boolean,
//    onToggleMenu: () -> Unit,
//    onTakePhoto: () -> Unit,
//    onUpload: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier.offset(x = (20).dp),
//                text = "Photo Options",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            IconButton(onClick = onToggleMenu) {
//                Icon(
//                    modifier = Modifier.offset(10.dp),
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Show photo options"
//                )
//            }
//        }
//
//        DropdownMenu(
//            expanded = showMenu,
//            onDismissRequest = onToggleMenu
//        ) {
//            DropdownMenuItem(text = { Text("Take Photo") }, onClick = onTakePhoto)
//            DropdownMenuItem(text = { Text("Upload from Gallery") }, onClick = onUpload)
//            DropdownMenuItem(text = { Text("Delete Photo") }, onClick = onDelete)
//        }
//    }
//    Log.d("PhotoOptionsMenu", "ShowMenu: $showMenu")
//}
//
//@Composable
//fun AccountFormBottom(
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>
//) {
//    var genderExpanded by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = firstname.value,
//            onValueChange = { firstname.value = it },
//            label = { Text("First Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = lastname.value,
//            onValueChange = { lastname.value = it },
//            label = { Text("Last Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = dateOfBirth.value,
//            onValueChange = { dateOfBirth.value = it },
//            label = { Text("Date of Birth") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = homeAddress.value,
//            onValueChange = { homeAddress.value = it },
//            label = { Text("Home Address") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = city.value,
//            onValueChange = { city.value = it },
//            label = { Text("City") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = gender.value.name,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Gender") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Face2, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    IconButton(onClick = { genderExpanded = true }) {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            DropdownMenu(
//                expanded = genderExpanded,
//                onDismissRequest = { genderExpanded = false }
//            ) {
//                Gender.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = {
//                            gender.value = option
//                            genderExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Email") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = phoneNumber.value,
//            onValueChange = { phoneNumber.value = it },
//            label = { Text("Phone Number") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//@Composable
//fun HealthInformationSection(
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Health Information", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = medications.value,
//            onValueChange = { medications.value = it },
//            label = { Text("Medication") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = allergies.value,
//            onValueChange = { allergies.value = it },
//            label = { Text("Allergies") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
//            OutlinedTextField(
//                value = bloodGroup.value,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Blood Group") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Bloodtype, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { bloodGroupExpanded = true }
//            )
//
//            DropdownMenu(
//                expanded = bloodGroupExpanded,
//                onDismissRequest = { bloodGroupExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                bloodGroups.forEach { group ->
//                    DropdownMenuItem(
//                        text = { Text(group) },
//                        onClick = {
//                            bloodGroup.value = group
//                            bloodGroupExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//    Log.d("HealthInformationSection", "Blood Group: ${bloodGroup.value}")
//}
//
//@Composable
//fun EmergencyContactSection(
//    emergencyFirstname: MutableState<String>,
//    emergencyLastname: MutableState<String>,
//    emergencyPhone: MutableState<String>,
//    emergencyRelationship: MutableState<String>
//) {
//    var relationExpanded by remember { mutableStateOf(false) }
//    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
//    }
//}
//
//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    profileBitmap: Bitmap?,
//
//    // User info
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>,
//
//    // Health info
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    val context = LocalContext.current
//
//    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
//    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // ---------------- USER OBJECT ----------------
//    val user by remember {
//        derivedStateOf {
//            User(
//                firstname = firstname.value,
//                lastname = lastname.value,
//                dateOfBirth = dateOfBirth.value,
//                homeAddress = homeAddress.value,
//                gender = gender.value,
//                email = email.value,
//                phoneNumber = phoneNumber.value,
//                city = city.value
//            )
//        }
//    }
//
//    // ---------------- VALIDATION ----------------
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//        } catch (e: Exception) {
//            isValid = false
//        }
//    }
//
//    // ---------------- CAMERA ----------------
//    val cameraLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//            bitmap?.let {
//                capturedBitmap = it
//                previewImage = it.asImageBitmap()
//                // SAVE TO TEMP STORAGE
//                TempProfileStorage.tempProfileBitmap = it
//                Log.d("AccountFormTop", "Camera image saved to temp storage")
//            }
//        }
//
//    // ---------------- GALLERY ----------------
//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                capturedBitmap = bitmap
//                previewImage = bitmap.asImageBitmap()
//                // SAVE TO TEMP STORAGE
//                TempProfileStorage.tempProfileBitmap = bitmap
//                Log.d("AccountFormTop", "Gallery image saved to temp storage")
//            }
//        }
//
//    // ---------------- UI ----------------
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("profile") },
//                onSave = {
//                    if (!isValid) return@TopBarWithSave
//
//                    isSaving = true
//
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            val healthInfo = HealthInformation(
//                                bloodGroup = bloodGroup.value,
//                                allergies = allergies.value,
//                                medication = medications.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                // This automatically handles image upload and old image deletion
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeHealthInformation(healthInfo)
//                            }
//
//                            // ---------- CLEAR FIELDS ----------
//                            firstname.value = ""
//                            lastname.value = ""
//                            dateOfBirth.value = ""
//                            homeAddress.value = ""
//                            gender.value = Gender.OTHER
//                            email.value = ""
//                            phoneNumber.value = ""
//                            city.value = ""
//
//                            bloodGroup.value = ""
//                            allergies.value = ""
//                            medications.value = ""
//
//                            previewImage = null
//                            capturedBitmap = null
//
//                            // KEEP TEMP STORAGE - Don't clear it so Account screen can use it
//                            Log.d("SaveAction", "Saved successfully - temp storage preserved")
//                        } catch (e: Exception) {
//                            Log.e("SaveAction", "Save failed: ${e.message}")
//                        } finally {
//                            isSaving = false
//                        }
//                    }
//                },
//                enabled = isValid,
//                isSaving = isSaving
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            ProfileImage(previewImage)
//
//            PhotoOptionsMenu(
//                showMenu = showMenu,
//                onToggleMenu = { showMenu = !showMenu },
//                onTakePhoto = {
//                    showMenu = false
//                    cameraLauncher.launch(null)
//                },
//                onUpload = {
//                    showMenu = false
//                    galleryLauncher.launch("image/*")
//                },
//                onDelete = {
//                    showMenu = false
//
//                    // Delete from Firebase Storage and Firestore
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.deleteUserProfileImage()
//                            }
//                            capturedBitmap = null
//                            previewImage = null
//                            // CLEAR TEMP STORAGE
//                            TempProfileStorage.tempProfileBitmap = null
//                            Log.d("DeletePhoto", "Profile image deleted successfully")
//                        } catch (e: Exception) {
//                            Log.e("DeletePhoto", "Failed to delete: ${e.message}")
//                        }
//                    }
//                }
//            )
//        }
//    }
//
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        AccountFormBottom(
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city
//        )
//
//        HealthInformationSection(
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications
//        )
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//



// ============================================
// GLOBAL STATE TO STORE TEMPORARY PROFILE IMAGE
// ============================================
//object TempProfileStorage {
//    var tempProfileBitmap: Bitmap? = null
//}
//
//// ============================================
//// ACCOUNT FORM SCREEN COMPOSABLES
//// ============================================
//
//@Composable
//fun ProfileImage(capturedBitmap: ImageBitmap?) {
//    val modifier = Modifier
//        .size(140.dp)
//        .clip(CircleShape)
//        .border(1.dp, Color.White, CircleShape)
//
//    if (capturedBitmap != null) {
//        Image(
//            bitmap = capturedBitmap,
//            contentDescription = "Captured Image",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    } else {
//        Image(
//            painter = painterResource(id = R.drawable.profile),
//            contentDescription = "Default Avatar",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    }
//    Log.d("ProfileImage", "CapturedBitmap: $capturedBitmap")
//}
//
//@Composable
//fun PhotoOptionsMenu(
//    showMenu: Boolean,
//    onToggleMenu: () -> Unit,
//    onTakePhoto: () -> Unit,
//    onUpload: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier.offset(x = (20).dp),
//                text = "Photo Options",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            IconButton(onClick = onToggleMenu) {
//                Icon(
//                    modifier = Modifier.offset(10.dp),
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Show photo options"
//                )
//            }
//        }
//
//        DropdownMenu(
//            expanded = showMenu,
//            onDismissRequest = onToggleMenu
//        ) {
//            DropdownMenuItem(text = { Text("Take Photo") }, onClick = onTakePhoto)
//            DropdownMenuItem(text = { Text("Upload from Gallery") }, onClick = onUpload)
//            DropdownMenuItem(text = { Text("Delete Photo") }, onClick = onDelete)
//        }
//    }
//    Log.d("PhotoOptionsMenu", "ShowMenu: $showMenu")
//}
//
//@Composable
//fun AccountFormBottom(
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>
//) {
//    var genderExpanded by remember { mutableStateOf(false) }
//    var showDatePicker by remember { mutableStateOf(false) }
//    val focusManager = LocalFocusManager.current
//    val datePickerState = rememberDatePickerState()
//
//    // Date Picker Dialog
//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    datePickerState.selectedDateMillis?.let { millis ->
//                        val date = java.util.Date(millis)
//                        val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
//                        dateOfBirth.value = formatter.format(date)
//                    }
//                    showDatePicker = false
//                }) {
//                    Text("OK")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) {
//                    Text("Cancel")
//                }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = firstname.value,
//            onValueChange = { firstname.value = it },
//            label = { Text("First Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = lastname.value,
//            onValueChange = { lastname.value = it },
//            label = { Text("Last Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = dateOfBirth.value,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Date of Birth") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            trailingIcon = {
//                IconButton(onClick = { showDatePicker = true }) {
//                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { showDatePicker = true }
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = homeAddress.value,
//            onValueChange = { homeAddress.value = it },
//            label = { Text("Home Address") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = city.value,
//            onValueChange = { city.value = it },
//            label = { Text("City") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = gender.value.name,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Gender") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Face2, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    IconButton(onClick = { genderExpanded = true }) {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            DropdownMenu(
//                expanded = genderExpanded,
//                onDismissRequest = { genderExpanded = false }
//            ) {
//                Gender.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = {
//                            gender.value = option
//                            genderExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Email") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = phoneNumber.value,
//            onValueChange = { phoneNumber.value = it },
//            label = { Text("Phone Number") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Phone,
//                imeAction = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//@Composable
//fun HealthInformationSection(
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Health Information", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = medications.value,
//            onValueChange = { medications.value = it },
//            label = { Text("Medication") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = allergies.value,
//            onValueChange = { allergies.value = it },
//            label = { Text("Allergies") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
//            OutlinedTextField(
//                value = bloodGroup.value,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Blood Group") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Bloodtype, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { bloodGroupExpanded = true }
//            )
//
//            DropdownMenu(
//                expanded = bloodGroupExpanded,
//                onDismissRequest = { bloodGroupExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                bloodGroups.forEach { group ->
//                    DropdownMenuItem(
//                        text = { Text(group) },
//                        onClick = {
//                            bloodGroup.value = group
//                            bloodGroupExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//    Log.d("HealthInformationSection", "Blood Group: ${bloodGroup.value}")
//}
//
//@Composable
//fun EmergencyContactSection(
//    emergencyFirstname: MutableState<String>,
//    emergencyLastname: MutableState<String>,
//    emergencyPhone: MutableState<String>,
//    emergencyRelationship: MutableState<String>
//) {
//    var relationExpanded by remember { mutableStateOf(false) }
//    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
//    }
//}
//
//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    profileBitmap: Bitmap?,
//
//    // User info
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>,
//
//    // Health info
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    val context = LocalContext.current
//
//    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
//    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // ---------------- USER OBJECT ----------------
//    val user by remember {
//        derivedStateOf {
//            User(
//                firstname = firstname.value,
//                lastname = lastname.value,
//                dateOfBirth = dateOfBirth.value,
//                homeAddress = homeAddress.value,
//                gender = gender.value,
//                email = email.value,
//                phoneNumber = phoneNumber.value,
//                city = city.value
//            )
//        }
//    }
//
//    // ---------------- VALIDATION ----------------
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//        } catch (e: Exception) {
//            isValid = false
//        }
//    }
//
//    // ---------------- CAMERA ----------------
//    val cameraLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//            bitmap?.let {
//                capturedBitmap = it
//                previewImage = it.asImageBitmap()
//                // SAVE TO TEMP STORAGE
//                TempProfileStorage.tempProfileBitmap = it
//                Log.d("AccountFormTop", "Camera image saved to temp storage")
//            }
//        }
//
//    // ---------------- GALLERY ----------------
//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                capturedBitmap = bitmap
//                previewImage = bitmap.asImageBitmap()
//                // SAVE TO TEMP STORAGE
//                TempProfileStorage.tempProfileBitmap = bitmap
//                Log.d("AccountFormTop", "Gallery image saved to temp storage")
//            }
//        }
//
//    // ---------------- UI ----------------
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("profile") },
//                onSave = {
//                    if (!isValid) return@TopBarWithSave
//
//                    isSaving = true
//
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            val healthInfo = HealthInformation(
//                                bloodGroup = bloodGroup.value,
//                                allergies = allergies.value,
//                                medication = medications.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                // This automatically handles image upload and old image deletion
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeHealthInformation(healthInfo)
//                            }
//
//                            // ---------- CLEAR FIELDS ----------
//                            firstname.value = ""
//                            lastname.value = ""
//                            dateOfBirth.value = ""
//                            homeAddress.value = ""
//                            gender.value = Gender.OTHER
//                            email.value = ""
//                            phoneNumber.value = ""
//                            city.value = ""
//
//                            bloodGroup.value = ""
//                            allergies.value = ""
//                            medications.value = ""
//
//                            previewImage = null
//                            capturedBitmap = null
//
//                            // KEEP TEMP STORAGE - Don't clear it so Account screen can use it
//                            Log.d("SaveAction", "Saved successfully - temp storage preserved")
//                        } catch (e: Exception) {
//                            Log.e("SaveAction", "Save failed: ${e.message}")
//                        } finally {
//                            isSaving = false
//                        }
//                    }
//                },
//                enabled = isValid,
//                isSaving = isSaving
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            ProfileImage(previewImage)
//
//            PhotoOptionsMenu(
//                showMenu = showMenu,
//                onToggleMenu = { showMenu = !showMenu },
//                onTakePhoto = {
//                    showMenu = false
//                    cameraLauncher.launch(null)
//                },
//                onUpload = {
//                    showMenu = false
//                    galleryLauncher.launch("image/*")
//                },
//                onDelete = {
//                    showMenu = false
//
//                    // Delete from Firebase Storage and Firestore
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.deleteUserProfileImage()
//                            }
//                            capturedBitmap = null
//                            previewImage = null
//                            // CLEAR TEMP STORAGE
//                            TempProfileStorage.tempProfileBitmap = null
//                            Log.d("DeletePhoto", "Profile image deleted successfully")
//                        } catch (e: Exception) {
//                            Log.e("DeletePhoto", "Failed to delete: ${e.message}")
//                        }
//                    }
//                }
//            )
//        }
//    }
//
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        AccountFormBottom(
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city
//        )
//
//        HealthInformationSection(
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications
//        )
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//
//
//



// ============================================
// GLOBAL STATE TO STORE TEMPORARY PROFILE IMAGE
// ============================================
//object TempProfileStorage {
//    var tempProfileBitmap: Bitmap? = null
//}
//
//// ============================================
//// ACCOUNT FORM SCREEN COMPOSABLES
//// ============================================
//
//@Composable
//fun ProfileImage(capturedBitmap: ImageBitmap?) {
//    val modifier = Modifier
//        .size(140.dp)
//        .clip(CircleShape)
//        .border(1.dp, Color.White, CircleShape)
//
//    if (capturedBitmap != null) {
//        Image(
//            bitmap = capturedBitmap,
//            contentDescription = "Captured Image",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    } else {
//        Image(
//            painter = painterResource(id = R.drawable.profile),
//            contentDescription = "Default Avatar",
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
//    }
//    Log.d("ProfileImage", "CapturedBitmap: $capturedBitmap")
//}
//
//@Composable
//fun PhotoOptionsMenu(
//    showMenu: Boolean,
//    onToggleMenu: () -> Unit,
//    onTakePhoto: () -> Unit,
//    onUpload: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                modifier = Modifier.offset(x = (20).dp),
//                text = "Photo Options",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            IconButton(onClick = onToggleMenu) {
//                Icon(
//                    modifier = Modifier.offset(10.dp),
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Show photo options"
//                )
//            }
//        }
//
//        DropdownMenu(
//            expanded = showMenu,
//            onDismissRequest = onToggleMenu
//        ) {
//            DropdownMenuItem(text = { Text("Take Photo") }, onClick = onTakePhoto)
//            DropdownMenuItem(text = { Text("Upload from Gallery") }, onClick = onUpload)
//            DropdownMenuItem(text = { Text("Delete Photo") }, onClick = onDelete)
//        }
//    }
//    Log.d("PhotoOptionsMenu", "ShowMenu: $showMenu")
//}
//
//@Composable
//fun AccountFormBottom(
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>
//) {
//    var genderExpanded by remember { mutableStateOf(false) }
//    var showDatePicker by remember { mutableStateOf(false) }
//    val focusManager = LocalFocusManager.current
//    val datePickerState = rememberDatePickerState()
//
//    // Date Picker Dialog
//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    datePickerState.selectedDateMillis?.let { millis ->
//                        val date = Date(millis)
//                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                        dateOfBirth.value = formatter.format(date)
//                    }
//                    showDatePicker = false
//                    focusManager.moveFocus(FocusDirection.Down)
//                }) {
//                    Text("OK")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) {
//                    Text("Cancel")
//                }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        OutlinedTextField(
//            value = firstname.value,
//            onValueChange = { firstname.value = it },
//            label = { Text("First Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = lastname.value,
//            onValueChange = { lastname.value = it },
//            label = { Text("Last Name") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = dateOfBirth.value,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Date of Birth") },
//                placeholder = { Text("DD/MM/YYYY") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    IconButton(onClick = { showDatePicker = true }) {
//                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//            // Invisible clickable layer to trigger date picker
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { showDatePicker = true }
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = homeAddress.value,
//            onValueChange = { homeAddress.value = it },
//            label = { Text("Home Address") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = city.value,
//            onValueChange = { city.value = it },
//            label = { Text("City") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth()) {
//            OutlinedTextField(
//                value = gender.value.name,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Gender") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Face2, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    IconButton(onClick = { genderExpanded = true }) {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            DropdownMenu(
//                expanded = genderExpanded,
//                onDismissRequest = { genderExpanded = false }
//            ) {
//                Gender.entries.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option.name) },
//                        onClick = {
//                            gender.value = option
//                            genderExpanded = false
//                            focusManager.moveFocus(FocusDirection.Down)
//                        }
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Email") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = phoneNumber.value,
//            onValueChange = { phoneNumber.value = it },
//            label = { Text("Phone Number") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Phone,
//                imeAction = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = { focusManager.clearFocus() }
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//@Composable
//fun HealthInformationSection(
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Health Information", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = medications.value,
//            onValueChange = { medications.value = it },
//            label = { Text("Medication") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = allergies.value,
//            onValueChange = { allergies.value = it },
//            label = { Text("Allergies") },
//            leadingIcon = {
//                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
//            OutlinedTextField(
//                value = bloodGroup.value,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Blood Group") },
//                leadingIcon = {
//                    RoundedIcon(Icons.Default.Bloodtype, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { bloodGroupExpanded = true }
//            )
//
//            DropdownMenu(
//                expanded = bloodGroupExpanded,
//                onDismissRequest = { bloodGroupExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                bloodGroups.forEach { group ->
//                    DropdownMenuItem(
//                        text = { Text(group) },
//                        onClick = {
//                            bloodGroup.value = group
//                            bloodGroupExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//    Log.d("HealthInformationSection", "Blood Group: ${bloodGroup.value}")
//}
//
//@Composable
//fun EmergencyContactSection(
//    emergencyFirstname: MutableState<String>,
//    emergencyLastname: MutableState<String>,
//    emergencyPhone: MutableState<String>,
//    emergencyRelationship: MutableState<String>
//) {
//    var relationExpanded by remember { mutableStateOf(false) }
//    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
//    }
//}
//
//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    profileBitmap: Bitmap?,
//
//    // User info
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>,
//
//    // Health info
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//    val context = LocalContext.current
//
//    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
//    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // ---------------- USER OBJECT ----------------
//    val user by remember {
//        derivedStateOf {
//            User(
//                firstname = firstname.value,
//                lastname = lastname.value,
//                dateOfBirth = dateOfBirth.value,
//                homeAddress = homeAddress.value,
//                gender = gender.value,
//                email = email.value,
//                phoneNumber = phoneNumber.value,
//                city = city.value
//            )
//        }
//    }
//
//    // ---------------- VALIDATION ----------------
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//        } catch (e: Exception) {
//            isValid = false
//        }
//    }
//
//    // ---------------- CAMERA ----------------
//    val cameraLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//            bitmap?.let {
//                capturedBitmap = it
//                previewImage = it.asImageBitmap()
//                // SAVE TO TEMP STORAGE
//                TempProfileStorage.tempProfileBitmap = it
//                Log.d("AccountFormTop", "Camera image saved to temp storage")
//            }
//        }
//
//    // ---------------- GALLERY ----------------
//    val galleryLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                capturedBitmap = bitmap
//                previewImage = bitmap.asImageBitmap()
//                // SAVE TO TEMP STORAGE
//                TempProfileStorage.tempProfileBitmap = bitmap
//                Log.d("AccountFormTop", "Gallery image saved to temp storage")
//            }
//        }
//
//    // ---------------- UI ----------------
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("profile") },
//                onSave = {
//                    if (!isValid) return@TopBarWithSave
//
//                    isSaving = true
//
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            val healthInfo = HealthInformation(
//                                bloodGroup = bloodGroup.value,
//                                allergies = allergies.value,
//                                medication = medications.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                // This automatically handles image upload and old image deletion
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeHealthInformation(healthInfo)
//                            }
//
//                            // ---------- CLEAR FIELDS ----------
//                            firstname.value = ""
//                            lastname.value = ""
//                            dateOfBirth.value = ""
//                            homeAddress.value = ""
//                            gender.value = Gender.OTHER
//                            email.value = ""
//                            phoneNumber.value = ""
//                            city.value = ""
//
//                            bloodGroup.value = ""
//                            allergies.value = ""
//                            medications.value = ""
//
//                            previewImage = null
//                            capturedBitmap = null
//
//                            // KEEP TEMP STORAGE - Don't clear it so Account screen can use it
//                            Log.d("SaveAction", "Saved successfully - temp storage preserved")
//                        } catch (e: Exception) {
//                            Log.e("SaveAction", "Save failed: ${e.message}")
//                        } finally {
//                            isSaving = false
//                        }
//                    }
//                },
//                enabled = isValid,
//                isSaving = isSaving
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            ProfileImage(previewImage)
//
//            PhotoOptionsMenu(
//                showMenu = showMenu,
//                onToggleMenu = { showMenu = !showMenu },
//                onTakePhoto = {
//                    showMenu = false
//                    cameraLauncher.launch(null)
//                },
//                onUpload = {
//                    showMenu = false
//                    galleryLauncher.launch("image/*")
//                },
//                onDelete = {
//                    showMenu = false
//
//                    // Delete from Firebase Storage and Firestore
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.deleteUserProfileImage()
//                            }
//                            capturedBitmap = null
//                            previewImage = null
//                            // CLEAR TEMP STORAGE
//                            TempProfileStorage.tempProfileBitmap = null
//                            Log.d("DeletePhoto", "Profile image deleted successfully")
//                        } catch (e: Exception) {
//                            Log.e("DeletePhoto", "Failed to delete: ${e.message}")
//                        }
//                    }
//                }
//            )
//        }
//    }
//
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}
//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        AccountFormBottom(
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city
//        )
//
//        HealthInformationSection(
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications
//        )
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}



// ============================================
// GLOBAL STATE TO STORE TEMPORARY PROFILE IMAGE
// ============================================
object TempProfileStorage {
    var tempProfileBitmap: Bitmap? = null
}

// ============================================
// ACCOUNT FORM SCREEN COMPOSABLES
// ============================================

@Composable
fun ProfileImage(capturedBitmap: ImageBitmap?) {
    val modifier = Modifier
        .size(140.dp)
        .clip(CircleShape)
        .border(1.dp, Color.White, CircleShape)

    if (capturedBitmap != null) {
        Image(
            bitmap = capturedBitmap,
            contentDescription = "Captured Image",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Default Avatar",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
    Log.d("ProfileImage", "CapturedBitmap: $capturedBitmap")
}

@Composable
fun PhotoOptionsMenu(
    showMenu: Boolean,
    onToggleMenu: () -> Unit,
    onTakePhoto: () -> Unit,
    onUpload: () -> Unit,
    onDelete: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onToggleMenu)
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.offset(x = (20).dp),
                text = "Photo Options",
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                modifier = Modifier.offset(20.dp),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show photo options"
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = onToggleMenu
        ) {
            DropdownMenuItem(text = { Text("Take Photo") }, onClick = onTakePhoto)
            DropdownMenuItem(text = { Text("Upload from Gallery") }, onClick = onUpload)
            DropdownMenuItem(text = { Text("Delete Photo") }, onClick = onDelete)
        }
    }
    Log.d("PhotoOptionsMenu", "ShowMenu: $showMenu")
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AccountFormBottom(
    firstname: MutableState<String>,
    lastname: MutableState<String>,
    dateOfBirth: MutableState<String>,
    homeAddress: MutableState<String>,
    gender: MutableState<Gender>,
    email: MutableState<String>,
    phoneNumber: MutableState<String>,
    city: MutableState<String>
) {
    var genderExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val datePickerState = rememberDatePickerState()

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        dateOfBirth.value = formatter.format(date)
                    }
                    showDatePicker = false
                    focusManager.moveFocus(FocusDirection.Down)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = firstname.value,
            onValueChange = { firstname.value = it },
            label = { Text("First Name") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastname.value,
            onValueChange = { lastname.value = it },
            label = { Text("Last Name") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = dateOfBirth.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Date of Birth") },
                placeholder = { Text("DD/MM/YYYY") },
                leadingIcon = {
                    RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                            showDatePicker = true
                            true
                        } else {
                            false
                        }
                    }
            )
            // Invisible clickable layer to trigger date picker
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = homeAddress.value,
            onValueChange = { homeAddress.value = it },
            label = { Text("Home Address") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = city.value,
            onValueChange = { city.value = it },
            label = { Text("City") },
            leadingIcon = {
                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = gender.value.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                leadingIcon = {
                    RoundedIcon(Icons.Default.Face2, Color(0xFF87CEEB), Color(0xFFEF6C00))
                },
                trailingIcon = {
                    IconButton(onClick = { genderExpanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Gender")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                            genderExpanded = true
                            true
                        } else {
                            false
                        }
                    }
            )

            // Invisible clickable overlay to make entire field clickable
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { genderExpanded = true }
            )

            DropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                Gender.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = {
                            gender.value = option
                            genderExpanded = false
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Phone Number") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.clearFocus()
                        true
                    } else {
                        false
                    }
                }
        )
    }
}

@Composable
fun HealthInformationSection(
    bloodGroup: MutableState<String>,
    allergies: MutableState<String>,
    medications: MutableState<String>
) {
    var bloodGroupExpanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Health Information", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = medications.value,
            onValueChange = { medications.value = it },
            label = { Text("Medication") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))



        OutlinedTextField(
            value = allergies.value,
            onValueChange = { allergies.value = it },
            label = { Text("Allergies") },
            leadingIcon = {
                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier
                .fillMaxWidth()
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        //val focusManager = LocalFocusManager.current

        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
            OutlinedTextField(
                value = bloodGroup.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Blood Group") },
                leadingIcon = {
                    RoundedIcon(Icons.Default.Bloodtype, Color(0xFF87CEEB), Color(0xFFEF6C00))
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else {
                            false
                        }
                    }
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { bloodGroupExpanded = true }
            )

            DropdownMenu(
                expanded = bloodGroupExpanded,
                onDismissRequest = { bloodGroupExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                bloodGroups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group) },
                        onClick = {
                            bloodGroup.value = group
                            bloodGroupExpanded = false
                        }
                    )
                }
            }
        }
    }
    Log.d("HealthInformationSection", "Blood Group: ${bloodGroup.value}")
}

@Composable
fun EmergencyContactSection(
    emergencyFirstname: MutableState<String>,
    emergencyLastname: MutableState<String>,
    emergencyPhone: MutableState<String>,
    emergencyRelationship: MutableState<String>
) {
    var relationExpanded by remember { mutableStateOf(false) }
    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
    }
}
@Composable
fun AccountFormTop(
    navController: NavHostController,
    profileBitmap: Bitmap?,

    // User info
    firstname: MutableState<String>,
    lastname: MutableState<String>,
    dateOfBirth: MutableState<String>,
    homeAddress: MutableState<String>,
    gender: MutableState<Gender>,
    email: MutableState<String>,
    phoneNumber: MutableState<String>,
    city: MutableState<String>,

    // Health info
    bloodGroup: MutableState<String>,
    allergies: MutableState<String>,
    medications: MutableState<String>
) {
    val context = LocalContext.current

    // FIX 1: Add 'profileBitmap' as a key to remember.
    // If the parent passes a new image, this state will now update correctly.
    var capturedBitmap by remember(profileBitmap) { mutableStateOf(profileBitmap) }

    // Initialize preview based on current capturedBitmap
    var previewImage by remember(capturedBitmap) {
        mutableStateOf(capturedBitmap?.asImageBitmap())
    }

    var showMenu by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // ---------------- USER OBJECT ----------------
    val user by remember {
        derivedStateOf {
            User(
                firstname = firstname.value,
                lastname = lastname.value,
                dateOfBirth = dateOfBirth.value,
                homeAddress = homeAddress.value,
                gender = gender.value,
                email = email.value,
                phoneNumber = phoneNumber.value,
                city = city.value
            )
        }
    }

    // ---------------- VALIDATION ----------------
    LaunchedEffect(user) {
        try {
            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
            isValid = validateUserInput(user, firebaseEmail)
        } catch (e: Exception) {
            isValid = false
        }
    }

    // ---------------- CAMERA LAUNCHER ----------------
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                capturedBitmap = it
                TempProfileStorage.tempProfileBitmap = it
            }
        }

    // ---------------- PERMISSION LAUNCHER ----------------
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    // ---------------- GALLERY ----------------
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                val bitmap = ImageDecoder.decodeBitmap(source)
                capturedBitmap = bitmap
                TempProfileStorage.tempProfileBitmap = bitmap
            }
        }

    // ---------------- UI ----------------
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top= 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarWithSave(
                title = "Account",
                onBack = { navController.navigate("profile") },
                onSave = {
                    if (!isValid) return@TopBarWithSave
                    isSaving = true

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val healthInfo = HealthInformation(
                                bloodGroup = bloodGroup.value,
                                allergies = allergies.value,
                                medication = medications.value
                            )

                            withContext(Dispatchers.IO) {
                                FirestoreHelper.writeUser(user, capturedBitmap)
                                FirestoreHelper.writeHealthInformation(healthInfo)
                            }

                            // Clear fields
                            firstname.value = ""
                            lastname.value = ""
                            dateOfBirth.value = ""
                            homeAddress.value = ""
                            gender.value = Gender.OTHER
                            email.value = ""
                            phoneNumber.value = ""
                            city.value = ""
                            bloodGroup.value = ""
                            allergies.value = ""
                            medications.value = ""

                            capturedBitmap = null // Preview updates automatically due to state observation
                            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Save failed: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isSaving = false
                        }
                    }
                },
                enabled = isValid,
                isSaving = isSaving
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileImage(previewImage)

            PhotoOptionsMenu(
                showMenu = showMenu,
                onToggleMenu = { showMenu = !showMenu },
                onTakePhoto = {
                    showMenu = false
                    val permission = android.Manifest.permission.CAMERA
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(null)
                    } else {
                        permissionLauncher.launch(permission)
                    }
                },
                onUpload = {
                    showMenu = false
                    galleryLauncher.launch("image/*")
                },
                onDelete = {
                    showMenu = false

                    // FIX 2: Add visual feedback and robust error handling
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            // 1. Delete from server
                            withContext(Dispatchers.IO) {
                                FirestoreHelper.deleteUserProfileImage()
                            }

                            // 2. Update UI only if server delete succeeded
                            capturedBitmap = null
                            TempProfileStorage.tempProfileBitmap = null

                            Toast.makeText(context, "Photo removed", Toast.LENGTH_SHORT).show()
                            Log.d("DeletePhoto", "Profile image deleted successfully")

                        } catch (e: Exception) {
                            // 3. Tell the user WHY it failed
                            Log.e("DeletePhoto", "Failed to delete: ${e.message}")
                            Toast.makeText(context, "Could not delete: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }
    }
}
//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        AccountFormBottom(
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city
//        )
//
//        HealthInformationSection(
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications
//        )
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}


//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//    // User info state
//    val firstname = remember { mutableStateOf("") }
//    val lastname = remember { mutableStateOf("") }
//    val dateOfBirth = remember { mutableStateOf("") }
//    val homeAddress = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf(Gender.OTHER) }
//    val email = remember { mutableStateOf("") }
//    val phoneNumber = remember { mutableStateOf("") }
//    val city = remember { mutableStateOf("") }
//
//    // Health info state
//    val bloodGroup = remember { mutableStateOf("") }
//    val allergies = remember { mutableStateOf("") }
//    val medications = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // FIXED TOP SECTION (doesn't scroll)
//        AccountFormTop(
//            navController = navController,
//            profileBitmap = null,
//
//            firstname = firstname,
//            lastname = lastname,
//            dateOfBirth = dateOfBirth,
//            homeAddress = homeAddress,
//            gender = gender,
//            email = email,
//            phoneNumber = phoneNumber,
//            city = city,
//
//            bloodGroup = bloodGroup,
//            allergies = allergies,
//            medications = medications,
//        )
//
//        // SCROLLABLE BOTTOM SECTION
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(vertical = 8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            AccountFormBottom(
//                firstname = firstname,
//                lastname = lastname,
//                dateOfBirth = dateOfBirth,
//                homeAddress = homeAddress,
//                gender = gender,
//                email = email,
//                phoneNumber = phoneNumber,
//                city = city
//            )
//
//            HealthInformationSection(
//                bloodGroup = bloodGroup,
//                allergies = allergies,
//                medications = medications
//            )
//        }
//    }
//    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
//}



@Composable
fun AccountFormScreen(navController: NavHostController) {
    // User info state
    val firstname = remember { mutableStateOf("") }
    val lastname = remember { mutableStateOf("") }
    val dateOfBirth = remember { mutableStateOf("") }
    val homeAddress = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf(Gender.OTHER) }
    val email = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }

    // Health info state
    val bloodGroup = remember { mutableStateOf("") }
    val allergies = remember { mutableStateOf("") }
    val medications = remember { mutableStateOf("") }

    // Profile bitmap state
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Load profile image from Firestore on launch
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            try {
                val user = withContext(Dispatchers.IO) {
                    FirestoreHelper.getUser(uid)
                }

                // Check temp storage first
                val tempBitmap = TempProfileStorage.tempProfileBitmap
                if (tempBitmap != null) {
                    profileBitmap = tempBitmap
                } else {
                    // Load from Firestore URL if temp storage is empty
                    user?.profileImageUrl?.let { url ->
                        withContext(Dispatchers.IO) {
                            try {
                                val stream = URL(url).openStream()
                                profileBitmap = BitmapFactory.decodeStream(stream)
                            } catch (e: Exception) {
                                Log.e("AccountFormScreen", "Error loading image: ${e.message}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AccountFormScreen", "Error loading user data: ${e.message}")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // FIXED TOP SECTION (doesn't scroll)
        AccountFormTop(
            navController = navController,
            profileBitmap = profileBitmap,  // Pass the loaded bitmap

            firstname = firstname,
            lastname = lastname,
            dateOfBirth = dateOfBirth,
            homeAddress = homeAddress,
            gender = gender,
            email = email,
            phoneNumber = phoneNumber,
            city = city,

            bloodGroup = bloodGroup,
            allergies = allergies,
            medications = medications,
        )

        // SCROLLABLE BOTTOM SECTION
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountFormBottom(
                firstname = firstname,
                lastname = lastname,
                dateOfBirth = dateOfBirth,
                homeAddress = homeAddress,
                gender = gender,
                email = email,
                phoneNumber = phoneNumber,
                city = city
            )

            HealthInformationSection(
                bloodGroup = bloodGroup,
                allergies = allergies,
                medications = medications
            )
        }
    }
    Log.d("AccountFormScreen", "Recomposing AccountFormScreen")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountFormScreenPreview() {
    AccountFormScreen(navController = rememberNavController())
}