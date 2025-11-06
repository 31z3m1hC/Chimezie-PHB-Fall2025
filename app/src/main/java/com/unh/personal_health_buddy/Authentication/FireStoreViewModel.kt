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
            val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return@launch

            val userDoc = db.collection("users").document(firebaseUser.uid).get().await()

            // Only create document if first time — do NOT overwrite filled data
            if (!userDoc.exists()) {
                val newUser = User(
                    uid = firebaseUser.uid,
                    firstname = "Default",
                    phoneNumber = "",
                    allergies = "",
                    lastname = "Name",
                    gender = Gender.MALE,
                    email = firebaseUser.email ?: "",
                    medication = "",
                    dateOfBirth = "",
                    homeAddress = "",
                    profileImageUrl = null // keep space for JPEG URL
                )

                try {
                    db.collection("users")
                        .document(firebaseUser.uid)
                        .set(newUser)
                        .await()

                    Log.d("AuthToFirestore", "User profile created in Firestore")
                } catch (e: Exception) {
                    Log.e("AuthToFirestore", "Error creating user", e)
                }
            } else {
                Log.d("AuthToFirestore", "User exists — not overriding")
            }
        }
    }

    fun saveEmergencyContact(contact: EmergencyContact) {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return@launch

            try {
                val userId = firebaseUser.uid
                val contactId = if (contact.contactId.isNotEmpty()) contact.contactId
                else db.collection("users").document(userId)
                    .collection("emergencyContacts").document().id  // generate unique id

                db.collection("users")
                    .document(userId)
                    .collection("emergencyContacts")
                    .document(contactId)
                    .set(contact.copy(contactId = contactId)) // save ID in model
                    .await()

                Log.d("AuthToFirestore", "Emergency contact saved")
            } catch (e: Exception) {
                Log.e("AuthToFirestore", "Error saving emergency contact", e)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthToFirestorePreview() {}
