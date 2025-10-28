package com.unh.personal_health_buddy.database

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun HealthInfo(
    navController: NavHostController,
    onHealthInfoSaved: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""

    var height by remember { mutableStateOf(Quantity("")) }
    var weight by remember { mutableStateOf(Quantity("")) }
    var bmi by remember { mutableStateOf(Quantity("")) }

    var isSaving by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val heightFocus = remember { FocusRequester() }
    val weightFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text("Health Information", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Height input
        OutlinedTextField(
            value = height.value,
            onValueChange = {
                height = Quantity("$it ft")
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
                weight = Quantity("$it lb")
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
            label = { Text("Calculated BMI (lb/ft²)") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                if (height.value.isBlank() || weight.value.isBlank() || userId.isBlank()) {
                    Log.e("HealthInfo", "Missing required fields")
                    return@Button
                }

                val healthInfo = HealthInfo(
                    bmi = mapOf(
                        "height" to height,
                        "weight" to weight,
                        "calculatedBMI" to bmi
                    ),
                    bloodInfo = "",
                    notifications = emptyList(),
                    reports = emptyList(),
                    appointments = emptyList()
                )

                isSaving = true
                scope.launch(Dispatchers.IO) {
                    try {
                        FirestoreHelper.writeHealthInfo(healthInfo)
                        withContext(Dispatchers.Main) {
                            isSaving = false
                            onHealthInfoSaved()
                        }
                    } catch (e: Exception) {
                        Log.e("HealthInfo", "Failed to save health info", e)
                        withContext(Dispatchers.Main) { isSaving = false }
                    }
                }
            },
            enabled = !isSaving && userId.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(if (isSaving) "Saving..." else "Save Health Info")
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HealthInfoPreview() {
    val navController = rememberNavController()
    HealthInfo(
        navController = navController,
        onHealthInfoSaved = {}
    )
}
