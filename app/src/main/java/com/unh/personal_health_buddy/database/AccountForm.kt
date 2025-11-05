//package com.unh.personal_health_buddy.database
//
//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.google.firebase.auth.FirebaseAuth
//import com.unh.personal_health_buddy.Authentication.FirestoreHelper
//import com.unh.personal_health_buddy.R
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@SuppressLint("DefaultLocale")
//fun calculateBMI(height: Quantity, weight: Quantity): Quantity {
//    val hValue = height.value.replace(" in", "").toFloatOrNull()
//    val wValue = weight.value.replace(" lb", "").toFloatOrNull()
//    return if (hValue != null && wValue != null && hValue > 0f) {
//        val bmiValue = (wValue / (hValue * hValue)) * 703
//        Quantity(String.format("%.2f lb/in²", bmiValue))
//    } else Quantity("")
//}
//
//private fun sanitizeNumericInput(rawInput: String, unit: String): String {
//    val withoutUnit = rawInput.replace(Regex("(?i)\\s*$unit\\s*"), "")
//    val cleaned = withoutUnit.replace(Regex("[^0-9.]"), "")
//    return cleaned.trim()
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun UserAccountForm(
//    navController: NavHostController,
//    onUserSaved: () -> Unit
//) {
//    val authUser = FirebaseAuth.getInstance().currentUser
//    val verifiedUid = authUser?.uid
//    val verifiedEmail = authUser?.email
//
//    var name by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var dateOfBirth by remember { mutableStateOf("") }
//    var homeAddress by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf<Gender?>(null) }
//    var medication by remember { mutableStateOf("") }
//    var height by remember { mutableStateOf(Quantity("")) }
//    var weight by remember { mutableStateOf(Quantity("")) }
//    var bmi by remember { mutableStateOf(Quantity("")) }
//
//    val bloodTypes = listOf("A+", "A−", "B+", "B−", "AB+", "AB−", "O+", "O−")
//    var bloodInfo by remember { mutableStateOf("") }
//    var expanded by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//
//    val scope = rememberCoroutineScope()
//    val focusManager = LocalFocusManager.current
//
//    val nameFocus = remember { FocusRequester() }
//    val lastnameFocus = remember { FocusRequester() }
//    val dobFocus = remember { FocusRequester() }
//    val addressFocus = remember { FocusRequester() }
//    val emailFocus = remember { FocusRequester() }
//    val medicationFocus = remember { FocusRequester() }
//    val heightFocus = remember { FocusRequester() }
//    val weightFocus = remember { FocusRequester() }
//
//    val scrollState = rememberScrollState()
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//                .padding(16.dp)
//        ) {
//            IconButton(onClick = { navController.navigate("profile") }) {
//                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Text("Personal Information", style = MaterialTheme.typography.titleMedium)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // First Name
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("First Name") },
//                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "First Name") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(nameFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { lastnameFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Last Name
//            OutlinedTextField(
//                value = lastname,
//                onValueChange = { lastname = it },
//                label = { Text("Last Name") },
//                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Last Name") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(lastnameFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { dobFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Date of Birth
//            OutlinedTextField(
//                value = dateOfBirth,
//                onValueChange = { dateOfBirth = it },
//                label = { Text("Date of Birth (MM/DD/YYYY)") },
//                leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Date of Birth") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(dobFocus),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
//                keyboardActions = KeyboardActions(onNext = { addressFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Home Address
//            OutlinedTextField(
//                value = homeAddress,
//                onValueChange = { homeAddress = it },
//                label = { Text("Home Address") },
//                leadingIcon = { Icon(Icons.Filled.Home, contentDescription = "Home Address") },
//                singleLine = false,
//                modifier = Modifier.fillMaxWidth().focusRequester(addressFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Email
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(emailFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { medicationFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Medication
//            OutlinedTextField(
//                value = medication,
//                onValueChange = { medication = it },
//                label = { Text("Medication") },
//                leadingIcon = { Icon(Icons.Filled.MedicalServices, contentDescription = "Medication") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(medicationFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { heightFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Gender Dropdown
//            var genderExpanded by remember { mutableStateOf(false) }
//            val genderOptions = Gender.entries.map { it.name }
//            Text("Gender")
//            ExposedDropdownMenuBox(
//                expanded = genderExpanded,
//                onExpandedChange = { genderExpanded = !genderExpanded }
//            ) {
//                OutlinedTextField(
//                    value = gender?.name ?: "",
//                    onValueChange = {},
//                    readOnly = true,
//                    label = { Text("Select Gender") },
//                    leadingIcon = { Icon(Icons.Filled.Wc, contentDescription = "Gender") },
//                    modifier = Modifier.fillMaxWidth().menuAnchor(),
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
//                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
//                )
//                ExposedDropdownMenu(
//                    expanded = genderExpanded,
//                    onDismissRequest = { genderExpanded = false }
//                ) {
//                    genderOptions.forEach { option ->
//                        DropdownMenuItem(
//                            text = { Text(option) },
//                            onClick = {
//                                gender = Gender.valueOf(option)
//                                genderExpanded = false
//                            }
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Text("Health Information", style = MaterialTheme.typography.titleMedium)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Height
//            OutlinedTextField(
//                value = height.value,
//                onValueChange = { incoming ->
//                    val cleaned = sanitizeNumericInput(incoming, "in")
//                    height = if (cleaned.isBlank()) Quantity("") else Quantity("$cleaned in")
//                    bmi = calculateBMI(height, weight)
//                },
//                label = { Text("Height (in)") },
//                leadingIcon = { Icon(Icons.Filled.Height, contentDescription = "Height") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(heightFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(onNext = { weightFocus.requestFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Weight
//            OutlinedTextField(
//                value = weight.value,
//                onValueChange = { incoming ->
//                    val cleaned = sanitizeNumericInput(incoming, "lb")
//                    weight = if (cleaned.isBlank()) Quantity("") else Quantity("$cleaned lb")
//                    bmi = calculateBMI(height, weight)
//                },
//                label = { Text("Weight (lb)") },
//                leadingIcon = { Icon(Icons.Filled.FitnessCenter, contentDescription = "Weight") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().focusRequester(weightFocus),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Calculated BMI
//            OutlinedTextField(
//                value = bmi.value,
//                onValueChange = {},
//                label = { Text("Calculated BMI lb/in²") },
//                leadingIcon = { Icon(Icons.Filled.Scale, contentDescription = "BMI") },
//                readOnly = true,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//            // Blood Group Dropdown
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }
//            ) {
//                OutlinedTextField(
//                    value = bloodInfo,
//                    onValueChange = {},
//                    readOnly = true,
//                    label = { Text("Select Blood Group") },
//                    leadingIcon = { Icon(Icons.Filled.Bloodtype, contentDescription = "Blood Group") },
//                    modifier = Modifier.fillMaxWidth().menuAnchor(),
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
//                )
//
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    bloodTypes.forEach { type ->
//                        DropdownMenuItem(
//                            text = { Text(type) },
//                            onClick = {
//                                bloodInfo = type
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            // Save Button
//            Button(
//                onClick = {
//                    if (name.isBlank() || lastname.isBlank() || email.isBlank() ||
//                        height.value.isBlank() || weight.value.isBlank()
//                    ) {
//                        Log.e("UserAccountForm", "Missing required fields")
//                        return@Button
//                    }
//                    if (verifiedUid == null || verifiedEmail == null) {
//                        Log.e("UserAccountForm", "No authenticated user")
//                        return@Button
//                    }
//                    if (verifiedEmail.trim().lowercase() != email.trim().lowercase()) {
//                        Log.e("UserAccountForm", "Email mismatch")
//                        return@Button
//                    }
//
//                    isSaving = true
//                    scope.launch(Dispatchers.IO) {
//                        try {
//                            val healthInfo = HealthInfo(
//                                bodyMassIndexInformation = mapOf(
//                                    "height" to height,
//                                    "weight" to weight,
//                                    "calculatedBMI" to bmi
//                                ),
//                                bloodInfo = bloodInfo,
//                                notifications = emptyList(),
//                                reports = emptyList(),
//                                appointments = emptyList()
//                            )
//
//                            val user = User(
//                                id = verifiedUid,
//                                firstname = name,
//                                lastname = lastname,
//                                dateOfBirth = dateOfBirth,
//                                homeAddress = homeAddress,
//                                gender = gender ?: Gender.MALE,
//                                email = verifiedEmail,
//                                medication = medication
//                            )
//
//                            FirestoreHelper.writeUser(user)
//                            FirestoreHelper.writeHealthInfo(healthInfo)
//
//                            withContext(Dispatchers.Main) {
//                                isSaving = false
//                                onUserSaved()
//                                name = ""
//                                lastname = ""
//                                dateOfBirth = ""
//                                homeAddress = ""
//                                email = ""
//                                medication = ""
//                                gender = null
//                                height = Quantity("")
//                                weight = Quantity("")
//                                bmi = Quantity("")
//                                bloodInfo = ""
//                            }
//                        } catch (e: Exception) {
//                            Log.e("UserAccountForm", "Failed to save user information", e)
//                            withContext(Dispatchers.Main) { isSaving = false }
//                        }
//                    }
//                },
//                enabled = !isSaving,
//                modifier = Modifier.fillMaxWidth().height(50.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = colorResource(id = R.color.purple_500),
//                    contentColor = Color.White
//                )
//            ) {
//                Text(if (isSaving) "Saving..." else "Save User Information")
//            }
//            Spacer(modifier = Modifier.height(64.dp))
//        }
//    }
//
//    Log.d("UserAccountForm", "User account form displayed")
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun UserAccountFormPreview() {
//    val navController = rememberNavController()
//    UserAccountForm(
//        navController = navController,
//        onUserSaved = {}
//    )
//}
