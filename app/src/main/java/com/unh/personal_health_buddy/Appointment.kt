package com.unh.personal_health_buddy


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

data class Appointment(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: LocalDate,
    val time: LocalTime,
    val notes: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen() {
    var appointments by remember { mutableStateOf(listOf<Appointment>()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointments") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Appointment")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No appointments yet. Tap + to add one.")
                }
            } else {
                LazyColumn {
                    items(appointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            onDelete = {
                                appointments = appointments.filter { it.id != appointment.id }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddAppointmentDialog(
            onDismiss = { showDialog = false },
            onSave = { newAppointment ->
                appointments = appointments + newAppointment
                showDialog = false
            }
        )
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(appointment.title, fontWeight = FontWeight.Bold)
                Text(
                    "${appointment.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))} - " +
                            appointment.time.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    style = MaterialTheme.typography.bodySmall
                )
                if (appointment.notes.isNotBlank()) {
                    Text(appointment.notes, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun AddAppointmentDialog(onDismiss: () -> Unit, onSave: (Appointment) -> Unit) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.now()) }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add Appointment") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = { newDate ->
                        runCatching { date = LocalDate.parse(newDate) }
                    },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time.toString(),
                    onValueChange = { newTime ->
                        runCatching { time = LocalTime.parse(newTime) }
                    },
                    label = { Text("Time (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(Appointment(title = title, date = date, time = time, notes = notes))
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppointmentScreenPreview() {
    MaterialTheme {
        AppointmentScreen()
    }
}