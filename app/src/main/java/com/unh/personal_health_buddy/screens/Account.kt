import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.*
import java.net.URL


//@Composable
//fun AccountScreen(navController: NavHostController) {
//    val scrollState = rememberScrollState()
//    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//    var user by remember { mutableStateOf<User?>(null) }
//    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
//    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    LaunchedEffect(userId) {
//        isLoading = true
//        try {
//            withContext(Dispatchers.IO) {
//                user = FirestoreHelper.getUser(userId)
//                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
//                healthInfo = FirestoreHelper.getHealthInformation()
//            }
//        } catch (e: Exception) {
//            Log.e("AccountScreen", "Error fetching data: ${e.message}")
//        } finally {
//            isLoading = false
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState)
//            .padding(16.dp)
//    ) {
//        // Top section: profile pic and first name
//        AccountTopSection(navController = navController, user = user)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Bottom section: personal info, emergency contacts, health info
//        BottomSection(
//            user = user,
//            emergencyContacts = emergencyContacts,
//            healthInfo = healthInfo,
//            isLoading = isLoading
//        )
//    }
//}
//
//
//@Composable
//fun BottomSection(
//    user: User?,
//    emergencyContacts: List<EmergencyContact>,
//    healthInfo: HealthInformation?,
//    isLoading: Boolean
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        if (isLoading) {
//            Text(
//                "Loading...",
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                fontWeight = FontWeight.Medium
//            )
//        } else {
//            // Personal Information
//            user?.let { u ->
//                Text("Personal Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("Full Name: ${u.firstname} ${u.lastname}")
//                Text("Date of Birth: ${u.dateOfBirth}")
//                Text("Gender: ${u.gender}")
//                Text("Email: ${u.email}")
//                Text("Phone Number: ${u.phoneNumber}")
//                Text("Home Address: ${u.homeAddress}")
//                Text("City: ${u.city}")
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Emergency Contacts
//            if (emergencyContacts.isNotEmpty()) {
//                emergencyContacts.forEach { c ->
//                    Text("Emergency Contact", fontWeight = FontWeight.Bold)
//                    Divider()
//                    Text("First Name: ${c.firstname}")
//                    Text("Last Name: ${c.lastname}")
//                    Text("Phone: ${c.phoneNumber}")
//                    Text("Relation: ${c.relationship}")
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            } else {
//                Text("No emergency contacts added.", fontStyle = FontStyle.Italic)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Health Information
//            healthInfo?.let { h ->
//                Text("Health Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("Blood Group: ${h.bloodGroup}")
//                Text("Allergies: ${h.allergies}")
//                Text("Medications: ${h.medication}")
//                Spacer(modifier = Modifier.height(16.dp))
//            } ?: run {
//                Text("No health information added.", fontStyle = FontStyle.Italic)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}
//
//
//
//@Composable
//fun AccountTopSection(navController: NavHostController, user: User?) {
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val firstName = user?.firstname ?: "User"  // Only the first name
//
//    LaunchedEffect(user?.profileImageUrl) {
//        user?.profileImageUrl?.let { url ->
//            try {
//                withContext(Dispatchers.IO) {
//                    val stream = URL(url).openStream()
//                    profileBitmap = BitmapFactory.decodeStream(stream)
//                }
//            } catch (e: Exception) {
//                Log.e("AccountTopSection", "Error loading image: ${e.message}")
//            }
//        }
//    }
//
//    val imageBitmap = profileBitmap?.asImageBitmap()
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BackHeader(
//            title = "Profile",
//            onBack = { navController.navigate("profile") } // Navigate to profile screen
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (imageBitmap != null) {
//            Image(
//                bitmap = imageBitmap,
//                contentDescription = "Profile Picture",
//                modifier = Modifier.size(140.dp).clip(CircleShape)
//            )
//        } else {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Default Profile",
//                modifier = Modifier.size(120.dp),
//                tint = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(text = firstName, fontWeight = FontWeight.SemiBold)
//        Spacer(modifier = Modifier.height(8.dp))
//    }
//}



// ============================================
// GLOBAL STATE TO STORE TEMPORARY PROFILE IMAGE
// ============================================
//object TempProfileStorage {
//    var tempProfileBitmap: Bitmap? = null
//}
//
//@Composable
//fun AccountScreen(navController: NavHostController) {
//    val scrollState = rememberScrollState()
//    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//    var user by remember { mutableStateOf<User?>(null) }
//    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
//    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    LaunchedEffect(userId) {
//        isLoading = true
//        try {
//            withContext(Dispatchers.IO) {
//                user = FirestoreHelper.getUser(userId)
//                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
//                healthInfo = FirestoreHelper.getHealthInformation()
//            }
//        } catch (e: Exception) {
//            Log.e("AccountScreen", "Error fetching data: ${e.message}")
//        } finally {
//            isLoading = false
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState)
//            .padding(16.dp)
//    ) {
//        // Top section: profile pic and first name
//        AccountTopSection(navController = navController, user = user)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Bottom section: personal info, emergency contacts, health info
//        BottomSection(
//            user = user,
//            emergencyContacts = emergencyContacts,
//            healthInfo = healthInfo,
//            isLoading = isLoading
//        )
//    }
//}
//
//@Composable
//fun BottomSection(
//    user: User?,
//    emergencyContacts: List<EmergencyContact>,
//    healthInfo: HealthInformation?,
//    isLoading: Boolean
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        if (isLoading) {
//            Text(
//                "Loading...",
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                fontWeight = FontWeight.Medium
//            )
//        } else {
//            // Personal Information
//            user?.let { u ->
//                Text("Personal Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("Full Name: ${u.firstname} ${u.lastname}")
//                Text("Date of Birth: ${u.dateOfBirth}")
//                Text("Gender: ${u.gender}")
//                Text("Email: ${u.email}")
//                Text("Phone Number: ${u.phoneNumber}")
//                Text("Home Address: ${u.homeAddress}")
//                Text("City: ${u.city}")
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Emergency Contacts
//            if (emergencyContacts.isNotEmpty()) {
//                emergencyContacts.forEach { c ->
//                    Text("Emergency Contact", fontWeight = FontWeight.Bold)
//                    Divider()
//                    Text("First Name: ${c.firstname}")
//                    Text("Last Name: ${c.lastname}")
//                    Text("Phone: ${c.phoneNumber}")
//                    Text("Relation: ${c.relationship}")
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            } else {
//                Text("No emergency contacts added.", fontStyle = FontStyle.Italic)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Health Information
//            healthInfo?.let { h ->
//                Text("Health Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("Blood Group: ${h.bloodGroup}")
//                Text("Allergies: ${h.allergies}")
//                Text("Medications: ${h.medication}")
//                Spacer(modifier = Modifier.height(16.dp))
//            } ?: run {
//                Text("No health information added.", fontStyle = FontStyle.Italic)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun AccountTopSection(navController: NavHostController, user: User?) {
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val firstName = user?.firstname ?: "User"
//
//    // UPDATED: Check temp storage first, then Firebase
//    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
//        // Priority 1: Check temp storage first
//        val tempBitmap = TempProfileStorage.tempProfileBitmap
//        if (tempBitmap != null) {
//            profileBitmap = tempBitmap
//            Log.d("AccountTopSection", "Using temp storage image")
//        } else {
//            // Priority 2: Load from Firebase if no temp image
//            user?.profileImageUrl?.let { url ->
//                try {
//                    withContext(Dispatchers.IO) {
//                        val stream = URL(url).openStream()
//                        profileBitmap = BitmapFactory.decodeStream(stream)
//                        Log.d("AccountTopSection", "Loaded image from Firebase")
//                    }
//                } catch (e: Exception) {
//                    Log.e("AccountTopSection", "Error loading image: ${e.message}")
//                }
//            }
//        }
//    }
//
//    val imageBitmap = profileBitmap?.asImageBitmap()
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BackHeader(
//            title = "Profile",
//            onBack = { navController.navigate("profile") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (imageBitmap != null) {
//            Image(
//                bitmap = imageBitmap,
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(140.dp)
//                    .clip(CircleShape)
//                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
//                contentScale = ContentScale.Crop
//            )
//        } else {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Default Profile",
//                modifier = Modifier.size(120.dp),
//                tint = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(text = firstName, fontWeight = FontWeight.SemiBold)
//        Spacer(modifier = Modifier.height(8.dp))
//    }
//}


//
//@Composable
//fun AccountScreen(navController: NavHostController) {
//    val scrollState = rememberScrollState()
//    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//    var user by remember { mutableStateOf<User?>(null) }
//    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
//    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    LaunchedEffect(userId) {
//        isLoading = true
//        try {
//            withContext(Dispatchers.IO) {
//                user = FirestoreHelper.getUser(userId)
//                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
//                healthInfo = FirestoreHelper.getHealthInformation()
//            }
//        } catch (e: Exception) {
//            Log.e("AccountScreen", "Error fetching data: ${e.message}")
//        } finally {
//            isLoading = false
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState)
//            .padding(16.dp)
//    ) {
//        // Top section: profile pic and first name
//        AccountTopSection(navController = navController, user = user)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Bottom section: personal info, emergency contacts, health info
//        BottomSection(
//            user = user,
//            emergencyContacts = emergencyContacts,
//            healthInfo = healthInfo,
//            isLoading = isLoading
//        )
//    }
//}
//
//@Composable
//fun BottomSection(
//    user: User?,
//    emergencyContacts: List<EmergencyContact>,
//    healthInfo: HealthInformation?,
//    isLoading: Boolean
//) {
//    Column(modifier = Modifier.fillMaxWidth()) {
//        if (isLoading) {
//            Text(
//                "Loading...",
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                fontWeight = FontWeight.Medium
//            )
//        } else {
//            // Personal Information
//            user?.let { u ->
//                Text("Personal Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("Full Name: ${u.firstname} ${u.lastname}")
//                Text("Date of Birth: ${u.dateOfBirth}")
//                Text("Gender: ${u.gender}")
//                Text("Email: ${u.email}")
//                Text("Phone Number: ${u.phoneNumber}")
//                Text("Home Address: ${u.homeAddress}")
//                Text("City: ${u.city}")
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Emergency Contacts - FIXED: Header outside loop
//            if (emergencyContacts.isNotEmpty()) {
//                Text("Emergency Contacts", fontWeight = FontWeight.Bold)
//                Divider()
//                Spacer(modifier = Modifier.height(8.dp))
//
//                emergencyContacts.forEach { c ->
//                    Text("First Name: ${c.firstname}")
//                    Text("Last Name: ${c.lastname}")
//                    Text("Phone: ${c.phoneNumber}")
//                    Text("Relation: ${c.relationship}")
//                    Spacer(modifier = Modifier.height(12.dp))
//                }
//                Spacer(modifier = Modifier.height(4.dp))
//            } else {
//                Text("Emergency Contacts", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("No emergency contacts added.", fontStyle = FontStyle.Italic)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Health Information
//            healthInfo?.let { h ->
//                Text("Health Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("Blood Group: ${h.bloodGroup}")
//                Text("Allergies: ${h.allergies}")
//                Text("Medications: ${h.medication}")
//                Spacer(modifier = Modifier.height(16.dp))
//            } ?: run {
//                Text("Health Information", fontWeight = FontWeight.Bold)
//                Divider()
//                Text("No health information added.", fontStyle = FontStyle.Italic)
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun AccountTopSection(navController: NavHostController, user: User?) {
//    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val firstName = user?.firstname ?: "User"
//
//    // UPDATED: Check temp storage first, then Firebase
//    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
//        // Priority 1: Check temp storage first
//        val tempBitmap = TempProfileStorage.tempProfileBitmap
//        if (tempBitmap != null) {
//            profileBitmap = tempBitmap
//            Log.d("AccountTopSection", "Using temp storage image")
//        } else {
//            // Priority 2: Load from Firebase if no temp image
//            user?.profileImageUrl?.let { url ->
//                try {
//                    withContext(Dispatchers.IO) {
//                        val stream = URL(url).openStream()
//                        profileBitmap = BitmapFactory.decodeStream(stream)
//                        Log.d("AccountTopSection", "Loaded image from Firebase")
//                    }
//                } catch (e: Exception) {
//                    Log.e("AccountTopSection", "Error loading image: ${e.message}")
//                }
//            }
//        }
//    }
//
//    val imageBitmap = profileBitmap?.asImageBitmap()
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BackHeader(
//            title = "Profile",
//            onBack = { navController.navigate("profile") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (imageBitmap != null) {
//            Image(
//                bitmap = imageBitmap,
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(140.dp)
//                    .clip(CircleShape)
//                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
//                contentScale = ContentScale.Crop
//            )
//        } else {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Default Profile",
//                modifier = Modifier.size(120.dp),
//                tint = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(text = firstName, fontWeight = FontWeight.SemiBold)
//        Spacer(modifier = Modifier.height(8.dp))
//    }
//}




@Composable
fun AccountScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var user by remember { mutableStateOf<User?>(null) }
    var emergencyContacts by remember { mutableStateOf<List<EmergencyContact>>(emptyList()) }
    var healthInfo by remember { mutableStateOf<HealthInformation?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        isLoading = true
        try {
            withContext(Dispatchers.IO) {
                user = FirestoreHelper.getUser(userId)
                emergencyContacts = FirestoreHelper.readAllEmergencyContacts()
                healthInfo = FirestoreHelper.getHealthInformation()
            }
        } catch (e: Exception) {
            Log.e("AccountScreen", "Error fetching data: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top section: profile pic and first name
        AccountTopSection(navController = navController, user = user)
        Spacer(modifier = Modifier.height(16.dp))

        // Bottom section: personal info, emergency contacts, health info
        BottomSection(
            user = user,
            emergencyContacts = emergencyContacts,
            healthInfo = healthInfo,
            isLoading = isLoading
        )

        // Extra space at bottom for better scrolling experience
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun BottomSection(
    user: User?,
    emergencyContacts: List<EmergencyContact>,
    healthInfo: HealthInformation?,
    isLoading: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Personal Information Card
            user?.let { u ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Personal Information",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow("Full Name", "${u.firstname} ${u.lastname}")
                        InfoRow("Date of Birth", u.dateOfBirth)
                        InfoRow("Gender", u.gender.toString())
                        InfoRow("Email", u.email)
                        InfoRow("Phone Number", u.phoneNumber)
                        InfoRow("Home Address", u.homeAddress)
                        InfoRow("City", u.city)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Emergency Contacts Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Emergency Contacts",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    if (emergencyContacts.isNotEmpty()) {
                        emergencyContacts.forEachIndexed { index, c ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(thickness = 0.5.dp, color = Color.LightGray)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            InfoRow("First Name", c.firstname)
                            InfoRow("Last Name", c.lastname)
                            InfoRow("Phone", c.phoneNumber)
                            InfoRow("Relationship", c.relationship)
                        }
                    } else {
                        Text(
                            "No emergency contacts added.",
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Health Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Health Information",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    healthInfo?.let { h ->
                        InfoRow("Blood Group", h.bloodGroup)
                        InfoRow("Allergies", h.allergies)
                        InfoRow("Medications", h.medication)
                    } ?: run {
                        Text(
                            "No health information added.",
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                }
            }
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
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
fun AccountTopSection(navController: NavHostController, user: User?) {
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val firstName = user?.firstname ?: "User"

    // UPDATED: Check temp storage first, then Firebase
    LaunchedEffect(user?.profileImageUrl, TempProfileStorage.tempProfileBitmap) {
        // Priority 1: Check temp storage first
        val tempBitmap = TempProfileStorage.tempProfileBitmap
        if (tempBitmap != null) {
            profileBitmap = tempBitmap
            Log.d("AccountTopSection", "Using temp storage image")
        } else {
            // Priority 2: Load from Firebase if no temp image
            user?.profileImageUrl?.let { url ->
                try {
                    withContext(Dispatchers.IO) {
                        val stream = URL(url).openStream()
                        profileBitmap = BitmapFactory.decodeStream(stream)
                        Log.d("AccountTopSection", "Loaded image from Firebase")
                    }
                } catch (e: Exception) {
                    Log.e("AccountTopSection", "Error loading image: ${e.message}")
                }
            }
        }
    }

    val imageBitmap = profileBitmap?.asImageBitmap()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        BackHeader(
            title = "Profile",
            onBack = { navController.navigate("profile") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default Profile",
                modifier = Modifier.size(120.dp),
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = firstName, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = rememberNavController())
}