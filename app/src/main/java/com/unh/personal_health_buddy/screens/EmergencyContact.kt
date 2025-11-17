import android.util.Log
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.focusable
import com.unh.personal_health_buddy.R
import androidx.compose.ui.input.key.onPreviewKeyEvent
import com.unh.personal_health_buddy.screens.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.EmergencyContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@Composable
//fun EmergencyContactScreen(navController: NavHostController) {
//    val scrollState = rememberScrollState()
//    var showDialog by remember { mutableStateOf(false) }
//    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    // Load contacts on launch
//    LaunchedEffect(Unit) {
//        val authUserId = FirebaseAuth.getInstance().currentUser?.uid
//        if (authUserId != null) {
//            isLoading = true
//            try {
//                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
//            } catch (e: Exception) {
//                Log.e("EmergencyContactScreen", "Error loading contacts: ${e.message}")
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//                .padding(16.dp)
//        ) {
//            Spacer(modifier = Modifier.height(32.dp))
//
//            BackHeader(
//                title = "home",
//                onBack = { navController.navigate("home") }
//            )
//
//            Text(
//                text = "Emergency Contacts",
//                style = MaterialTheme.typography.titleLarge,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            } else if (emergencyContacts.isEmpty()) {
//                Text(
//                    text = "No emergency contacts added yet.",
//                    modifier = Modifier.padding(16.dp)
//                )
//            } else {
//                emergencyContacts.forEach { contact ->
//                    EmergencyContactCard(contact)
//                    Spacer(modifier = Modifier.height(12.dp))
//                }
//            }
//
//            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
//        }
//
//        FloatingActionButton(
//            onClick = { showDialog = true },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Add Emergency Contact")
//        }
//
//        if (showDialog) {
//            AddEmergencyContactDialog(
//                onDismiss = { showDialog = false },
//                onSave = { newContact ->
//                    emergencyContacts = emergencyContacts + newContact
//                    showDialog = false
//                }
//            )
//        }
//    }
//}
//
//
//
//
//@Composable
//fun EmergencyContactCard(contact: EmergencyContact) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "First Name: ${contact.firstname}")
//            Text(text = "Last Name: ${contact.lastname}")
//            Text(text = "Phone: ${contact.phoneNumber}")
//            Text(text = "Relationship: ${contact.relationship}")
//        }
//    }
//}
//
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddEmergencyContactDialog(
//    onDismiss: () -> Unit,
//    onSave: (EmergencyContact) -> Unit
//) {
//    var firstname by remember { mutableStateOf("") }
//    var lastname by remember { mutableStateOf("") }
//    var phoneNumber by remember { mutableStateOf("") }
//    var relationship by remember { mutableStateOf("") }
//    var expandedDropdown by remember { mutableStateOf(false) }
//    var isSaving by remember { mutableStateOf(false) }
//    val focusManager = LocalFocusManager.current
//
//    // Create FocusRequesters for each field
//    val firstnameFocus = remember { FocusRequester() }
//    val lastnameFocus = remember { FocusRequester() }
//    val phoneFocus = remember { FocusRequester() }
//    val relationshipFocus = remember { FocusRequester() }
//
//    val relationships = listOf("Parent", "Sibling", "Friend", "Others")
//
//    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                Text(
//                    text = "Emergency Contact",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // FIRST NAME
//                OutlinedTextField(
//                    value = firstname,
//                    onValueChange = { firstname = it },
//                    label = { Text("First Name") },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onNext = { lastnameFocus.requestFocus() }
//                    ),
//                    singleLine = true,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(firstnameFocus)
//                        .onKeyEvent { keyEvent ->
//                            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
//                                lastnameFocus.requestFocus()
//                                true
//                            } else {
//                                false
//                            }
//                        },
//                    leadingIcon = {
//                        RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // LAST NAME
//                OutlinedTextField(
//                    value = lastname,
//                    onValueChange = { lastname = it },
//                    label = { Text("Last Name") },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onNext = { phoneFocus.requestFocus() }
//                    ),
//                    singleLine = true,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(lastnameFocus)
//                        .onKeyEvent { keyEvent ->
//                            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
//                                phoneFocus.requestFocus()
//                                true
//                            } else {
//                                false
//                            }
//                        },
//                    leadingIcon = {
//                        RoundedIcon(Icons.Default.Person, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // PHONE NUMBER
//                OutlinedTextField(
//                    value = phoneNumber,
//                    onValueChange = { phoneNumber = it },
//                    label = { Text("Phone Number") },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Phone,
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onNext = { relationshipFocus.requestFocus() }
//                    ),
//                    singleLine = true,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(phoneFocus)
//                        .onKeyEvent { keyEvent ->
//                            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
//                                relationshipFocus.requestFocus()
//                                true
//                            } else {
//                                false
//                            }
//                        },
//                    leadingIcon = {
//                        RoundedIcon(Icons.Default.Phone, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // RELATIONSHIP DROPDOWN
//                ExposedDropdownMenuBox(
//                    expanded = expandedDropdown,
//                    onExpandedChange = { expandedDropdown = it }
//                ) {
//                    OutlinedTextField(
//                        value = relationship,
//                        onValueChange = {},
//                        readOnly = true,
//                        label = { Text("Relationship") },
//                        leadingIcon = {
//                            RoundedIcon(Icons.Default.People, Color(0xFF87CEEB), Color(0xFFEF6C00))
//                        },
//                        trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown)
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .menuAnchor()
//                            .focusRequester(relationshipFocus)
//                            .onKeyEvent { keyEvent ->
//                                if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
//                                    focusManager.clearFocus()
//                                    true
//                                } else if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
//                                    expandedDropdown = true
//                                    true
//                                } else {
//                                    false
//                                }
//                            }
//                    )
//
//                    ExposedDropdownMenu(
//                        expanded = expandedDropdown,
//                        onDismissRequest = { expandedDropdown = false }
//                    ) {
//                        relationships.forEach { option ->
//                            DropdownMenuItem(
//                                text = { Text(option) },
//                                onClick = {
//                                    relationship = option
//                                    expandedDropdown = false
//                                    focusManager.clearFocus()
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    // CANCEL BUTTON
//                    Button(
//                        onClick = onDismiss,
//                        modifier = Modifier.weight(1f),
//                        enabled = !isSaving,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = colorResource(id = R.color.purple_500),
//                            contentColor = Color.White
//                        )
//                    ) {
//                        Text("Cancel")
//                    }
//
//                    // SAVE BUTTON
//                    Button(
//                        onClick = {
//                            if (firstname.isNotBlank() && lastname.isNotBlank() &&
//                                phoneNumber.isNotBlank() && relationship.isNotBlank()) {
//
//                                isSaving = true
//                                val newContact = EmergencyContact(
//                                    contactId = "",
//                                    firstname = firstname,
//                                    lastname = lastname,
//                                    phoneNumber = phoneNumber,
//                                    relationship = relationship
//                                )
//
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    try {
//                                        FirestoreHelper.writeEmergencyContact(newContact)
//                                        withContext(Dispatchers.Main) {
//                                            onSave(newContact)
//                                        }
//                                    } catch (e: Exception) {
//                                        Log.e("AddEmergencyContact", "Error saving: ${e.message}")
//                                        withContext(Dispatchers.Main) {
//                                            isSaving = false
//                                        }
//                                    }
//                                }
//                            }
//                        },
//                        modifier = Modifier.weight(1f),
//                        enabled = !isSaving &&
//                                firstname.isNotBlank() &&
//                                lastname.isNotBlank() &&
//                                phoneNumber.isNotBlank() &&
//                                relationship.isNotBlank(),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = colorResource(id = R.color.purple_500),
//                            contentColor = Color.White,
//                            disabledContainerColor = Color.LightGray,
//                            disabledContentColor = Color.White
//                        )
//                    ) {
//                        if (isSaving) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(16.dp),
//                                color = Color.White
//                            )
//                        } else {
//                            Text("Save")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    // Request focus on first field when dialog opens
//    LaunchedEffect(Unit) {
//        firstnameFocus.requestFocus()
//    }
//}



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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            BackHeader(
                title = "home",
                onBack = { navController.navigate("home") }
            )

            Text(
                text = "Emergency Contacts",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (emergencyContacts.isEmpty()) {
                Text(
                    text = "No emergency contacts added yet.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                emergencyContacts.forEach { contact ->
                    EmergencyContactCard(
                        contact = contact,
                        onDelete = { contactToDelete = contact }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = colorResource(id = R.color.purple_500),
            contentColor = Color.White
        ) {
            Text(
                text = "Add",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
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
                text = { Text("Are you sure you want to delete ${contactToDelete?.firstname} ${contactToDelete?.lastname}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val contact = contactToDelete
                            if (contact != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        FirestoreHelper.deleteEmergencyContact(contact.contactId)
                                        withContext(Dispatchers.Main) {
                                            emergencyContacts = emergencyContacts.filter { it.contactId != contact.contactId }
                                            contactToDelete = null
                                        }
                                    } catch (e: Exception) {
                                        Log.e("EmergencyContactScreen", "Error deleting contact: ${e.message}")
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "First Name: ${contact.firstname}")
                Text(text = "Last Name: ${contact.lastname}")
                Text(text = "Phone: ${contact.phoneNumber}")
                Text(text = "Relationship: ${contact.relationship}")
            }

            Box {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.size(40.dp)
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

    // Create FocusRequesters for each field
    val firstnameFocus = remember { FocusRequester() }
    val lastnameFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val relationshipFocus = remember { FocusRequester() }

    val relationships = listOf("Parent", "Sibling", "Friend", "Others")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Emergency Contact",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // FIRST NAME
                OutlinedTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = { Text("First Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { lastnameFocus.requestFocus() }
                    ),
                    singleLine = true,
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

                Spacer(modifier = Modifier.height(12.dp))

                // LAST NAME
                OutlinedTextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    label = { Text("Last Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { phoneFocus.requestFocus() }
                    ),
                    singleLine = true,
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

                Spacer(modifier = Modifier.height(12.dp))

                // PHONE NUMBER
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { relationshipFocus.requestFocus() }
                    ),
                    singleLine = true,
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
                    }
                )

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
                            if (firstname.isNotBlank() && lastname.isNotBlank() &&
                                phoneNumber.isNotBlank() && relationship.isNotBlank()) {

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
                                phoneNumber.isNotBlank() &&
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
