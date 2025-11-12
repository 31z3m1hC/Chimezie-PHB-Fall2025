//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.ChevronLeft
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.unh.personal_health_buddy.Authentication.FirestoreHelper
//import com.unh.personal_health_buddy.database.EmergencyContact
//import com.unh.personal_health_buddy.database.HealthInformation
//import com.unh.personal_health_buddy.database.User
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.net.URL
//
//
//@Composable
//fun AccountTopSection(navController: NavHostController) {
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var firstname by remember { mutableStateOf("") }
//
//    // Fetch user data from Firestore
//    LaunchedEffect(Unit) {
//        try {
//            val user = FirestoreHelper.readUser()
//            user?.let {
//                firstname = it.firstname
//                profileBitmap = it.profileImageUrl?.let { url ->
//                    val bitmap = withContext(Dispatchers.IO) {
//                        val stream = URL(url).openStream()
//                        BitmapFactory.decodeStream(stream)
//                    }
//                    bitmap
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("AccountTopSection", "Error fetching user: ${e.message}")
//        }
//    }
//
//    val imageBitmap = profileBitmap?.asImageBitmap()
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .offset(y = (-10).dp)
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        BackHeader(
//            title = "Profile",
//            onBack = { navController.navigate("profile") }
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        if (imageBitmap != null) {
//            Image(
//                bitmap = imageBitmap,
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(140.dp)
//                    .clip(CircleShape)
//                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
//            )
//        } else {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Default Profile",
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape),
//                tint = Color.Gray
//            )
//        }
//
//        Text(
//            text = firstname,
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.SemiBold
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Divider()
//    }
//    Log.d("AccountTopSection", "Profile picture: $profileBitmap")
//}
//
//
//
//
//
//@Composable
//fun AccountBottomSection() {
//    var user by remember { mutableStateOf<User?>(null) }
//    var emergencyContact by remember { mutableStateOf<EmergencyContact?>(null) }
//    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
//
//    LaunchedEffect(Unit) {
//        try {
//            user = FirestoreHelper.readUser()
//            emergencyContact = FirestoreHelper.readEmergencyContact()
//            healthInfo = FirestoreHelper.readHealthInformation()
//        } catch (e: Exception) {
//            Log.e("AccountBottomSection", "Error loading data: ${e.message}")
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        user?.let {
//            Text("Full Name: ${it.firstname} ${it.lastname}")
//            Text("Date of Birth: ${it.dateOfBirth}")
//            Text("Gender: ${it.gender}")
//            Text("Email: ${it.email}")
//            Text("Phone Number: ${it.phoneNumber}")
//            Text("Home Address: ${it.homeAddress}")
//            Text("City: ${it.city}")
//        }
//
//        Divider()
//
//        emergencyContact?.let {
//            Text("Emergency Contact Name: ${it.name}")
//            Text("Phone Number: ${it.phoneNumber}")
//            Text("Relation: ${it.relation}")
//        }
//
//        Divider()
//
//        healthInfo?.let {
//            Text("Blood Group: ${it.bloodGroup}")
//            Text("Allergies: ${it.allergies}")
//            Text("Medications: ${it.medication}")
//        }
//    }
//    Log.d("AccountBottomSection", "User: $user")
//    Log.d("AccountBottomSection", "Emergency Contact: $emergencyContact")
//}
//
//
//@Composable
//fun AccountScreen(navController: NavHostController) {
//    Column(modifier = Modifier.fillMaxSize())
//    {
//        Spacer(modifier = Modifier.height(30.dp))
//        AccountTopSection(navController = navController)
//        Spacer(modifier = Modifier.height(12.dp))
//
//        AccountBottomSection()
//    }
//}
//
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AccountScreenPreview() {
//    AccountScreen(
//        navController = rememberNavController())
//}
//


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.*

@Composable
fun AccountScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // <-- make scrollable
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color.White)
                )
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AccountTopSection(navController)
        Spacer(modifier = Modifier.height(24.dp))
        AccountBottomSection()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AccountTopSection(navController: NavHostController) {
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var firstname by remember { mutableStateOf("") }

    // Fetch user data from Firestore
    LaunchedEffect(Unit) {
        try {
            val user = FirestoreHelper.readUser()
            user?.let {
                firstname = it.firstname
                profileBitmap = it.profileImageUrl?.let { url ->
                    val bitmap = withContext(Dispatchers.IO) {
                        val stream = URL(url).openStream()
                        BitmapFactory.decodeStream(stream)
                    }
                    bitmap
                }
            }
        } catch (e: Exception) {
            Log.e("AccountTopSection", "Error fetching user: ${e.message}")
        }
    }

    val imageBitmap = profileBitmap?.asImageBitmap()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-10).dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHeader(
            title = "Profile",
            onBack = { navController.navigate("profile") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile photo unchanged
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default Profile",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                tint = Color.Gray
            )
        }

        Text(
            text = firstname,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}

@Composable
fun AccountBottomSection() {
    var user by remember { mutableStateOf<User?>(null) }
    var emergencyContact by remember { mutableStateOf<EmergencyContact?>(null) }
    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }

    LaunchedEffect(Unit) {
        try {
            user = FirestoreHelper.readUser()
            emergencyContact = FirestoreHelper.readEmergencyContact()
            healthInfo = FirestoreHelper.readHealthInformation()
        } catch (e: Exception) {
            Log.e("AccountBottomSection", "Error loading data: ${e.message}")
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        user?.let { userData ->
            InfoCard(title = "Personal Information") {
                InfoRow("Full Name", "${userData.firstname} ${userData.lastname}")
                InfoRow("Date of Birth", userData.dateOfBirth)
                InfoRow("Gender", userData.gender.toString())
                InfoRow("Email", userData.email)
                InfoRow("Phone Number", userData.phoneNumber)
                InfoRow("Home Address", userData.homeAddress)
                InfoRow("City", userData.city)
            }
        }

        emergencyContact?.let { contact ->
            InfoCard(title = "Emergency Contact") {
                InfoRow("Name", contact.name)
                InfoRow("Phone", contact.phoneNumber)
                InfoRow("Relation", contact.relation)
            }
        }

        healthInfo?.let { health ->
            InfoCard(title = "Health Information") {
                InfoRow("Blood Group", health.bloodGroup)
                InfoRow("Allergies", health.allergies)
                InfoRow("Medications", health.medication)
            }
        }
    }
}

@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { /* Optional: expand/edit */ },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0D47A1)
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium, color = Color(0xFF0D47A1))
        Text(value, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A237E))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = rememberNavController())
}
