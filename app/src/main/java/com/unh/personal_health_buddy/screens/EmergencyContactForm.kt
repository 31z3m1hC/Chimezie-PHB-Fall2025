package com.unh.personal_health_buddy.database

import android.util.Log
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactForm(
    navController: NavHostController,
    onContactSaved: () -> Unit
) {
    val authUser = FirebaseAuth.getInstance().currentUser
    if (authUser?.uid == null || authUser.email == null) {
        Log.e("EmergencyContactForm", "No authenticated user")
        return
    }

    var contactId by remember { mutableStateOf("") }
    var contactName by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var phone1 by remember { mutableStateOf("") }
    var phone2 by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val contactNameFocus = remember { FocusRequester() }
    val relationshipFocus = remember { FocusRequester() }
    val phone1Focus = remember { FocusRequester() }
    val phone2Focus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            val contacts = FirestoreHelper.readEmergencyContact()
            val existingContact = contacts.firstOrNull()
            if (existingContact != null) {
                contactId = existingContact.contactId
                contactName = existingContact.name
                relationship = existingContact.relationship
                phone1 = existingContact.phone.getOrNull(0) ?: ""
                phone2 = existingContact.phone.getOrNull(1) ?: ""
            }
        } catch (e: Exception) {
            Log.e("EmergencyContactForm", "Error reading contact", e)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Start)
                .clickable {
                    if (!navController.popBackStack()) {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Emergency Contact Information", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text("Contact Name") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .focusRequester(contactNameFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { relationshipFocus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = relationship,
            onValueChange = { relationship = it },
            label = { Text("Relationship") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .focusRequester(relationshipFocus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { phone1Focus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone1,
            onValueChange = { phone1 = it },
            label = { Text("Primary Phone Number") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .focusRequester(phone1Focus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { phone2Focus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone2,
            onValueChange = { phone2 = it },
            label = { Text("Secondary Phone Number (Optional)") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .focusRequester(phone2Focus),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (contactName.isBlank() || relationship.isBlank() || phone1.isBlank()) {
                    Log.e("EmergencyContactForm", "Missing required fields")
                    return@Button
                }

                val contact = EmergencyContact(
                    contactId = if (contactId.isNotBlank()) contactId else authUser.uid,
                    name = contactName,
                    phone = listOfNotNull(
                        phone1.takeIf { it.isNotBlank() },
                        phone2.takeIf { it.isNotBlank() }
                    ),
                    relationship = relationship
                )

                isSaving = true
                scope.launch(Dispatchers.IO) {
                    try {
                        FirestoreHelper.writeEmergencyContact(contact)
                        withContext(Dispatchers.Main) {
                            isSaving = false
                            onContactSaved()
                        }
                    } catch (e: Exception) {
                        Log.e("EmergencyContactForm", "Failed to save contact", e)
                        withContext(Dispatchers.Main) { isSaving = false }
                    }
                }
            },
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White
            )
        ) {
            Text(if (isSaving) "Saving..." else "Save Emergency Contact")
        }
    }

    Log.d("EmergencyContactForm", "Emergency contact form displayed")
}


@Preview(showBackground = true)
@Composable
fun EmergencyContactFormPreview() {
    val navController = rememberNavController()
    EmergencyContactForm(navController = navController, onContactSaved = {})
}
