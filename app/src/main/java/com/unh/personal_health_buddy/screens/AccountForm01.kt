//package com.unh.personal_health_buddy.database
//
//import android.R.attr.label
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.util.Log
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.storage.FirebaseStorage
//import com.unh.personal_health_buddy.Authentication.FirestoreHelper
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import kotlinx.coroutines.tasks.await
//import java.io.ByteArrayOutputStream
//
//// ------------------ Country Code ------------------
//data class CountryCode(val code: String, val flag: String, val country: String)
//
//@Composable
//fun PhoneNumberField(
//    selectedCountry: CountryCode,
//    onCountrySelected: (CountryCode) -> Unit,
//    phoneNumber: String,
//    onPhoneChange: (String) -> Unit,
//    isPhoneValid: Boolean,
//    countryBoxWidth: Dp = 120.dp
//) {
//    val countryList = listOf(
//        CountryCode("+1", "🇺🇸", "United States"),
//        CountryCode("+91", "🇮🇳", "India"),
//        CountryCode("+44", "🇬🇧", "United Kingdom"),
//        CountryCode("+81", "🇯🇵", "Japan"),
//        CountryCode("+977", "🇳🇵", "Nepal"),
//        CountryCode("+61", "🇦🇺", "Australia")
//    )
//
//    var expanded by remember { mutableStateOf(false) }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Box(
//            modifier = Modifier
//                .width(countryBoxWidth)
//                .clickable { expanded = true }
//                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
//                .padding(12.dp)
//        ) {
//            Text("${selectedCountry.flag} ${selectedCountry.code}")
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            countryList.forEach { country ->
//                DropdownMenuItem(
//                    text = { Text("${country.flag}  ${country.country}  (${country.code})") },
//                    onClick = {
//                        onCountrySelected(country)
//                        expanded = false
//                    }
//                )
//            }
//        }
//
//        Spacer(Modifier.width(8.dp))
//
//        OutlinedTextField(
//            value = phoneNumber,
//            onValueChange = onPhoneChange,
//            label = { Text("Phone number") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//            modifier = Modifier.weight(1f),
//            shape = RoundedCornerShape(12.dp),
//            isError = !isPhoneValid && phoneNumber.isNotBlank()
//        )
//    }
//}
//
//// ------------------ ImageBitmap Conversion ------------------
//fun ImageBitmap.asAndroidBitmap(): Bitmap {
//    return if (this is Bitmap) this else Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//}
//
//// ------------------ AccountForm ------------------
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AccountForm(
//    navController: NavController,
//    modifier: Modifier = Modifier,
//    imageBitmap: ImageBitmap? = null,
//    onCancel: () -> Unit = {},
//    onSave: () -> Unit = {},
//    onPickImage: () -> Unit = {},
//    onLaunchCamera: () -> Unit = {},
//    onDeletePhoto: () -> Unit = {}
//) {
//    val context = LocalContext.current
//    var internalImage by remember { mutableStateOf<ImageBitmap?>(null) }
//    val currentImage = imageBitmap ?: internalImage
//
//    // ------------------ Launchers ------------------
//    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            context.contentResolver.openInputStream(it)?.use { stream ->
//                internalImage = BitmapFactory.decodeStream(stream)?.asImageBitmap()
//            }
//        }
//    }
//
//    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp: Bitmap? ->
//        bmp?.let { internalImage = it.asImageBitmap() }
//    }
//
//    // ------------------ Pre-fill user data if navigated from UserAccount ------------------
//    val existingUser = navController.previousBackStackEntry?.savedStateHandle?.get<User>("userToEdit")
//
//    val firstName = remember { mutableStateOf(existingUser?.firstname ?: "") }
//    val lastName = remember { mutableStateOf(existingUser?.lastname ?: "") }
//    val dob = remember { mutableStateOf(existingUser?.dateOfBirth ?: "") }
//    val phone = remember { mutableStateOf(existingUser?.phoneNumber ?: "") }
//    val address = remember { mutableStateOf(existingUser?.homeAddress ?: "") }
//    val gender = remember { mutableStateOf(existingUser?.gender?.name ?: "") }
//    val email = remember { mutableStateOf(existingUser?.email ?: FirebaseAuth.getInstance().currentUser?.email ?: "") }
//    val medications = remember { mutableStateOf(existingUser?.medication ?: "") }
//    val allergies = remember { mutableStateOf(existingUser?.allergies ?: "") }
//    val city = remember { mutableStateOf(existingUser?.city ?: "") }
//
//
//    var genderExpanded by remember { mutableStateOf(false) }
//    var photoMenuExpanded by remember { mutableStateOf(false) }
//    var selectedCountry by remember { mutableStateOf(CountryCode("+1", "🇺🇸", "United States")) }
//
//    val scope = rememberCoroutineScope()
//    var isSaving by remember { mutableStateOf(false) }
//
//    // ------------------ Load profile image from Firebase ------------------
//    LaunchedEffect(existingUser) {
//        existingUser?.profileImageUrl?.let { imageUrl ->
//            try {
//                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
//                val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
//                internalImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
//            } catch (_: Exception) {}
//        }
//    }
//
//    // ------------------ Validation ------------------
//    val isFirstNameValid = firstName.value.all { it.isLetter() || it.isWhitespace() } && firstName.value.isNotBlank()
//    val isLastNameValid = lastName.value.all { it.isLetter() || it.isWhitespace() } && lastName.value.isNotBlank()
//    val isPhoneValid = phone.value.all { it.isDigit() } && phone.value.isNotBlank()
//
//    val allFieldsFilled = listOf(
//        isFirstNameValid,
//        isLastNameValid,
//        dob.value.isNotBlank(),
//        isPhoneValid,
//        address.value.isNotBlank(),
//        gender.value.isNotBlank(),
//        email.value.isNotBlank(),
//        medications.value.isNotBlank(),
//        allergies.value.isNotBlank(),
//        city.value.isNotBlank(),
//
//    ).all { it }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color(0xFFD6EFFF))
//            .verticalScroll(rememberScrollState())
//            .padding(bottom = 16.dp)
//    ) {
//        Spacer(Modifier.height(32.dp))
//
//        // ------------------ Top Bar ------------------
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Cancel",
//                modifier = Modifier
//                    .clickable { onCancel() }
//                    .weight(1f),
//                textAlign = TextAlign.Start
//            )
//
//            Text(
//                "Personal Information",
//                modifier = Modifier.weight(2f),
//                textAlign = TextAlign.Center
//            )
//
//            Button(
//                onClick = {
//                    if (!allFieldsFilled || isSaving) return@Button
//                    val authUser = FirebaseAuth.getInstance().currentUser ?: return@Button
//                    isSaving = true
//                    scope.launch(Dispatchers.IO) {
//                        try {
//                            val uid = authUser.uid
//                            val bitmap: Bitmap? = internalImage?.asAndroidBitmap()
//                            var downloadUrl: String? = null
//
//                            if (bitmap != null) {
//                                val baos = ByteArrayOutputStream()
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
//                                val imageBytes = baos.toByteArray()
//
//                                val storageRef = FirebaseStorage.getInstance()
//                                    .reference
//                                    .child("profileImages/$uid/profile.jpg")
//                                storageRef.putBytes(imageBytes).await()
//                                downloadUrl = storageRef.downloadUrl.await().toString()
//                            }
//
//                            val user = User(
//                                firstname = firstName.value,
//                                lastname = lastName.value,
//                                dateOfBirth = dob.value,
//                                homeAddress = address.value,
//                                gender = Gender.valueOf(gender.value.uppercase()),
//                                email = authUser.email!!,
//                                medication = medications.value,
//                                phoneNumber = phone.value,
//                                profileImageUrl = downloadUrl,
//                                allergies = allergies.value,
//                                city = city.value,
//                            )
//
//                            FirestoreHelper.writeUser(user)
//
//                            withContext(Dispatchers.Main) {
//                                isSaving = false
//                                onSave()
//                            }
//                        } catch (e: Exception) {
//                            Log.e("AccountForm", "Save failed", e)
//                            withContext(Dispatchers.Main) { isSaving = false }
//                        }
//                    }
//                },
//                enabled = allFieldsFilled && !isSaving,
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(if (isSaving) "Saving..." else "Save")
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // ------------------ Profile Image ------------------
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .clickable { photoMenuExpanded = true }
//            ) {
//                if (currentImage != null) {
//                    Image(
//                        bitmap = currentImage,
//                        contentDescription = "Profile",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                } else {
//                    Box(
//                        Modifier
//                            .fillMaxSize()
//                            .background(Color.Gray),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(48.dp))
//                    }
//                }
//
//                DropdownMenu(
//                    expanded = photoMenuExpanded,
//                    onDismissRequest = { photoMenuExpanded = false }
//                ) {
//                    DropdownMenuItem(
//                        text = { Text("Pick from Gallery") },
//                        onClick = {
//                            photoMenuExpanded = false
//                            pickImageLauncher.launch("image/*")
//                            onPickImage()
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text("Take Photo") },
//                        onClick = {
//                            photoMenuExpanded = false
//                            takePhotoLauncher.launch(null)
//                            onLaunchCamera()
//                        }
//                    )
//                }
//            }
//
//            Spacer(Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier.clickable {
//                    internalImage = null
//                    onDeletePhoto()
//                },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(Icons.Default.Delete, null, modifier = Modifier.size(20.dp))
//                Spacer(Modifier.width(4.dp))
//                Text("Delete Photo", color = Color.Red, fontSize = 14.sp)
//            }
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        // ------------------ Form Fields ------------------
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 10.dp),
//            color = MaterialTheme.colorScheme.surface,
//            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
//            shadowElevation = 6.dp
//        ) {
//            Column(Modifier.fillMaxWidth().padding(16.dp)) {
//                OutlinedTextField(
//                    value = firstName.value,
//                    onValueChange = { firstName.value = it },
//                    label = { Text("First Name") },
//                    leadingIcon = { Icon(Icons.Default.Person, null) },
//                    isError = !isFirstNameValid && firstName.value.isNotBlank(),
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = lastName.value,
//                    onValueChange = { lastName.value = it },
//                    label = { Text("Last Name") },
//                    leadingIcon = { Icon(Icons.Default.Person, null) },
//                    isError = !isLastNameValid && lastName.value.isNotBlank(),
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = dob.value,
//                    onValueChange = { dob.value = it },
//                    label = { Text("Date of Birth") },
//                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null) },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(Modifier.height(10.dp))
//
//                PhoneNumberField(
//                    selectedCountry = selectedCountry,
//                    onCountrySelected = { selectedCountry = it },
//                    phoneNumber = phone.value,
//                    onPhoneChange = { phone.value = it },
//                    isPhoneValid = isPhoneValid,
//                    countryBoxWidth = 70.dp
//                )
//                Spacer(Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = email.value,
//                    onValueChange = { email.value = it },
//                    label = { Text("Email Address") },
//                    leadingIcon = { Icon(Icons.Default.Email, null) },
//                    modifier = Modifier.fillMaxWidth(),
//                    readOnly = false
//                )
//
//                Spacer(Modifier.height(10.dp))
//
//                // Gender dropdown
//                ExposedDropdownMenuBox(
//                    expanded = genderExpanded,
//                    onExpandedChange = { genderExpanded = !genderExpanded }
//                ) {
//                    OutlinedTextField(
//                        value = gender.value,
//                        onValueChange = {},
//                        readOnly = true,
//                        label = { Text("Gender") },
//                        leadingIcon = { Icon(Icons.Default.Face, null) },
//                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
//                        modifier = Modifier.fillMaxWidth().menuAnchor()
//                    )
//                    ExposedDropdownMenu(
//                        expanded = genderExpanded,
//                        onDismissRequest = { genderExpanded = false }
//                    ) {
//                        listOf("Male", "Female", "Other").forEach {
//                            DropdownMenuItem(
//                                text = { Text(it) },
//                                onClick = {
//                                    gender.value = it
//                                    genderExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = address.value,
//                    onValueChange = { address.value = it },
//                    label = { Text("Home Address") },
//                    leadingIcon = { Icon(Icons.Default.Home, null) },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(Modifier.height(10.dp))
//
//
//                OutlinedTextField(
//                    value = city.value,
//                    onValueChange = { city.value = it },
//                    label = { Text("City") },
//                    leadingIcon = { Icon(Icons.Default.LocationCity, null) },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = medications.value,
//                    onValueChange = { medications.value = it },
//                    label = { Text("Medications") },
//                    leadingIcon = { Icon(Icons.Default.MedicalServices, null) },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(Modifier.height(10.dp))
//
//                OutlinedTextField(
//                    value = allergies.value,
//                    onValueChange = { allergies.value = it },
//                    label = { Text("Possible Allergies") },
//                    leadingIcon = { Icon(Icons.Default.MedicalInformation, null) },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(Modifier.height(16.dp))
//            }
//        }
//    }
//
//    Log.d("AccountForm", "Account form displayed")
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewForm() {
//    AccountForm(navController = rememberNavController())
//}


package com.unh.personal_health_buddy.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ------------------ Country Code ------------------
data class CountryCode(val code: String, val flag: String, val country: String)

@Composable
fun PhoneNumberField(
    selectedCountry: CountryCode,
    onCountrySelected: (CountryCode) -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    isPhoneValid: Boolean,
    countryBoxWidth: Dp = 120.dp
) {
    val countryList = listOf(
        CountryCode("+1", "🇺🇸", "United States"),
        CountryCode("+91", "🇮🇳", "India"),
        CountryCode("+44", "🇬🇧", "United Kingdom"),
        CountryCode("+81", "🇯🇵", "Japan"),
        CountryCode("+977", "🇳🇵", "Nepal"),
        CountryCode("+61", "🇦🇺", "Australia")
    )

    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .width(countryBoxWidth)
                .clickable { expanded = true }
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text("${selectedCountry.flag} ${selectedCountry.code}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            countryList.forEach { country ->
                DropdownMenuItem(
                    text = { Text("${country.flag}  ${country.country}  (${country.code})") },
                    onClick = {
                        onCountrySelected(country)
                        expanded = false
                    }
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneChange,
            label = { Text("Phone number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            isError = !isPhoneValid && phoneNumber.isNotBlank()
        )
    }
}

// ------------------ AccountForm ------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountForm(
    navController: NavController,
    modifier: Modifier = Modifier,
    imageBitmap: ImageBitmap? = null,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current
    var internalImage by remember { mutableStateOf<ImageBitmap?>(imageBitmap) }
    val currentImage = internalImage

    // ------------------ Launchers ------------------
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { stream ->
                internalImage = BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        }
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp: Bitmap? ->
        bmp?.let { internalImage = it.asImageBitmap() }
    }

    // ------------------ Pre-fill user data if editing ------------------
    val existingUser = navController.previousBackStackEntry?.savedStateHandle?.get<User>("userToEdit")
    val firstName = remember { mutableStateOf(existingUser?.firstname ?: "") }
    val lastName = remember { mutableStateOf(existingUser?.lastname ?: "") }
    val dob = remember { mutableStateOf(existingUser?.dateOfBirth ?: "") }
    val phone = remember { mutableStateOf(existingUser?.phoneNumber ?: "") }
    val address = remember { mutableStateOf(existingUser?.homeAddress ?: "") }
    val gender = remember { mutableStateOf(existingUser?.gender?.name ?: "") }
    val email = remember { mutableStateOf(existingUser?.email ?: FirebaseAuth.getInstance().currentUser?.email ?: "") }
    val medications = remember { mutableStateOf(existingUser?.medication ?: "") }
    val allergies = remember { mutableStateOf(existingUser?.allergies ?: "") }
    val city = remember { mutableStateOf(existingUser?.city ?: "") }

    var genderExpanded by remember { mutableStateOf(false) }
    var photoMenuExpanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(CountryCode("+1", "🇺🇸", "United States")) }

    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    // ------------------ Validation ------------------
    val isFirstNameValid = firstName.value.all { it.isLetter() || it.isWhitespace() } && firstName.value.isNotBlank()
    val isLastNameValid = lastName.value.all { it.isLetter() || it.isWhitespace() } && lastName.value.isNotBlank()
    val isPhoneValid = phone.value.all { it.isDigit() } && phone.value.isNotBlank()
    val allFieldsFilled = listOf(
        isFirstNameValid,
        isLastNameValid,
        dob.value.isNotBlank(),
        isPhoneValid,
        address.value.isNotBlank(),
        gender.value.isNotBlank(),
        email.value.isNotBlank(),
        medications.value.isNotBlank(),
        allergies.value.isNotBlank(),
        city.value.isNotBlank()
    ).all { it }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFD6EFFF))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        // ------------------ Top Bar ------------------
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cancel",
                modifier = Modifier.clickable { onCancel() }.weight(1f),
                textAlign = TextAlign.Start
            )

            Text(
                "Personal Information",
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    if (!allFieldsFilled || isSaving) return@Button
                    isSaving = true

                    val authUser = FirebaseAuth.getInstance().currentUser ?: return@Button

                    scope.launch {
                        try {
                            val userToSave = User(
                                firstname = firstName.value,
                                lastname = lastName.value,
                                dateOfBirth = dob.value,
                                homeAddress = address.value,
                                gender = Gender.valueOf(gender.value.uppercase()),
                                email = authUser.email!!,
                                medication = medications.value,
                                phoneNumber = phone.value,
                                profileImageUrl = null, // handled in helper
                                allergies = allergies.value,
                                city = city.value,
                            )

                            // Write user to Firestore, including profile image
                            FirestoreHelper.writeUser(userToSave, internalImage?.asAndroidBitmap())

                            withContext(Dispatchers.Main) {
                                isSaving = false
                                onSave()
                            }
                        } catch (e: Exception) {
                            Log.e("AccountForm", "Save failed", e)
                            withContext(Dispatchers.Main) { isSaving = false }
                        }
                    }
                },
                enabled = allFieldsFilled && !isSaving,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isSaving) "Saving..." else "Save")
            }
        }

        Spacer(Modifier.height(16.dp))

        // ------------------ Profile Image ------------------
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { photoMenuExpanded = true }
            ) {
                if (currentImage != null) {
                    Image(
                        bitmap = currentImage,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        Modifier.fillMaxSize().background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(48.dp))
                    }
                }

                DropdownMenu(expanded = photoMenuExpanded, onDismissRequest = { photoMenuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Pick from Gallery") },
                        onClick = {
                            photoMenuExpanded = false
                            pickImageLauncher.launch("image/*")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Take Photo") },
                        onClick = {
                            photoMenuExpanded = false
                            takePhotoLauncher.launch(null)
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.clickable { internalImage = null },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Delete, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text("Delete Photo", color = Color.Red, fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        // ------------------ Form Fields ------------------
        Surface(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            shadowElevation = 6.dp
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                OutlinedTextField(
                    value = firstName.value,
                    onValueChange = { firstName.value = it },
                    label = { Text("First Name") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    isError = !isFirstNameValid && firstName.value.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = lastName.value,
                    onValueChange = { lastName.value = it },
                    label = { Text("Last Name") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    isError = !isLastNameValid && lastName.value.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = dob.value,
                    onValueChange = { dob.value = it },
                    label = { Text("Date of Birth") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                PhoneNumberField(
                    selectedCountry = selectedCountry,
                    onCountrySelected = { selectedCountry = it },
                    phoneNumber = phone.value,
                    onPhoneChange = { phone.value = it },
                    isPhoneValid = isPhoneValid,
                    countryBoxWidth = 70.dp
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = false
                )

                Spacer(Modifier.height(10.dp))

                // Gender dropdown
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        value = gender.value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gender") },
                        leadingIcon = { Icon(Icons.Default.Face, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        listOf("Male", "Female", "Other").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    gender.value = it
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = address.value,
                    onValueChange = { address.value = it },
                    label = { Text("Home Address") },
                    leadingIcon = { Icon(Icons.Default.Home, null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = city.value,
                    onValueChange = { city.value = it },
                    label = { Text("City") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = medications.value,
                    onValueChange = { medications.value = it },
                    label = { Text("Medications") },
                    leadingIcon = { Icon(Icons.Default.MedicalServices, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = allergies.value,
                    onValueChange = { allergies.value = it },
                    label = { Text("Possible Allergies") },
                    leadingIcon = { Icon(Icons.Default.MedicalInformation, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    Log.d("AccountForm", "Account form displayed")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewForm() {
    AccountForm(navController = rememberNavController())
}
