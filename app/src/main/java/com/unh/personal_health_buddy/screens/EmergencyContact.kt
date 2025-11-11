import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // Back button (added here, top-left)
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

            Spacer(Modifier.height(60.dp))

            Text(
                text = "Emergency Contact",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Text("Name:", style = MaterialTheme.typography.labelMedium)
            Text(emergencyContact?.name ?: "Not set", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(12.dp))

            Text("Phone:", style = MaterialTheme.typography.labelMedium)
            Text(emergencyContact?.phoneNumber ?: "Not set", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(12.dp))

            Text("Relationship:", style = MaterialTheme.typography.labelMedium)
            Text(emergencyContact?.relation ?: "Not set", style = MaterialTheme.typography.bodyLarge)
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmergencyContactScreen() {
    EmergencyContactScreen(rememberNavController())
}
