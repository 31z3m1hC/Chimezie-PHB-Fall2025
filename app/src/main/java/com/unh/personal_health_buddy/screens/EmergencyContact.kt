package com.unh.personal_health_buddy.screens

import RoundedIcon
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.R
import com.unh.personal_health_buddy.database.EmergencyContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EmergencyContactScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var contactToDelete by remember { mutableStateOf<EmergencyContact?>(null) }

    // Load contacts on launch
    LaunchedEffect(Unit) {
        val authUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (authUserId != null) {
            isLoading = true
            try {
                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
            } catch (e: Exception) {
                Log.e("EmergencyContactScreen", "Error loading contacts: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F7F7)) // light teal background like the mockup
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // HEADER LIKE THE MOCKUP (back arrow + title in a bordered rectangle)
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDFF8F5)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF00695C)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Emergency Contact",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C)
                    )
                    Spacer(modifier = Modifier.width(48.dp)) // to balance the back button space
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator()
                    }
                }

                emergencyContacts.isEmpty() -> {
                    // EMPTY STATE UI LIKE YOUR MOCKUP
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Card(
                            modifier = Modifier
                                .width(280.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF80CBC4)),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE0F2F1)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 24.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Circular icon area
                                Box(
                                    modifier = Modifier
                                        .size(96.dp)
                                        .background(
                                            color = Color(0xFFB2DFDB),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        tint = Color(0xFF004D40),
                                        modifier = Modifier.size(48.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No Contacts Added",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF00695C),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Tap the + button to add an emergency contact.",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = "Saved Contacts",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF004D40)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    emergencyContacts.forEach { contact ->
                        EmergencyContactCard(
                            contact = contact,
                            onDelete = { contactToDelete = contact }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }

        // Square-ish FAB like the mockup
        FloatingActionButton(
            onClick = { showDialog = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = Color(0xFF00A58A),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Emergency Contact",
                modifier = Modifier.size(28.dp)
            )
        }

        if (showDialog) {
            AddEmergencyContactDialog(
                onDismiss = { showDialog = false },
                onSave = { newContact ->
                    emergencyContacts = emergencyContacts + newContact
                    showDialog = false
                }
            )
        }

        // Delete Confirmation Dialog
        if (contactToDelete != null) {
            AlertDialog(
                onDismissRequest = { contactToDelete = null },
                title = { Text("Delete Contact") },
                text = {
                    Text(
                        "Are you sure you want to delete ${contactToDelete?.firstname} ${contactToDelete?.lastname}?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val contact = contactToDelete
                            if (contact != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        FirestoreHelper.deleteEmergencyContact(contact.contactId)
                                        withContext(Dispatchers.Main) {
                                            emergencyContacts =
                                                emergencyContacts.filter { it.contactId != contact.contactId }
                                            contactToDelete = null
                                        }
                                    } catch (e: Exception) {
                                        Log.e(
                                            "EmergencyContactScreen",
                                            "Error deleting contact: ${e.message}"
                                        )
                                        withContext(Dispatchers.Main) {
                                            contactToDelete = null
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { contactToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun EmergencyContactCard(
    contact: EmergencyContact,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFB2DFDB)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F2F1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${contact.firstname} ${contact.lastname}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF004D40)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Phone: ${contact.phoneNumber}", fontSize = 14.sp)
                Text(text = "Relationship: ${contact.relationship}", fontSize = 14.sp)
            }

            Box {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "More Options"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
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
                                Text("Delete", color = Color.Red)
                            }
                        },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmergencyContactDialog(
    onDismiss: () -> Unit,
    onSave: (EmergencyContact) -> Unit
) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Error states
    var firstnameError by remember { mutableStateOf(false) }
    var lastnameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    // Create FocusRequesters for each field
    val firstnameFocus = remember { FocusRequester() }
    val lastnameFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val relationshipFocus = remember { FocusRequester() }

    val relationships = listOf("Parent", "Sibling", "Friend", "Others")

    // Validation functions
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.all { it.isLetter() || it.isWhitespace() }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFF1FFFE))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Emergency Contact",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF00695C)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // FIRST NAME
                OutlinedTextField(
                    value = firstname,
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                            firstname = it
                            firstnameError = false
                        }
                    },
                    label = { Text("First Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { lastnameFocus.requestFocus() }
                    ),
                    singleLine = true,
                    isError = firstnameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(firstnameFocus)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                                lastnameFocus.requestFocus()
                                true
                            } else {
                                false
                            }
                        },
                    leadingIcon = {
                        RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
                    }
                )
                if (firstnameError) {
                    Text(
                        text = "First name must contain only letters",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // LAST NAME
                OutlinedTextField(
                    value = lastname,
                    onValueChange = {
                        if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                            lastname = it
                            lastnameError = false
                        }
                    },
                    label = { Text("Last Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { phoneFocus.requestFocus() }
                    ),
                    singleLine = true,
                    isError = lastnameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(lastnameFocus)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                                phoneFocus.requestFocus()
                                true
                            } else {
                                false
                            }
                        },
                    leadingIcon = {
                        RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
                    }
                )
                if (lastnameError) {
                    Text(
                        text = "Last name must contain only letters",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // PHONE NUMBER
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                            phoneNumber = it
                            phoneError = false
                        }
                    },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { relationshipFocus.requestFocus() }
                    ),
                    singleLine = true,
                    isError = phoneError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(phoneFocus)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                                relationshipFocus.requestFocus()
                                true
                            } else {
                                false
                            }
                        },
                    leadingIcon = {
                        RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
                    },
                    supportingText = {
                        Text(
                            text = "${phoneNumber.length}/10",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
                if (phoneError) {
                    Text(
                        text = "Phone number must be 10 digits",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // RELATIONSHIP DROPDOWN
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = it }
                ) {
                    OutlinedTextField(
                        value = relationship,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Relationship") },
                        leadingIcon = {
                            RoundedIcon(Icons.Default.People, Color(0xFF87CEEB), Color(0xFFEF6C00))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .focusRequester(relationshipFocus)
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                                    focusManager.clearFocus()
                                    true
                                } else if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                                    expandedDropdown = true
                                    true
                                } else {
                                    false
                                }
                            }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        relationships.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    relationship = option
                                    expandedDropdown = false
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // CANCEL BUTTON
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.purple_500),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }

                    // SAVE BUTTON
                    Button(
                        onClick = {
                            // Validate all fields
                            firstnameError = !isValidName(firstname)
                            lastnameError = !isValidName(lastname)
                            phoneError = phoneNumber.length != 10

                            if (!firstnameError && !lastnameError && !phoneError && relationship.isNotBlank()) {
                                isSaving = true
                                val newContact = EmergencyContact(
                                    contactId = "",
                                    firstname = firstname,
                                    lastname = lastname,
                                    phoneNumber = phoneNumber,
                                    relationship = relationship
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        FirestoreHelper.writeEmergencyContact(newContact)
                                        withContext(Dispatchers.Main) {
                                            onSave(newContact)
                                        }
                                    } catch (e: Exception) {
                                        Log.e("AddEmergencyContact", "Error saving: ${e.message}")
                                        withContext(Dispatchers.Main) {
                                            isSaving = false
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving &&
                                firstname.isNotBlank() &&
                                lastname.isNotBlank() &&
                                phoneNumber.length == 10 &&
                                relationship.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.purple_500),
                            contentColor = Color.White,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Request focus on first field when dialog opens
    LaunchedEffect(Unit) {
        firstnameFocus.requestFocus()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmergencyContactScreen() {
    EmergencyContactScreen(rememberNavController())
}