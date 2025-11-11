import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.EmergencyContact
import com.unh.personal_health_buddy.database.HealthInformation
import com.unh.personal_health_buddy.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL


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
    Log.d("AccountTopSection", "Profile picture: $profileBitmap")
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        user?.let {
            Text("Full Name: ${it.firstname} ${it.lastname}")
            Text("Date of Birth: ${it.dateOfBirth}")
            Text("Gender: ${it.gender}")
            Text("Email: ${it.email}")
            Text("Phone Number: ${it.phoneNumber}")
            Text("Home Address: ${it.homeAddress}")
            Text("City: ${it.city}")
        }

        Divider()

        emergencyContact?.let {
            Text("Emergency Contact Name: ${it.name}")
            Text("Phone Number: ${it.phoneNumber}")
            Text("Relation: ${it.relation}")
        }

        Divider()

        healthInfo?.let {
            Text("Blood Group: ${it.bloodGroup}")
            Text("Allergies: ${it.allergies}")
            Text("Medications: ${it.medication}")
        }
    }
    Log.d("AccountBottomSection", "User: $user")
    Log.d("AccountBottomSection", "Emergency Contact: $emergencyContact")
}


@Composable
fun AccountScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize())
    {
        Spacer(modifier = Modifier.height(30.dp))
        AccountTopSection(navController = navController)
        Spacer(modifier = Modifier.height(12.dp))

        AccountBottomSection()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(
        navController = rememberNavController())
}

