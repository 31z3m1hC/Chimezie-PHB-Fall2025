package com.unh.personal_health_buddy.database

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.identity.util.UUID
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



@SuppressLint("DefaultLocale")
fun calculateBMI(height: Quantity, weight: Quantity): Quantity {
    val hValue = height.value.replace(" ft", "").toFloatOrNull()
    val wValue = weight.value.replace(" lb", "").toFloatOrNull()
    return if (hValue != null && wValue != null && hValue > 0f) {
        val heightInches = hValue * 12
        val bmiValue = (wValue / (heightInches * heightInches)) * 703
        Quantity(String.format("%.2f lb/ft²", bmiValue))
    } else Quantity("")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountForm(
    navController: NavHostController,
    onUserSaved: () -> Unit
) {
    val authUser = FirebaseAuth.getInstance().currentUser
    val verifiedUid = authUser?.uid
    val verifiedEmail = authUser?.email

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<Gender?>(null) }
    var avatar by remember { mutableStateOf("") }
    var height by remember { mutableStateOf(Quantity("")) }
    var weight by remember { mutableStateOf(Quantity("")) }
    var bmi by remember { mutableStateOf(Quantity("")) }

    val bloodTypes = listOf("A+", "A−", "B+", "B−", "AB+", "AB−", "O+", "O−")
    var bloodInfo by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val avatarFocus = remember { FocusRequester() }
    val heightFocus = remember { FocusRequester() }
    val weightFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Personal Information", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { avatarFocus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = avatar,
            onValueChange = { avatar = it },
            label = { Text("Avatar URL") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(avatarFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { heightFocus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Gender")
        Row(modifier = Modifier.fillMaxWidth()) {
            Gender.entries.forEach { g ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = gender == g,
                        onClick = { gender = g }
                    )
                    Text(g.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Health Information", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = height.value,
            onValueChange = {
                val clean = it.replace(" ft", "").trim()
                height = Quantity("$clean ft")
                bmi = calculateBMI(height, weight)
            },
            label = { Text("Height (ft)") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(heightFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { weightFocus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight.value,
            onValueChange = {
                val clean = it.replace(" lb", "").trim()
                weight = Quantity("$clean lb")
                bmi = calculateBMI(height, weight)
            },
            label = { Text("Weight (lb)") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(weightFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bmi.value,
            onValueChange = {},
            label = { Text("Calculated BMI lb/ft²") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = bloodInfo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Blood Group") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                bloodTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            bloodInfo = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || height.value.isBlank() || weight.value.isBlank()) {
                    Log.e("UserAccountForm", "Missing required fields")
                    return@Button
                }

                if (verifiedUid == null || verifiedEmail == null) {
                    Log.e("UserAccountForm", "No authenticated user")
                    return@Button
                }

                if (verifiedEmail.trim().lowercase() != email.trim().lowercase()) {
                    Log.e("UserAccountForm", "Email mismatch: entered email = $email, auth email = $verifiedEmail")
                    return@Button
                }

                isSaving = true
                scope.launch(Dispatchers.IO) {
                    try {
                        Log.d("UserAccountForm", "Starting Firestore write for UID: $verifiedUid")

                        val healthInfo = HealthInfo(
                            bmi = mapOf(
                                "height" to height,
                                "weight" to weight,
                                "calculatedBMI" to bmi
                            ),
                            bloodInfo = bloodInfo,
                            notifications = emptyList(),
                            reports = emptyList(),
                            appointments = emptyList()
                        )

                        val user = User(
                            id = verifiedUid,
                            name = name,
                            email = verifiedEmail,
                            gender = gender ?: Gender.MALE,
                            avatar = avatar
                        )

                        FirestoreHelper.writeUser(user)
                        FirestoreHelper.writeHealthInfo(healthInfo)

                        withContext(Dispatchers.Main) {
                            Log.d("UserAccountForm", "Firestore write complete")
                            isSaving = false
                            onUserSaved()

                            name = ""
                            email = ""
                            avatar = ""
                            gender = null
                            height = Quantity("")
                            weight = Quantity("")
                            bmi = Quantity("")
                            bloodInfo = ""
                        }
                    } catch (e: Exception) {
                        Log.e("UserAccountForm", "Failed to save user information", e)
                        withContext(Dispatchers.Main) { isSaving = false }
                    }
                }
            },
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text(if (isSaving) "Saving..." else "Save User Information")
        }
    }

    Log.d("UserAccountForm", "User account form displayed")
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserAccountFormPreview() {
    val navController = rememberNavController()
    UserAccountForm(
        navController = navController,
        onUserSaved = {}
    )
}

