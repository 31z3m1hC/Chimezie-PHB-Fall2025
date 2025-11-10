import android.R.attr.enabled
import android.R.attr.name
import android.R.attr.phoneNumber
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Face2
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.unh.personal_health_buddy.R
import java.io.File
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.Authentication.FirestoreHelper.writeEmergencyContact
import com.unh.personal_health_buddy.Authentication.FirestoreHelper.writeHealthInformation
import com.unh.personal_health_buddy.Authentication.FirestoreHelper.writeUser
import com.unh.personal_health_buddy.database.EmergencyContact
import com.unh.personal_health_buddy.database.Gender
import com.unh.personal_health_buddy.database.HealthInformation
import com.unh.personal_health_buddy.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import kotlinx.coroutines.withContext


fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "profile01.jpg")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}




fun validateUserInput(user: User, firebaseEmail: String): Boolean {
    val emailValid = user.email == firebaseEmail
    val phoneValid = user.phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$"))
    val nameValid = user.firstname.isNotBlank()

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
            .padding(vertical = 8.dp),
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
    }
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
}

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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.offset(x = (20).dp),
                text = "Photo Options",
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = onToggleMenu) {
                Icon(
                    modifier = Modifier.offset(10.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Show photo options"
                )
            }
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
}
//
//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    emergencyContact: EmergencyContact,
//    healthInfo: HealthInformation,
//    profileBitmap: Bitmap?,
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>
//)
// {
//    val context = LocalContext.current
//
//
//    // UI state
//    var capturedBitmap by remember { mutableStateOf<Bitmap?>(profileBitmap) }
//    var previewImage by remember { mutableStateOf<ImageBitmap?>(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//     var emergencyContact by remember { mutableStateOf(EmergencyContact()) }
//     var healthInfo by remember { mutableStateOf(HealthInformation()) }
//
//    // Construct user from current field values
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
//    // Reactive validation
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//            Log.d("Validation", "isValid = $isValid")
//        } catch (e: Exception) {
//            Log.e("Validation", "Error verifying user: ${e.message}")
//            isValid = false
//        }
//    }
//
//    // Launchers
//    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//        bitmap?.let {
//            capturedBitmap = it
//            previewImage = it.asImageBitmap()
//        }
//    }
//
//    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        uri?.let {
//            val source = ImageDecoder.createSource(context.contentResolver, it)
//            val bitmap = ImageDecoder.decodeBitmap(source)
//            capturedBitmap = bitmap
//            previewImage = bitmap.asImageBitmap()
//        }
//    }
//
//    // UI layout
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("account") },
//                onSave = {
//                    if (!isValid) {
//                        Log.e("SaveBlocked", "Attempted to save invalid data")
//                        return@TopBarWithSave
//                    }
//
//                    isSaving = true
//                    CoroutineScope(Dispatchers.Main).launch {
//                        try {
//                            //Update Emergency Contact from UI state
//                            emergencyContact = emergencyContact.copy(
//                                name = name.value,
//                                phone = emergencyPhone.value,
//                                relation = emergencyRelation.value
//                            )
//
//                            // Update Health Info from UI state
//                            healthInfo = healthInfo.copy(
//                                bloodType = bloodType.value,
//                                allergies = allergies.value,
//                                medications = medications.value,
//                                chronicConditions = chronicConditions.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeEmergencyContact(emergencyContact)
//                                FirestoreHelper.writeHealthInformation(healthInfo)
//                            }
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
//}




