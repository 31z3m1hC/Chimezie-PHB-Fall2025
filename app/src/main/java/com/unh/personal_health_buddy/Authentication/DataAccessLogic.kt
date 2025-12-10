package com.unh.personal_health_buddy.Authentication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storageMetadata
import com.unh.personal_health_buddy.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID



object FirestoreHelper {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference





    // ==================== PRESCRIPTION OPERATIONS ====================

    suspend fun writePrescription(prescription: Prescription) {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val prescriptionRef = db.collection("users")
                    .document(userId)
                    .collection("prescriptions")
                    .document()

                val prescriptionWithId = prescription.copy(id = prescriptionRef.id)
                prescriptionRef.set(prescriptionWithId).await()

                Log.d("FirestoreHelper", "Prescription saved: ${prescriptionRef.id} for user: $userId")
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error saving prescription: ${e.message}", e)
                throw e
            }
        }
    }

    suspend fun readAllPrescriptions(): List<Prescription> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val snapshot = db.collection("users")
                    .document(userId)
                    .collection("prescriptions")
                    .get()
                    .await()

                val prescriptions = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Prescription::class.java)
                }

                Log.d("FirestoreHelper", "Found ${prescriptions.size} prescriptions for user: $userId")
                prescriptions
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error getting prescriptions: ${e.message}", e)
                emptyList()
            }
        }
    }

    suspend fun deletePrescription(prescriptionId: String) {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                db.collection("users")
                    .document(userId)
                    .collection("prescriptions")
                    .document(prescriptionId)
                    .delete()
                    .await()

                Log.d("FirestoreHelper", "Prescription deleted: $prescriptionId for user: $userId")
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error deleting prescription: ${e.message}", e)
                throw e
            }
        }
    }

    suspend fun deleteAllPrescriptions() {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val prescriptionsSnapshot = db.collection("users")
                    .document(userId)
                    .collection("prescriptions")
                    .get()
                    .await()

                prescriptionsSnapshot.documents.forEach { doc ->
                    doc.reference.delete().await()
                }

                Log.d("FirestoreHelper", "All prescriptions deleted for user: $userId")
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error deleting all prescriptions: ${e.message}", e)
                throw e
            }
        }
    }

    // ==================== NEW: BULK UPDATE FUNCTION ====================

    suspend fun updateUserData(
        userId: String,
        updatedUser: User,
        updatedContacts: List<EmergencyContact>,
        updatedHealth: HealthInformation?
    ) {
        return withContext(Dispatchers.IO) {
            try {
                // Verify the userId matches the authenticated user
                val currentUserId = getCurrentUserId()
                if (userId != currentUserId) {
                    throw Exception("User ID mismatch: cannot update data for another user")
                }

                // 1. Update/Write User Profile
                db.collection("users")
                    .document(userId)
                    .set(updatedUser)
                    .await()
                Log.d("FirestoreHelper", "User data updated successfully: $userId")


                // 2. Update/Write Emergency Contacts
                updatedContacts.forEach { contact ->
                    if (contact.contactId.isBlank()) {
                        writeEmergencyContact(contact, userId)
                    } else {
                        updateEmergencyContact(contact, userId)
                    }
                }
                Log.d("FirestoreHelper", "Emergency contacts processed: ${updatedContacts.size}")


                // 3. Update/Write Health Information
                if (updatedHealth != null) {
                    writeHealthInformation(updatedHealth, userId)
                    Log.d("FirestoreHelper", "Health data updated successfully.")
                } else {
                    db.collection("users")
                        .document(userId)
                        .collection("healthInformation")
                        .document("info")
                        .delete()
                        .await()
                    Log.d("FirestoreHelper", "Health data deleted/not present.")
                }

            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error performing bulk update: ${e.message}", e)
                throw e
            }
        }
    }

    fun getVerifiedUser(): Pair<String, String> {
        val user = FirebaseAuth.getInstance().currentUser
        requireNotNull(user?.uid) { "No authenticated user UID found." }
        requireNotNull(user.email) { "Authenticated user has no email." }
        return user.uid to user.email!!
    }

    // Get current user ID
    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw Exception("No authenticated user")
    }

    // ==================== EMERGENCY CONTACTS ====================

    suspend fun writeEmergencyContact(contact: EmergencyContact) {
        val userId = getCurrentUserId()
        return writeEmergencyContact(contact, userId)
    }

    private suspend fun writeEmergencyContact(contact: EmergencyContact, userId: String) {
        return withContext(Dispatchers.IO) {
            val contactRef = db.collection("users")
                .document(userId)
                .collection("emergencyContacts")
                .document()

            val contactWithId = contact.copy(contactId = contactRef.id)
            contactRef.set(contactWithId).await()

            Log.d("FirestoreHelper", "Emergency contact saved: ${contactRef.id} for user: $userId")
        }
    }

    suspend fun readAllEmergencyContacts(): List<EmergencyContact> {
        return withContext(Dispatchers.IO) {
            val userId = getCurrentUserId()
            val snapshot = db.collection("users")
                .document(userId)
                .collection("emergencyContacts")
                .get()
                .await()

            val contacts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(EmergencyContact::class.java)
            }

            Log.d("FirestoreHelper", "Found ${contacts.size} emergency contacts for user: $userId")
            contacts
        }
    }

    suspend fun deleteEmergencyContact(contactId: String) {
        return withContext(Dispatchers.IO) {
            val userId = getCurrentUserId()
            db.collection("users")
                .document(userId)
                .collection("emergencyContacts")
                .document(contactId)
                .delete()
                .await()

            Log.d("FirestoreHelper", "Emergency contact deleted: $contactId for user: $userId")
        }
    }

    suspend fun updateEmergencyContact(contact: EmergencyContact) {
        val userId = getCurrentUserId()
        return updateEmergencyContact(contact, userId)
    }

    private suspend fun updateEmergencyContact(contact: EmergencyContact, userId: String) {
        return withContext(Dispatchers.IO) {
            db.collection("users")
                .document(userId)
                .collection("emergencyContacts")
                .document(contact.contactId)
                .set(contact)
                .await()

            Log.d("FirestoreHelper", "Emergency contact updated: ${contact.contactId} for user: $userId")
        }
    }

    suspend fun getEmergencyContact(): EmergencyContact? {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val snapshot = db.collection("users")
                    .document(userId)
                    .collection("emergencyContacts")
                    .limit(1)
                    .get()
                    .await()
                snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error getting emergency contact: ${e.message}")
                null
            }
        }
    }

    // ==================== USER OPERATIONS ====================

    suspend fun getUser(userId: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = db.collection("users")
                    .document(userId)
                    .get()
                    .await()
                snapshot.toObject(User::class.java)
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error getting user: ${e.message}")
                null
            }
        }
    }

    suspend fun writeUser(user: User, bitmap: Bitmap?) {
        return withContext(Dispatchers.IO) {
            val userId = getCurrentUserId()

            // Get existing user to check for old profile image
            val existingUser = getUser(userId)
            val oldImageUrl = existingUser?.profileImageUrl

            // Upload new profile image if provided
            val profileImageUrl = if (bitmap != null) {
                val uploadedUrl = updateProfileImage(oldImageUrl, bitmap)
                // If upload fails, keep the old URL instead of setting null
                uploadedUrl ?: oldImageUrl
            } else {
                oldImageUrl
            }

            // Update user object with image URL
            val userWithImage = user.copy(profileImageUrl = profileImageUrl)

            // Save to Firestore
            db.collection("users")
                .document(userId)
                .set(userWithImage)
                .await()

            Log.d("FirestoreHelper", "User saved successfully with image URL: $profileImageUrl")
        }
    }

    // ==================== HEALTH INFORMATION ====================

    suspend fun getHealthInformation(): HealthInformation? {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val snapshot = db.collection("users")
                    .document(userId)
                    .collection("healthInformation")
                    .document("info")
                    .get()
                    .await()
                snapshot.toObject(HealthInformation::class.java)
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error getting health info: ${e.message}")
                null
            }
        }
    }

    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
        val userId = getCurrentUserId()
        return writeHealthInformation(healthInfo, userId)
    }

    private suspend fun writeHealthInformation(healthInfo: HealthInformation, userId: String) {
        return withContext(Dispatchers.IO) {
            db.collection("users")
                .document(userId)
                .collection("healthInformation")
                .document("info")
                .set(healthInfo)
                .await()

            Log.d("FirestoreHelper", "Health information saved for user: $userId")
        }
    }

    // ==================== FIREBASE STORAGE OPERATIONS ====================

    private suspend fun uploadProfileImage(bitmap: Bitmap): String? {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val fileName = "${System.currentTimeMillis()}.jpg"
                val imageRef = storageRef.child("profile_images/$userId/$fileName")

                // Compress bitmap to JPEG format
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos)
                val imageData = baos.toByteArray()

                // Upload with metadata
                val metadata = storageMetadata {
                    contentType = "image/jpeg"
                }

                // Upload the image with proper error handling
                val uploadTask = imageRef.putBytes(imageData, metadata)

                uploadTask.await()

                // Get the download URL
                val downloadUrl = imageRef.downloadUrl.await()

                Log.d("FirestoreHelper", "Profile image uploaded: $downloadUrl")
                downloadUrl.toString()

            } catch (e: StorageException) {
                Log.e("FirestoreHelper", "Storage error: ${e.errorCode} - ${e.message}", e)
                null
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error uploading profile image: ${e.message}", e)
                null
            }
        }
    }

    private suspend fun deleteProfileImage(imageUrl: String?): Boolean {
        if (imageUrl.isNullOrEmpty()) return false

        return withContext(Dispatchers.IO) {
            try {
                val imageRef = storage.getReferenceFromUrl(imageUrl)
                imageRef.delete().await()
                Log.d("FirestoreHelper", "Profile image deleted successfully")
                true
            } catch (e: StorageException) {
                // Ignore 404 errors - file might already be deleted
                if (e.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    Log.d("FirestoreHelper", "Image already deleted or doesn't exist")
                    true
                } else {
                    Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
                    false
                }
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
                false
            }
        }
    }

    private suspend fun updateProfileImage(oldImageUrl: String?, newBitmap: Bitmap): String? {
        // Delete old image if it exists
        if (!oldImageUrl.isNullOrEmpty()) {
            deleteProfileImage(oldImageUrl)
        }

        // Upload new image
        return uploadProfileImage(newBitmap)
    }

    suspend fun deleteUserProfileImage() {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                val user = getUser(userId)
                val imageUrl = user?.profileImageUrl

                if (imageUrl != null) {
                    // Delete from Storage
                    deleteProfileImage(imageUrl)

                    // Update Firestore to remove image URL
                    val updatedUser = user.copy(profileImageUrl = null)
                    db.collection("users")
                        .document(userId)
                        .set(updatedUser)
                        .await()

                    Log.d("FirestoreHelper", "User profile image deleted")
                }
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error deleting user profile image: ${e.message}", e)
                throw e
            }
        }
    }

    // ==================== DELETE ACCOUNT ====================

    suspend fun deleteAllUserData(userId: String) {
        return withContext(Dispatchers.IO) {
            try {
                val currentUserId = getCurrentUserId()
                if (userId != currentUserId) {
                    throw Exception("User ID mismatch: cannot delete data for another user")
                }

                // 1. Delete all emergency contacts
                try {
                    val emergencyContactsSnapshot = db.collection("users")
                        .document(userId)
                        .collection("emergencyContacts")
                        .get()
                        .await()

                    emergencyContactsSnapshot.documents.forEach { doc ->
                        doc.reference.delete().await()
                    }
                    Log.d("FirestoreHelper", "Deleted emergency contacts for user: $userId")
                } catch (e: Exception) {
                    Log.e("FirestoreHelper", "Error deleting contacts: ${e.message}")
                }

                // 2. Delete health information
                try {
                    db.collection("users")
                        .document(userId)
                        .collection("healthInformation")
                        .document("info")
                        .delete()
                        .await()
                    Log.d("FirestoreHelper", "Deleted health information for user: $userId")
                } catch (e: Exception) {
                    Log.e("FirestoreHelper", "Error deleting health info: ${e.message}")
                }

                // 3. Delete profile image from storage
                try {
                    val user = getUser(userId)
                    user?.profileImageUrl?.let { imageUrl ->
                        deleteProfileImage(imageUrl)
                    }
                    Log.d("FirestoreHelper", "Deleted profile image for user: $userId")
                } catch (e: Exception) {
                    Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}")
                }

                // 4. Delete all files in user's storage folder
                try {
                    val userStorageRef = storageRef.child("profile_images/$userId")
                    val listResult = userStorageRef.listAll().await()
                    listResult.items.forEach { item ->
                        try {
                            item.delete().await()
                        } catch (e: Exception) {
                            Log.e("FirestoreHelper", "Error deleting item: ${e.message}")
                        }
                    }
                    Log.d("FirestoreHelper", "Deleted all storage files for user: $userId")
                } catch (e: Exception) {
                    Log.e("FirestoreHelper", "Error deleting storage folder: ${e.message}")
                }

                // 5. Finally, delete the user document
                db.collection("users")
                    .document(userId)
                    .delete()
                    .await()
                Log.d("FirestoreHelper", "Deleted user document for user: $userId")

            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error deleting user data: ${e.message}", e)
                // Don't throw - continue with deletion even if some parts fail
            }
        }
    }

    suspend fun deleteUserAccountWithReauth(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val user = auth.currentUser ?: throw Exception("No authenticated user")

                // Re-authenticate
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential).await()
                Log.d("FirestoreHelper", "User re-authenticated successfully")

                // Delete account
                val userId = user.uid
                deleteAllUserData(userId)
                user.delete().await()

                Log.d("FirestoreHelper", "User account deleted successfully")
                true
            } catch (e: Exception) {
                Log.e("FirestoreHelper", "Error deleting account: ${e.message}", e)
                false
            }
        }
    }
}