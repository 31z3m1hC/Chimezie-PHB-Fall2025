import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unh.personal_health_buddy.database.EmergencyContact
import com.unh.personal_health_buddy.database.Gender
import com.unh.personal_health_buddy.database.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthToFirestore : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun syncAuthUserToFirestore() {
        viewModelScope.launch {
            val auth = FirebaseAuth.getInstance()
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    firstname = "Default Name",
                    lastname = "Default Name",
                    gender = Gender.MALE,
                    email = firebaseUser.email ?: "",
                    medication = "",
                    dateOfBirth = "",
                    homeAddress = ""
                )
                try {
                    db.collection("users")
                        .document(user.uid)
                        .set(user)
                        .await()
                    Log.d("AuthToFirestore", "User synced successfully")
                } catch (e: Exception) {
                    Log.e("AuthToFirestore", "Error syncing user", e)
                }
            }
        }
    }

    fun saveEmergencyContact(contact: EmergencyContact) {
        viewModelScope.launch {
            val auth = FirebaseAuth.getInstance()
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                try {
                    val userId = firebaseUser.uid
                    val contactId = contact.contactId.ifEmpty { userId }

                    db.collection("users")
                        .document(userId)
                        .collection("emergencyContacts")
                        .document(contactId)
                        .set(contact)
                        .await()

                    Log.d("AuthToFirestore", "Emergency contact saved successfully.")
                } catch (e: Exception) {
                    Log.e("AuthToFirestore", "Error saving emergency contact", e)
                }
            } else {
                Log.e("AuthToFirestore", "No authenticated user found.")
            }
        }
    }
}
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable

fun AuthToFirestorePreview() {
    val viewModel = AuthToFirestore()
    viewModel.syncAuthUserToFirestore()
}