import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.Authentication.FirestoreHelper
import com.unh.personal_health_buddy.database.EmergencyContact

@Composable
fun EmergencyContactScreen(navController: NavController) {
    var emergencyContact by remember { mutableStateOf<EmergencyContact?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            emergencyContact = FirestoreHelper.readEmergencyContact()
        } catch (e: Exception) {
            Log.e("EmergencyContactScreen", "Failed to load emergency contact: ${e.message}")
        } finally {
            loading = false
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF3F51B5))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                    )
                )
                .padding(20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            BackHeader(
                title = "Profile",
                onBack = {
                    navController.navigate("profile") {
                        launchSingleTop = true
                        popUpTo("profile") { inclusive = false }
                    }
                }
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = "Emergency Contact",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(0xFF0D47A1)
            )

            Spacer(Modifier.height(30.dp))

            EmergencyContactCard(label = "Name", value = emergencyContact?.name)
            EmergencyContactCard(label = "Phone", value = emergencyContact?.phoneNumber)
            EmergencyContactCard(label = "Relationship", value = emergencyContact?.relation)
        }
    }
}

@Composable
fun EmergencyContactCard(label: String, value: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFF0D47A1)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value ?: "Not set",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF1A237E)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmergencyContactScreen() {
    EmergencyContactScreen(rememberNavController())
}
