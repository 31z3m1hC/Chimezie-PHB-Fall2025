//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.unh.personal_health_buddy.database.EmergencyContact
//import com.unh.personal_health_buddy.database.Gender
//import com.unh.personal_health_buddy.database.User
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class AuthToFirestore : ViewModel() {
//    private val db = FirebaseFirestore.getInstance()
//
//    // ------------------ Sync Auth User ------------------
//    fun syncAuthUserToFirestore() {
//        viewModelScope.launch {
//            val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return@launch
//
//            try {
//                val userDoc = db.collection("users")
//                    .document(firebaseUser.uid)
//                    .get()
//                    .await()
//
//                // Only create document if first time — do NOT overwrite filled data
//                if (!userDoc.exists()) {
//                    // Only include main user fields in document
//                    val newUser = User(
//                        uid = firebaseUser.uid,
//                        firstname = "Default",
//                        lastname = "Name",
//                        phoneNumber = "",
//                        email = firebaseUser.email ?: "",
//                        dateOfBirth = "",
//                        homeAddress = "",
//                        city = "",
//                        gender = Gender.MALE,
//                        profileImageUrl = null
//                    )
//
//                    db.collection("users")
//                        .document(firebaseUser.uid)
//                        .set(newUser)
//                        .await()
//
//                    Log.d("AuthToFirestore", "User profile created in Firestore")
//
//                    // Initialize empty HealthInformation subcollection
//                    db.collection("users")
//                        .document(firebaseUser.uid)
//                        .collection("HealthInfo")
//                        .document("details")
//                        .set(hashMapOf(
//                            "medication" to "",
//                            "allergies" to "",
//                            "bloodGroup" to ""
//                        ))
//                        .await()
//
//                    // Initialize empty EmergencyContacts subcollection
//                    db.collection("users")
//                        .document(firebaseUser.uid)
//                        .collection("EmergencyContacts")
//                        .document()
//                        .set(hashMapOf(
//                            "contactId" to "",
//                            "Contactname" to "",
//                            "phone" to emptyList<String>(),
//                            "relationship" to ""
//                        ))
//                        .await()
//
//                } else {
//                    Log.d("AuthToFirestore", "User exists — not overriding")
//                }
//            } catch (e: Exception) {
//                Log.e("AuthToFirestore", "Error creating or checking user", e)
//            }
//        }
//    }
//
//    // ------------------ Save Emergency Contact ------------------
//    fun saveEmergencyContact(contact: EmergencyContact) {
//        viewModelScope.launch {
//            val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return@launch
//            try {
//                val userId = firebaseUser.uid
//                val contactId = if (contact.contactId.isNotEmpty()) contact.contactId
//                else db.collection("users")
//                    .document(userId)
//                    .collection("EmergencyContacts")
//                    .document()
//                    .id // auto-generated unique ID
//
//                val contactToSave = contact.copy(contactId = contactId)
//
//                db.collection("users")
//                    .document(userId)
//                    .collection("EmergencyContacts")
//                    .document(contactId)
//                    .set(contactToSave)
//                    .await()
//
//                Log.d("AuthToFirestore", "Emergency contact saved")
//            } catch (e: Exception) {
//                Log.e("AuthToFirestore", "Error saving emergency contact", e)
//            }
//        }
//    }
//}
//
//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AuthToFirestorePreview() {
//    // Preview stub
//}
