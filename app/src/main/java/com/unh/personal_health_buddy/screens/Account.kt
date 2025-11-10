//package com.unh.personal_health_buddy.screens
//import kotlin.collections.firstOrNull
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Log
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.ArrowForwardIos
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.google.firebase.storage.FirebaseStorage
//import com.unh.personal_health_buddy.Authentication.FirestoreHelper
//import com.unh.personal_health_buddy.database.User
//import kotlinx.coroutines.tasks.await
//
//import androidx.compose.ui.graphics.asAndroidBitmap
//import androidx.compose.ui.graphics.Canvas
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.compose.rememberNavController
//import com.unh.personal_health_buddy.database.EmergencyContact
//import com.unh.personal_health_buddy.database.Gender
//import com.unh.personal_health_buddy.database.HealthInformation
//import kotlinx.coroutines.launch
//import kotlin.collections.forEachIndexed
//
//@Composable
//fun AccountForm(navController: NavHostController) {
//
//    val scope = rememberCoroutineScope()
//
//    var firstname by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var dob by remember { mutableStateOf("") }
//    var address by remember { mutableStateOf("") }
//    var city by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var phoneNumber by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf("") }
//
//    var medication by remember { mutableStateOf("") }
//    var allergies by remember { mutableStateOf("") }
//    var bloodGroup by remember { mutableStateOf("") }
//
//    var emergencyName by remember { mutableStateOf("") }
//    var emergencyPhone by remember { mutableStateOf("") }
//    var emergencyRelationship by remember { mutableStateOf("") }
//
//    var pickedImage by remember { mutableStateOf<ImageBitmap?>(null) }
//
//    val context = LocalContext.current
//
//    // Image Picker
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        uri?.let {
//            val stream = context.contentResolver.openInputStream(uri)
//            val bitmap = BitmapFactory.decodeStream(stream)
//            pickedImage = bitmap.asImageBitmap()
//        }
//    }
//
//    // Load data initially
//    LaunchedEffect(Unit) {
//        val user = FirestoreHelper.readUser()
//        val health = FirestoreHelper.readHealthInformation()
//        val ec = FirestoreHelper.readEmergencyContact()
//
//        user?.let {
//            firstname = it.firstname
//            lastname = it.lastname
//            dob = it.dateOfBirth
//            address = it.homeAddress
//            city = it.city
//            email = it.email
//            phoneNumber = it.phoneNumber
//            gender = it.gender.name
//        }
//
//        health?.let {
//            medication = it.medication
//            allergies = it.allergies
//            bloodGroup = it.bloodGroup
//        }
//
//        ec?.let { contact ->
//            emergencyName = contact.contactName
//            emergencyPhone = contact.phone.firstOrNull() ?: ""
//            emergencyRelationship = contact.relationship
//        }
//    }
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .padding(20.dp)
//    ) {
//
//        Text("Edit Account", style = MaterialTheme.typography.headlineSmall)
//
//        Spacer(Modifier.height(10.dp))
//
//        // 📸 Image Picker
//        Box(
//            modifier = Modifier
//                .size(120.dp)
//                .clip(CircleShape)
//                .background(Color.LightGray)
//                .clickable { launcher.launch("image/*") },
//            contentAlignment = Alignment.Center
//        ) {
//            if (pickedImage != null) {
//                Image(
//                    bitmap = pickedImage!!,
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp))
//            }
//        }
//
//        Spacer(Modifier.height(15.dp))
//
//        // ---------- Input fields (simple TextFields) ----------
//
//        SimpleField("First Name", firstname) { firstname = it }
//        SimpleField("Last Name", lastname) { lastname = it }
//        SimpleField("Date of Birth", dob) { dob = it }
//        SimpleField("Address", address) { address = it }
//        SimpleField("City", city) { city = it }
//        SimpleField("Email", email) { email = it }
//        SimpleField("Phone", phoneNumber) { phoneNumber = it }
//        SimpleField("Gender", gender) { gender = it }
//
//        Divider()
//        Text("Health Info", fontWeight = FontWeight.Bold)
//
//        SimpleField("Medication", medication) { medication = it }
//        SimpleField("Allergies", allergies) { allergies = it }
//        SimpleField("Blood Group", bloodGroup) { bloodGroup = it }
//
//        Divider()
//        Text("Emergency Contact", fontWeight = FontWeight.Bold)
//
//        SimpleField("Contact Name", emergencyName) { emergencyName = it }
//        SimpleField("Contact Phone", emergencyPhone) { emergencyPhone = it }
//        SimpleField("Relationship", emergencyRelationship) { emergencyRelationship = it }
//
//        Spacer(Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//                scope.launch {
//                    val user = User(
//                        firstname = firstname,
//                        lastname = lastname,
//                        dateOfBirth = dob,
//                        homeAddress = address,
//                        city = city,
//                        email = email,
//                        phoneNumber = phoneNumber,
//                        gender = Gender.valueOf(gender)
//                    )
//
//                    // Convert image
//                    val bitmap = pickedImage?.asAndroidBitmapFixed()
//
//                    // Save user + image
//                    FirestoreHelper.writeUser(user, bitmap)
//
//                    // Save health
//                    FirestoreHelper.writeHealthInformation(
//                        HealthInformation(medication, allergies, bloodGroup)
//                    )
//
//                    // Save emergency contact
//                    FirestoreHelper.deleteEmergencyContact(emergencyPhone) // delete old one if exists
//                    FirestoreHelper.writeEmergencyContact(
//                        EmergencyContact(
//                            contactName = emergencyName,
//                            phone = listOf(emergencyPhone),
//                            relationship = emergencyRelationship
//                        )
//                    )
//
//                    // Send picked image back
//                    navController.previousBackStackEntry
//                        ?.savedStateHandle
//                        ?.set("pickedImage", pickedImage)
//
//                    navController.popBackStack()
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Save")
//        }
//    }
//}
//
//// Simple text field builder
//@Composable
//fun SimpleField(label: String, value: String, onChange: (String) -> Unit) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onChange,
//        label = { Text(label) },
//        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
//    )
//}