//@Composable
//fun AccountFormTop(
//    navController: NavHostController,
//    emergencyContact: EmergencyContact,
//    healthInfo: HealthInformation,
//    profileBitmap: Bitmap?,
//    firstname: MutableState<String>,
//    lastname: MutableState<String>,
//    dateOfBirth: MutableState<String>,
//    homeAddress: MutableState<String>,
//    gender: MutableState<Gender>,
//    email: MutableState<String>,
//    phoneNumber: MutableState<String>,
//    city: MutableState<String>,
//    emergencyName: MutableState<String>,
//    emergencyPhone: MutableState<String>,
//    emergencyRelation: MutableState<String>,
//    bloodGroup: MutableState<String>,
//    allergies: MutableState<String>,
//    medications: MutableState<String>
//) {
//
//    val context = LocalContext.current
//
//    // UI state
//    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
//    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
//    var showMenu by remember { mutableStateOf(false) }
//    var isValid by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    // store objects as mutable state (fixing your panic)
//    var emergencyContactState by remember { mutableStateOf(emergencyContact) }
//    var healthInfoState by remember { mutableStateOf(healthInfo) }
//
//    // Construct user from current field values
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
//    // Validation state
//    LaunchedEffect(user) {
//        try {
//            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
//            isValid = validateUserInput(user, firebaseEmail)
//        } catch (e: Exception) {
//            isValid = false
//        }
//    }
//
//    // Camera launcher
//    val cameraLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.TakePicturePreview()
//    ) { bitmap ->
//        bitmap?.let {
//            capturedBitmap = it
//            previewImage = it.asImageBitmap()
//        }
//    }
//
//    // Gallery launcher
//    val galleryLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri ->
//        uri?.let {
//            val source = ImageDecoder.createSource(context.contentResolver, it)
//            val bitmap = ImageDecoder.decodeBitmap(source)
//            capturedBitmap = bitmap
//            previewImage = bitmap.asImageBitmap()
//        }
//    }
//
//    // UI layout
//    Box(modifier = Modifier.fillMaxWidth()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            TopBarWithSave(
//                title = "Account",
//                onBack = { navController.navigate("account") },
//                onSave = {
//                    if (!isValid) return@TopBarWithSave
//
//                    isSaving = true
//                    CoroutineScope(Dispatchers.Main).launch {
//
//                        try {
//                            // Update emergency contact from UI state
//                            emergencyContactState = emergencyContactState.copy(
//                                name = emergencyName.value,
//                                phoneNumber = emergencyPhone.value,
//                                relation = emergencyRelation.value
//                            )
//
//                            // Update health info from UI state
//                            healthInfoState = healthInfoState.copy(
//                                bloodGroup = bloodGroup.value,
//                                allergies = allergies.value,
//                                medication = medications.value
//                            )
//
//                            withContext(Dispatchers.IO) {
//                                FirestoreHelper.writeUser(user, capturedBitmap)
//                                FirestoreHelper.writeEmergencyContact(emergencyContactState)
//                                FirestoreHelper.writeHealthInformation(healthInfoState)
//                            }
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
//}




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

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = firstname.value,
            onValueChange = { firstname.value = it },
            label = { Text("First Name") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastname.value,
            onValueChange = { lastname.value = it },
            label = { Text("Last Name") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dateOfBirth.value,
            onValueChange = { dateOfBirth.value = it },
            label = { Text("Date of Birth") },
            leadingIcon = {
                RoundedIcon(Icons.Default.DateRange, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = homeAddress.value,
            onValueChange = { homeAddress.value = it },
            label = { Text("Home Address") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Home, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city.value,
            onValueChange = { city.value = it },
            label = { Text("City") },
            leadingIcon = {
                RoundedIcon(Icons.Default.LocationCity, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

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
                modifier = Modifier.fillMaxWidth()
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
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Email, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Phone Number") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
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

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Health Information", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = medications.value,
            onValueChange = { medications.value = it },
            label = { Text("Medication") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Medication, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = allergies.value,
            onValueChange = { allergies.value = it },
            label = { Text("Allergies") },
            leadingIcon = {
                RoundedIcon(Icons.Default.MedicalInformation, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

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
                modifier = Modifier.fillMaxWidth()
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
}

//
//
//@Composable
//fun HealthInformationSection() {
//    var medication by remember { mutableStateOf("") }
//    var allergies by remember { mutableStateOf("") }
//    var bloodGroup by remember { mutableStateOf("") }
//    var bloodGroupExpanded by remember { mutableStateOf(false) }
//
//    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
//
//    Column(modifier = Modifier.padding( 16.dp)) {
//        Text("Health Information", style = MaterialTheme.typography.titleMedium)
//
//        OutlinedTextField(
//            value = medication,
//            onValueChange = { medication = it },
//            label = { Text("Medication") },
//            leadingIcon = {
//                RoundedIcon(
//                    imageVector = Icons.Default.Medication,
//                    backgroundColor = Color(0xFF87CEEB),
//                    iconTint = Color(0xFFEF6C00),
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = allergies,
//            onValueChange = { allergies = it },
//            label = { Text("Allergies") },
//            leadingIcon = {
//                RoundedIcon(
//                    imageVector = Icons.Default.MedicalInformation,
//                    backgroundColor = Color(0xFF87CEEB),
//                    iconTint = Color(0xFFEF6C00),
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Box(modifier = Modifier
//            .fillMaxWidth()
//            .zIndex(1f)
//        ) {
//            // Anchor for dropdown
//            OutlinedTextField(
//                value = bloodGroup,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Blood Group") },
//                leadingIcon = {
//                    RoundedIcon(
//                        imageVector = Icons.Default.Bloodtype,
//                        backgroundColor = Color(0xFF87CEEB),
//                        iconTint = Color(0xFFEF6C00),
//                    )
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Blood Group")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .pointerInput(Unit) {}
//            )
//
//            // Transparent clickable layer
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
//                            bloodGroup = group
//                            bloodGroupExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun EmergencyContactSection() {
//    var name by remember { mutableStateOf("") }
//    var phoneNumber by remember { mutableStateOf("") }
//    var relation by remember { mutableStateOf("") }
//    var relationExpanded by remember { mutableStateOf(false) }
//
//    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text(
//            text = "Emergency Contact",
//            style = MaterialTheme.typography.titleMedium
//        )
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            leadingIcon = {
//                RoundedIcon(
//                    imageVector = Icons.Default.Person,
//                    backgroundColor = Color(0xFF87CEEB),
//                    iconTint = Color(0xFFEF6C00),
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = phoneNumber,
//            onValueChange = { phoneNumber = it },
//            label = { Text("Phone Number") },
//            leadingIcon = {
//                RoundedIcon(
//                    imageVector = Icons.Default.Phone,
//                    backgroundColor = Color(0xFF87CEEB),
//                    iconTint = Color(0xFFEF6C00),
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .zIndex(1f)
//        ) {
//            OutlinedTextField(
//                value = relation,
//                onValueChange = {},
//                readOnly = true,
//                label = { Text("Relationship") },
//                leadingIcon = {
//                    RoundedIcon(
//                        imageVector = Icons.Default.Group,
//                        backgroundColor = Color(0xFF87CEEB),
//                        iconTint = Color(0xFFEF6C00),
//                    )
//                },
//                trailingIcon = {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Relation")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .pointerInput(Unit) {}
//            )
//
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clickable { relationExpanded = true }
//            )
//
//            DropdownMenu(
//                expanded = relationExpanded,
//                onDismissRequest = { relationExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                relationOptions.forEach { option ->
//                    DropdownMenuItem(
//                        text = { Text(option) },
//                        onClick = {
//                            relation = option
//                            relationExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//}



@Composable
fun EmergencyContactSection(
    emergencyName: MutableState<String>,
    emergencyPhone: MutableState<String>,
    emergencyRelation: MutableState<String>
) {
    var relationExpanded by remember { mutableStateOf(false) }
    val relationOptions = listOf("Sibling", "Parent", "Friend", "Relative", "Spouse")

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = emergencyName.value,
            onValueChange = { emergencyName.value = it },
            label = { Text("Name") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = emergencyPhone.value,
            onValueChange = { emergencyPhone.value = it },
            label = { Text("Phone Number") },
            leadingIcon = {
                RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxWidth().zIndex(1f)) {
            OutlinedTextField(
                value = emergencyRelation.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Relationship") },
                leadingIcon = {
                    RoundedIcon(Icons.Default.Group, Color(0xFF87CEEB), Color(0xFFEF6C00))
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Relation")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { relationExpanded = true }
            )

            DropdownMenu(
                expanded = relationExpanded,
                onDismissRequest = { relationExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                relationOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            emergencyRelation.value = option
                            relationExpanded = false
                        }
                    )
                }
            }
        }
    }
}





//
//@Composable
//fun AccountFormScreen(navController: NavHostController) {
//
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
//
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Spacer(modifier = Modifier.height(8.dp))
//        AccountFormTop(
//            navController = navController,            emergencyContact = EmergencyContact(),
//            healthInfo = HealthInformation(),
//            profileBitmap = null
//        )
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
//        HealthInformationSection()
//        EmergencyContactSection()
//    }
//}

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

    // Emergency contact
    emergencyName: MutableState<String>,
    emergencyPhone: MutableState<String>,
    emergencyRelation: MutableState<String>,

    // Health info
    bloodGroup: MutableState<String>,
    allergies: MutableState<String>,
    medications: MutableState<String>
) {
    val context = LocalContext.current

    var capturedBitmap by remember { mutableStateOf(profileBitmap) }
    var previewImage by remember { mutableStateOf(profileBitmap?.asImageBitmap()) }
    var showMenu by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

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

    LaunchedEffect(user) {
        try {
            val (_, firebaseEmail) = FirestoreHelper.getVerifiedUser()
            isValid = validateUserInput(user, firebaseEmail)
        } catch (e: Exception) {
            isValid = false
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            capturedBitmap = it
            previewImage = it.asImageBitmap()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            val bitmap = ImageDecoder.decodeBitmap(source)
            capturedBitmap = bitmap
            previewImage = bitmap.asImageBitmap()
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarWithSave(
                title = "Account",
                onBack = { navController.navigate("account") },
                onSave = {
                    if (!isValid) return@TopBarWithSave

                    isSaving = true

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val emergencyContact = EmergencyContact(
                                contactId = "",
                                name = emergencyName.value,
                                phoneNumber = emergencyPhone.value,
                                relation = emergencyRelation.value
                            )

                            val healthInfo = HealthInformation(
                                bloodGroup = bloodGroup.value,
                                allergies = allergies.value,
                                medication = medications.value
                            )

                            withContext(Dispatchers.IO) {
                                FirestoreHelper.writeUser(user, capturedBitmap)
                                FirestoreHelper.writeEmergencyContact(emergencyContact)
                                FirestoreHelper.writeHealthInformation(healthInfo)
                            }

                            Log.d("SaveAction", "Saved successfully")
                        } catch (e: Exception) {
                            Log.e("SaveAction", "Save failed: ${e.message}")
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
                    cameraLauncher.launch(null)
                },
                onUpload = {
                    showMenu = false
                    galleryLauncher.launch("image/*")
                },
                onDelete = {
                    showMenu = false
                    capturedBitmap = null
                    previewImage = null
                }
            )
        }
    }
}




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

    // Emergency contact state
    val emergencyName = remember { mutableStateOf("") }
    val emergencyPhone = remember { mutableStateOf("") }
    val emergencyRelation = remember { mutableStateOf("") }

    // Health info state
    val bloodGroup = remember { mutableStateOf("") }
    val allergies = remember { mutableStateOf("") }
    val medications = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        AccountFormTop(
            navController = navController,
            profileBitmap = null,

            firstname = firstname,
            lastname = lastname,
            dateOfBirth = dateOfBirth,
            homeAddress = homeAddress,
            gender = gender,
            email = email,
            phoneNumber = phoneNumber,
            city = city,

            emergencyName = emergencyName,
            emergencyPhone = emergencyPhone,
            emergencyRelation = emergencyRelation,

            bloodGroup = bloodGroup,
            allergies = allergies,
            medications = medications
        )

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

        EmergencyContactSection(
            emergencyName = emergencyName,
            emergencyPhone = emergencyPhone,
            emergencyRelation = emergencyRelation
        )
    }
}






@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountFormScreenPreview() {
    AccountFormScreen(navController = rememberNavController())
}


