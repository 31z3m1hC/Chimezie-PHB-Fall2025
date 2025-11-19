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

//
//object FirestoreHelper {
//    @SuppressLint("StaticFieldLeak")
//    private val db = FirebaseFirestore.getInstance()
//    private val storage = FirebaseStorage.getInstance()
//
//    fun getVerifiedUser(): Pair<String, String> {
//        val user = FirebaseAuth.getInstance().currentUser
//        requireNotNull(user?.uid) { "No authenticated user UID found." }
//        requireNotNull(user.email) { "Authenticated user has no email." }
//        return user.uid to user.email!!
//    }
//
//    // -------------------- Users --------------------
////    suspend fun writeUser(user: User, profileImage: Bitmap? = null) {
////        try {
////            val (uid, email) = getVerifiedUser()
////            require(user.email == email) { "Email mismatch: form email does not match authenticated email." }
////
////            val userMap = hashMapOf(
////                "firstname" to user.firstname,
////                "lastname" to user.lastname,
////                "dateOfBirth" to user.dateOfBirth,
////                "homeAddress" to user.homeAddress,
////                "gender" to user.gender.name,
////                "email" to user.email,
////                "medication" to user.medication,
////                "phoneNumber" to user.phoneNumber,
////                "allergies" to user.allergies,
////                "city" to user.city,
////                "profileImageUrl" to user.profileImageUrl
////            )
////
////            profileImage?.let { bitmap ->
////                try {
////                    val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance()
////                        .reference.child("profilePics/$uid.jpg")
////
////                    val baos = ByteArrayOutputStream()
////                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
////                    val imageData = baos.toByteArray()
////
////                    storageRef.putBytes(imageData).await()
////                    val downloadUrl = storageRef.downloadUrl.await().toString()
////
////                    userMap["profileImageUrl"] = downloadUrl
////                    Log.d("FirestoreHelper", "Profile image uploaded successfully")
////                } catch (e: Exception) {
////                    Log.e("FirestoreHelper", "Failed to upload profile image", e)
////                }
////            }
////
////            db.collection("users")
////                .document(uid)
////                .set(userMap)
////                .await()
////
////            Log.d("FirestoreHelper", "User written to Firestore with UID: $uid")
////        } catch (e: Exception) {
////            Log.e("FirestoreHelper", "Failed to write user to Firestore", e)
////        }
////    }
//
////    suspend fun writeUser(user: User, profileImage: Bitmap?) {
////        try {
////            val (uid, email) = getVerifiedUser()
////            require(user.email == email) { "Email mismatch" }
////
////            var imageUrl: String? = null
////
////            //Upload image first if exists
////            if (profileImage != null) {
////                try {
////                    val baos = ByteArrayOutputStream()
////                    profileImage.compress(Bitmap.CompressFormat.JPEG, 90, baos)
////                    val data = baos.toByteArray()
////
////                    val storageRef = FirebaseStorage.getInstance()
////                        .reference.child("profilePics/$uid.jpg")
////
////                    storageRef.putBytes(data).await()
////                    imageUrl = storageRef.downloadUrl.await().toString()
////
////                    Log.d("FirestoreHelper", "Image uploaded: $imageUrl")
////
////                } catch (e: Exception) {
////                    Log.e("FirestoreHelper", "Image upload failed", e)
////                }
////            }
////
////            // Now write Firestore with correct URL
////            val userMap = hashMapOf(
////                "firstname" to user.firstname,
////                "lastname" to user.lastname,
////                "dateOfBirth" to user.dateOfBirth,
////                "homeAddress" to user.homeAddress,
////                "gender" to user.gender.name,
////                "email" to user.email,
////                "medication" to user.medication,
////                "phoneNumber" to user.phoneNumber,
////                "allergies" to user.allergies,
////                "city" to user.city,
////                "profileImageUrl" to (imageUrl ?: user.profileImageUrl)
////            )
////
////            db.collection("users").document(uid).set(userMap).await()
////            Log.d("FirestoreHelper", "User saved to Firestore")
////
////        } catch (e: Exception) {
////            Log.e("FirestoreHelper", "Failed to save user", e)
////        }
////    }
////
////
////
////
////    suspend fun readUser(): User? {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users")
////            .document(uid)
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
////        return snapshot.toObject(User::class.java)
////    }
//
//
////
////    suspend fun writeUser(user: User, profileBitmap: Bitmap?) {
////        val uid = FirebaseAuth.getInstance().currentUser?.uid
////            ?: throw Exception("User not logged in")
////
////        var profileUrl: String? = null
////
////        profileBitmap?.let { bitmap ->
////            // Upload to Storage with correct extension
////            val imageRef = storage.reference.child("profileImages/$uid.jpg")
////            val baos = ByteArrayOutputStream()
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
////            val data = baos.toByteArray()
////
////            imageRef.putBytes(data).await()      // Wait for upload
////            profileUrl = imageRef.downloadUrl.await().toString()  // Get URL
////        }
////
////        // Save user in Firestore
////        val userWithUrl = user.copy(profileImageUrl = profileUrl)
////        db.collection("users")
////            .document(uid)
////            .set(userWithUrl)
////            .await()
////    }
////
////    // ------------------ Read user ------------------
////    suspend fun readUser(): User? {
////        val uid = FirebaseAuth.getInstance().currentUser?.uid
////            ?: return null
////
////        return try {
////            val snapshot = db.collection("users")
////                .document(uid)
////                .get()
////                .await()
////
////            Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
////            snapshot.toObject(User::class.java)
////        } catch (e: Exception) {
////            Log.e("FirestoreHelper", "Failed to read user", e)
////            null
////        }
////    }
////
////
////
////    suspend fun deleteUser() {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
////    }
////
////    // -------------------- Health Info --------------------
////    suspend fun deleteHealthInfo() {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("HealthInfo")
////            .document("details")
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "Health info deleted for UID: $uid")
////    }
////
////
////    suspend fun writeHealthInformation(health: HealthInformation) {
////        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
////
////        db.collection("users")
////            .document(uid)
////            .collection("HealthInfo")
////            .document("details")
////            .set(health)
////            .await()
////        Log.d("FirestoreHelper", "Health information written for UID: $uid")
////    }
////
////
////
////
////    // -------------------- Emergency Contact --------------------
//////    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//////        val (uid, _) = getVerifiedUser()
//////        db.collection("users")
//////            .document(uid)
//////            .collection("EmergencyContact")
//////            .document(contact.contactId)
//////            .set(contact)
//////            .await()
//////        Log.d("FirestoreHelper", "Emergency contact written for UID: $uid")
//////    }
////
////    suspend fun writeEmergencyContact(contact: EmergencyContact) {
////        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
////
////        val docRef = db.collection("users")
////            .document(uid)
////            .collection("EmergencyContacts")
////            .document() // auto ID
////
////        val contactWithId = contact.copy(contactId = docRef.id)
////
////        docRef.set(contactWithId).await()
////        Log.d("FirestoreHelper", "Emergency contact written for UID: $uid")
////    }
////
////    suspend fun readEmergencyContact(): List<EmergencyContact> {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users")
////            .document(uid)
////            .collection("EmergencyContact")
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "Emergency contacts read for UID: $uid")
////        return snapshot.toObjects(EmergencyContact::class.java)
////    }
////
////    suspend fun deleteEmergencyContact(contactId: String) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("EmergencyContact")
////            .document(contactId)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "🗑️ Emergency contact deleted for UID: $uid")
////    }
////
////    // -------------------- Chats --------------------
////    suspend fun writeChat(chat: Chats) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("Chats")
////            .document(chat.chatId)
////            .set(chat)
////            .await()
////        Log.d("FirestoreHelper", "Chat written for UID: $uid")
////    }
////
////    suspend fun readChats(): List<Chats> {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users")
////            .document(uid)
////            .collection("Chats")
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "Chats read for UID: $uid")
////        return snapshot.toObjects(Chats::class.java)
////    }
////
////    suspend fun deleteChat(chatId: String) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("Chats")
////            .document(chatId)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "Chat deleted for UID: $uid")
////    }
////
////    // -------------------- Articles --------------------
////    suspend fun writeArticle(article: Articles) {
////        db.collection("articles")
////            .add(article)
////            .await()
////        Log.d("FirestoreHelper", "Article written")
////    }
////
////    suspend fun readArticles(): List<Articles> {
////        val snapshot = db.collection("articles")
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "Articles read")
////        return snapshot.toObjects(Articles::class.java)
////    }
////
////    suspend fun deleteArticle(articleId: String) {
////        db.collection("articles")
////            .document(articleId)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "Article deleted")
////    }
////
////    // -------------------- FAQs --------------------
////    suspend fun writeFAQ(faq: FAQs) {
////        db.collection("faqs")
////            .add(faq)
////            .await()
////        Log.d("FirestoreHelper", "FAQ written")
////    }
////
////    suspend fun readFAQs(): List<FAQs> {
////        val snapshot = db.collection("faqs")
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "FAQs read")
////        return snapshot.toObjects(FAQs::class.java)
////    }
////
////    suspend fun deleteFAQ(faqId: String) {
////        db.collection("faqs")
////            .document(faqId)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "FAQ deleted")
////    }
////
////    // -------------------- Helpers --------------------
////    fun decodeBase64ToBitmap(base64Str: String): Bitmap {
////        val bytes = Base64.decode(base64Str, Base64.DEFAULT)
////        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
////    }
////}
//
//
////    fun decodeBase64ToBitmap(base64Str: String): Bitmap {
////        val bytes = Base64.decode(base64Str, Base64.DEFAULT)
////        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
////    }
////
////    // -------------------- User --------------------
////    suspend fun writeUser(user: User, profileBitmap: Bitmap?) {
////        val (uid, _) = getVerifiedUser()
////        var profileUrl: String? = null
////
////        profileBitmap?.let { bitmap ->
////            val imageRef = storage.reference.child("profileImages/$uid.jpg")
////            val baos = ByteArrayOutputStream()
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
////            val data = baos.toByteArray()
////            imageRef.putBytes(data).await()
////            profileUrl = imageRef.downloadUrl.await().toString()
////        }
////
////        val userWithUrl = user.copy(profileImageUrl = profileUrl)
////        db.collection("users").document(uid).set(userWithUrl).await()
////        Log.d("FirestoreHelper", "User written with UID: $uid")
////    }
////
////    suspend fun readUser(): User? {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users").document(uid).get().await()
////        Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
////        return snapshot.toObject(User::class.java)
////    }
////
////    suspend fun deleteUser() {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users").document(uid).delete().await()
////        Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
////    }
////
////    // -------------------- Health Info --------------------
////    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
////        val (uid, _) = getVerifiedUser() // get authenticated UID
////        db.collection("users")
////            .document(uid)
////            .collection("HealthInformation")
////            .document("primary")
////            .set(healthInfo)
////            .await()
////        Log.d("FirestoreHelper", "HealthInformation saved for UID: $uid")
////    }
////
////
////    suspend fun readHealthInformation(): HealthInformation? {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users")
////            .document(uid)
////            .collection("HealthInfo")
////            .document("details")
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "Health information read for UID: $uid")
////        return snapshot.toObject(HealthInformation::class.java)
////    }
////
////    suspend fun deleteHealthInfo() {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("HealthInfo")
////            .document("details")
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "Health info deleted for UID: $uid")
////    }
////
////    // -------------------- Emergency Contacts --------------------
////    suspend fun writeEmergencyContact(contact: EmergencyContact) {
////        val (uid, _) = getVerifiedUser()
////        val docRef = db.collection("users")
////            .document(uid)
////            .collection("EmergencyContacts")
////            .document()
////        val contactWithId = contact.copy(contactId = docRef.id)
////        docRef.set(contactWithId).await()
////        Log.d("FirestoreHelper", "Emergency contact written with ID: ${docRef.id}")
////    }
////
////    suspend fun readEmergencyContact(): List<EmergencyContact> {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users")
////            .document(uid)
////            .collection("EmergencyContacts")
////            .get()
////            .await()
////        Log.d("FirestoreHelper", "Emergency contacts read for UID: $uid")
////        return snapshot.toObjects(EmergencyContact::class.java)
////    }
////
////
////    suspend fun deleteEmergencyContact(contactId: String) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("EmergencyContacts")
////            .document(contactId)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "Emergency contact deleted with ID: $contactId")
////    }
////
////    // -------------------- Chats --------------------
////    suspend fun writeChat(chat: Chats) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("Chats")
////            .document(chat.chatId)
////            .set(chat)
////            .await()
////        Log.d("FirestoreHelper", "Chat written with ID: ${chat.chatId}")
////    }
////
////    suspend fun readChats(): List<Chats> {
////        val (uid, _) = getVerifiedUser()
////        val snapshot = db.collection("users")
////            .document(uid)
////            .collection("Chats")
////            .get()
////            .await()
////        return snapshot.toObjects(Chats::class.java)
////    }
////
////    suspend fun deleteChat(chatId: String) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("Chats")
////            .document(chatId)
////            .delete()
////            .await()
////        Log.d("FirestoreHelper", "Chat deleted with ID: $chatId")
////    }
////
////    // -------------------- Articles --------------------
////    suspend fun writeArticle(article: Articles) {
////        db.collection("articles").add(article).await()
////        Log.d("FirestoreHelper", "Article written")
////    }
////
////    suspend fun readArticles(): List<Articles> {
////        val snapshot = db.collection("articles").get().await()
////        return snapshot.toObjects(Articles::class.java)
////    }
////
////    suspend fun deleteArticle(articleId: String) {
////        db.collection("articles").document(articleId).delete().await()
////        Log.d("FirestoreHelper", "Article deleted with ID: $articleId")
////    }
////
////    // -------------------- FAQs --------------------
////    suspend fun writeFAQ(faq: FAQs) {
////        db.collection("faqs").add(faq).await()
////        Log.d("FirestoreHelper", "FAQ written")
////    }
////
////    suspend fun readFAQs(): List<FAQs> {
////        val snapshot = db.collection("faqs").get().await()
////        return snapshot.toObjects(FAQs::class.java)
////    }
////
////    suspend fun deleteFAQ(faqId: String) {
////        db.collection("faqs").document(faqId).delete().await()
////        Log.d("FirestoreHelper", "FAQ deleted with ID: $faqId")
////    }
////}
//
//
//
//
//        // -------------------- Utility --------------------
//        fun decodeBase64ToBitmap(base64Str: String): Bitmap {
//            val bytes = Base64.decode(base64Str, Base64.DEFAULT)
//            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        }
//
//
//
//        // -------------------- User --------------------
//        suspend fun writeUser(user: User, profileBitmap: Bitmap?) {
//            val (uid, _) = getVerifiedUser()
//            var profileUrl: String? = null
//
//            profileBitmap?.let { bitmap ->
//                val imageRef = storage.reference.child("profileImages/$uid.jpg")
//                val baos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
//                val data = baos.toByteArray()
//                imageRef.putBytes(data).await()
//                profileUrl = imageRef.downloadUrl.await().toString()
//            }
//
//            val userWithUrl = user.copy(profileImageUrl = profileUrl)
//            db.collection("users").document(uid).set(userWithUrl).await()
//            Log.d("FirestoreHelper", "User written with UID: $uid")
//        }
//
//        suspend fun readUser(): User? {
//            val (uid, _) = getVerifiedUser()
//            val snapshot = db.collection("users").document(uid).get().await()
//            Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
//            return snapshot.toObject(User::class.java)
//        }
//
//        suspend fun deleteUser() {
//            val (uid, _) = getVerifiedUser()
//            db.collection("users").document(uid).delete().await()
//            Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
//        }
//
//        // -------------------- Health Info --------------------
////        suspend fun writeHealthInformation(healthInfo: HealthInformation) {
////            val (uid, _) = getVerifiedUser()
////            db.collection("users")
////                .document(uid)
////                .collection("HealthInformation")
////                .document("primary")
////                .set(healthInfo)
////                .await()
////            Log.d("FirestoreHelper", "HealthInformation saved for UID: $uid")
////        }
//
//    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//        val (uid, _) = getVerifiedUser()
//
//        // Ensure this uses actual UI values
//        db.collection("users")
//            .document(uid)
//            .collection("HealthInformation")
//            .document("primary")
//            .set(healthInfo)
//            .await()
//        Log.d("FirestoreHelper", "HealthInformation saved for UID: $uid")
//    }
//
//
//    suspend fun readHealthInformation(): HealthInformation? {
//            val (uid, _) = getVerifiedUser()
//            val snapshot = db.collection("users")
//                .document(uid)
//                .collection("HealthInformation")
//                .document("primary")
//                .get()
//                .await()
//            Log.d("FirestoreHelper", "HealthInformation read for UID: $uid")
//            return snapshot.toObject(HealthInformation::class.java)
//        }
//
//        suspend fun deleteHealthInformation() {
//            val (uid, _) = getVerifiedUser()
//            db.collection("users")
//                .document(uid)
//                .collection("HealthInformation")
//                .document("primary")
//                .delete()
//                .await()
//            Log.d("FirestoreHelper", "HealthInformation deleted for UID: $uid")
//        }
//
//        // -------------------- Emergency Contacts --------------------
////        suspend fun writeEmergencyContact(contact: EmergencyContact) {
////            val (uid, _) = getVerifiedUser()
////            val docRef = db.collection("users")
////                .document(uid)
////                .collection("EmergencyContacts")
////                .document() // Firebase generates ID
////            val contactWithId = contact.copy(contactId = docRef.id)
////            docRef.set(contactWithId).await()
////            Log.d("FirestoreHelper", "EmergencyContact written with ID: ${docRef.id}")
////        }
//
//
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        val (uid, _) = getVerifiedUser()
//
//        // Use a fixed document ID if only 1 emergency contact per user
//        val docRef = db.collection("users")
//            .document(uid)
//            .collection("EmergencyContacts")
//            .document("primary")
//
//        val contactWithId = contact.copy(contactId = docRef.id)
//        docRef.set(contactWithId).await()
//        Log.d("FirestoreHelper", "EmergencyContact written with ID: ${docRef.id}")
//    }
//
//    suspend fun readEmergencyContact(): EmergencyContact? {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .collection("EmergencyContacts")
//            .limit(1)
//            .get()
//            .await()
//
//        return snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
//    }
//
//
//        suspend fun deleteEmergencyContact(contactId: String) {
//            val (uid, _) = getVerifiedUser()
//            db.collection("users")
//                .document(uid)
//                .collection("EmergencyContacts")
//                .document(contactId)
//                .delete()
//                .await()
//            Log.d("FirestoreHelper", "EmergencyContact deleted with ID: $contactId")
//        }
//
//        // -------------------- Chats --------------------
//        suspend fun writeChat(chat: Chats) {
//            val (uid, _) = getVerifiedUser()
//            db.collection("users")
//                .document(uid)
//                .collection("Chats")
//                .document(chat.chatId)
//                .set(chat)
//                .await()
//            Log.d("FirestoreHelper", "Chat written with ID: ${chat.chatId}")
//        }
//
//        suspend fun readChats(): List<Chats> {
//            val (uid, _) = getVerifiedUser()
//            val snapshot = db.collection("users")
//                .document(uid)
//                .collection("Chats")
//                .get()
//                .await()
//            return snapshot.toObjects(Chats::class.java)
//        }
//
//        suspend fun deleteChat(chatId: String) {
//            val (uid, _) = getVerifiedUser()
//            db.collection("users")
//                .document(uid)
//                .collection("Chats")
//                .document(chatId)
//                .delete()
//                .await()
//            Log.d("FirestoreHelper", "Chat deleted with ID: $chatId")
//        }
//
//        // -------------------- Articles --------------------
//        suspend fun writeArticle(article: Articles) {
//            db.collection("articles").add(article).await()
//            Log.d("FirestoreHelper", "Article written")
//        }
//
//        suspend fun readArticles(): List<Articles> {
//            val snapshot = db.collection("articles").get().await()
//            return snapshot.toObjects(Articles::class.java)
//        }
//
//        suspend fun deleteArticle(articleId: String) {
//            db.collection("articles").document(articleId).delete().await()
//            Log.d("FirestoreHelper", "Article deleted with ID: $articleId")
//        }
//
//        // -------------------- FAQs --------------------
//        suspend fun writeFAQ(faq: FAQs) {
//            db.collection("faqs").add(faq).await()
//            Log.d("FirestoreHelper", "FAQ written")
//        }
//
//        suspend fun readFAQs(): List<FAQs> {
//            val snapshot = db.collection("faqs").get().await()
//            return snapshot.toObjects(FAQs::class.java)
//        }
//
//        suspend fun deleteFAQ(faqId: String) {
//            db.collection("faqs").document(faqId).delete().await()
//            Log.d("FirestoreHelper", "FAQ deleted with ID: $faqId")
//        }
//    }


// Update your FirestoreHelper with these methods
//
//object FirestoreHelper {
//    @SuppressLint("StaticFieldLeak")
//    private val db = Firebase.firestore
//    private val auth = Firebase.auth
//
//    fun getVerifiedUser(): Pair<String, String> {
//        val user = FirebaseAuth.getInstance().currentUser
//        requireNotNull(user?.uid) { "No authenticated user UID found." }
//        requireNotNull(user.email) { "Authenticated user has no email." }
//        return user.uid to user.email!!
//    }
//
//    // Get current user ID
//    private fun getCurrentUserId(): String {
//        return auth.currentUser?.uid ?: throw Exception("No authenticated user")
//    }
//
//    // Write emergency contact as subcollection under current user
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val contactRef = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document() // Auto-generate ID
//
//            val contactWithId = contact.copy(contactId = contactRef.id)
//
//            contactRef.set(contactWithId).await()
//        }
//    }
//
//    // Read all emergency contacts for current user (no external userId)
//    suspend fun readAllEmergencyContacts(): List<EmergencyContact> {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val snapshot = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .get()
//                .await()
//
//            snapshot.documents.mapNotNull { doc ->
//                doc.toObject(EmergencyContact::class.java)
//            }
//        }
//    }
//
//    // Delete emergency contact
//    suspend fun deleteEmergencyContact(contactId: String) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contactId)
//                .delete()
//                .await()
//        }
//    }
//
//    // Update emergency contact
//    suspend fun updateEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contact.contactId)
//                .set(contact)
//                .await()
//        }
//    }
//
//    // Get any user by id (keep as-is)
//    suspend fun getUser(userId: String): User? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .get()
//                    .await()
//                snapshot.toObject(User::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting user: ${e.message}")
//                null
//            }
//        }
//    }
//
//    // Get a single emergency contact for current user (uses current user)
//    suspend fun getEmergencyContact(): EmergencyContact? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("emergencyContacts")
//                    .limit(1)
//                    .get()
//                    .await()
//                snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting emergency contact: ${e.message}")
//                null
//            }
//        }
//    }
//
//    // Get health information for current user (uses current user)
//    suspend fun getHealthInformation(): HealthInformation? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("healthInformation")
//                    .document("info")
//                    .get()
//                    .await()
//                snapshot.toObject(HealthInformation::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting health info: ${e.message}")
//                null
//            }
//        }
//    }
//
//    suspend fun writeUser(user: User, bitmap: Bitmap?) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//
//            // Upload profile image if provided
//            var profileImageUrl: String? = null
//            if (bitmap != null) {
//                // You'll need Firebase Storage setup for this
//                // For now, leaving it as null or implement storage upload
//                profileImageUrl = uploadProfileImage(userId, bitmap)
//            }
//
//            val userWithImage = user.copy(profileImageUrl = profileImageUrl)
//
//            db.collection("users")
//                .document(userId)
//                .set(userWithImage)
//                .await()
//        }
//    }
//
//    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("healthInformation")
//                .document("info")
//                .set(healthInfo)
//                .await()
//        }
//    }
//
//    // Helper function for image upload (you'll need Firebase Storage)
////    private suspend fun uploadProfileImage(userId: String, bitmap: Bitmap): String {
////        // TODO: Implement Firebase Storage upload
////        // For now, return empty string
////        return ""
////    }
//
//
//
//    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
//    private val storageRef: StorageReference = storage.reference
//
//    /**
//     * Uploads a profile image to Firebase Storage
//     * @param userId The user's unique ID
//     * @param bitmap The profile image bitmap
//     * @return The download URL of the uploaded image, or null if upload fails
//     */
//    suspend fun uploadProfileImage(userId: String, bitmap: Bitmap): String? {
//        return try {
//            // Create a reference to store the image
//            val imageRef = storageRef.child("profile_images/$userId/${UUID.randomUUID()}.jpg")
//
//            // Compress bitmap to JPEG format
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos)
//            val imageData = baos.toByteArray()
//
//            // Upload the image
//            val uploadTask = imageRef.putBytes(imageData).await()
//
//            // Get the download URL
//            val downloadUrl = imageRef.downloadUrl.await()
//
//            Log.d("FirebaseStorage", "Image uploaded successfully: $downloadUrl")
//            downloadUrl.toString()
//
//        } catch (e: Exception) {
//            Log.e("FirebaseStorage", "Error uploading image: ${e.message}", e)
//            null
//        }
//    }
//
//    /**
//     * Deletes an old profile image from Firebase Storage
//     * @param imageUrl The URL of the image to delete
//     */
//    suspend fun deleteProfileImage(imageUrl: String?): Boolean {
//        if (imageUrl.isNullOrEmpty()) return false
//
//        return try {
//            val imageRef = storage.getReferenceFromUrl(imageUrl)
//            imageRef.delete().await()
//            Log.d("FirebaseStorage", "Image deleted successfully")
//            true
//        } catch (e: Exception) {
//            Log.e("FirebaseStorage", "Error deleting image: ${e.message}", e)
//            false
//        }
//    }
//
//    /**
//     * Updates profile image - deletes old image and uploads new one
//     * @param userId The user's unique ID
//     * @param oldImageUrl The URL of the old image to delete
//     * @param newBitmap The new profile image bitmap
//     * @return The download URL of the new uploaded image
//     */
//    suspend fun updateProfileImage(
//        userId: String,
//        oldImageUrl: String?,
//        newBitmap: Bitmap
//    ): String? {
//        // Delete old image if it exists
//        if (!oldImageUrl.isNullOrEmpty()) {
//            deleteProfileImage(oldImageUrl)
//        }
//
//        // Upload new image
//        return uploadProfileImage(userId, newBitmap)
//    }
//}
//
///**
// * Extended FirestoreHelper with integrated image upload
// */
//object FirestoreHelperExtended {
//
//    /**
//     * Writes user data to Firestore and uploads profile image to Storage
//     * @param user The user object to save
//     * @param profileBitmap The profile image bitmap (nullable)
//     */
//    suspend fun writeUserWithImage(user: User, profileBitmap: Bitmap?) {
//        try {
//            val userId = FirebaseAuth.getInstance().currentUser?.uid
//                ?: throw IllegalStateException("User not authenticated")
//
//            // Get existing user to check for old profile image
//            val existingUser = FirestoreHelper.getUser(userId)
//            val oldImageUrl = existingUser?.profileImageUrl
//
//            // Upload new profile image if provided
//            val newImageUrl = if (profileBitmap != null) {
//                FirestoreHelper.updateProfileImage(userId, oldImageUrl, profileBitmap)
//            } else {
//                oldImageUrl // Keep existing image URL
//            }
//
//            // Update user object with image URL
//            val updatedUser = user.copy(profileImageUrl = newImageUrl)
//
//            // Write to Firestore
//            FirestoreHelper.writeUser(updatedUser, null)
//
//            Log.d("FirestoreHelper", "User saved with profile image")
//
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Error saving user with image: ${e.message}", e)
//            throw e
//        }
//    }
//
//    /**
//     * Deletes user's profile image from Storage
//     * @param userId The user's unique ID
//     */
//    suspend fun deleteUserProfileImage(userId: String) {
//        try {
//            val user = FirestoreHelper.getUser(userId)
//            val imageUrl = user?.profileImageUrl
//
//            if (imageUrl != null) {
//                FirestoreHelper.deleteProfileImage(imageUrl)
//
//                // Update Firestore to remove image URL
//                val updatedUser = user.copy(profileImageUrl = null)
//                FirestoreHelper.writeUser(updatedUser, null)
//            }
//
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
//            throw e
//        }
//    }
//}




/**
 * UPDATED FirestoreHelper with integrated Firebase Storage
 * All operations use the current authenticated user
 */
//object FirestoreHelper {
//    @SuppressLint("StaticFieldLeak")
//    private val db = Firebase.firestore
//    private val auth = Firebase.auth
//    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
//    private val storageRef: StorageReference = storage.reference
//
//    fun getVerifiedUser(): Pair<String, String> {
//        val user = FirebaseAuth.getInstance().currentUser
//        requireNotNull(user?.uid) { "No authenticated user UID found." }
//        requireNotNull(user.email) { "Authenticated user has no email." }
//        return user.uid to user.email!!
//    }
//
//    // Get current user ID
//    private fun getCurrentUserId(): String {
//        return auth.currentUser?.uid ?: throw Exception("No authenticated user")
//    }
//
//    // ==================== EMERGENCY CONTACTS ====================
//
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val contactRef = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document()
//
//            val contactWithId = contact.copy(contactId = contactRef.id)
//            contactRef.set(contactWithId).await()
//        }
//    }
//
//    suspend fun readAllEmergencyContacts(): List<EmergencyContact> {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val snapshot = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .get()
//                .await()
//
//            snapshot.documents.mapNotNull { doc ->
//                doc.toObject(EmergencyContact::class.java)
//            }
//        }
//    }
//
//    suspend fun deleteEmergencyContact(contactId: String) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contactId)
//                .delete()
//                .await()
//        }
//    }
//
//    suspend fun updateEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contact.contactId)
//                .set(contact)
//                .await()
//        }
//    }
//
//    suspend fun getEmergencyContact(): EmergencyContact? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("emergencyContacts")
//                    .limit(1)
//                    .get()
//                    .await()
//                snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting emergency contact: ${e.message}")
//                null
//            }
//        }
//    }
//
//    // ==================== USER OPERATIONS ====================
//
//    suspend fun getUser(userId: String): User? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .get()
//                    .await()
//                snapshot.toObject(User::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting user: ${e.message}")
//                null
//            }
//        }
//    }
//
//    /**
//     * Writes user data with optional profile image
//     * Uses current authenticated user - no UUID generation needed
//     */
//    suspend fun writeUser(user: User, bitmap: Bitmap?) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//
//            // Get existing user to check for old profile image
//            val existingUser = getUser(userId)
//            val oldImageUrl = existingUser?.profileImageUrl
//
//            // Upload new profile image if provided
//            val profileImageUrl = if (bitmap != null) {
//                // Delete old image and upload new one
//                updateProfileImage(oldImageUrl, bitmap)
//            } else {
//                // Keep existing image URL if no new image provided
//                oldImageUrl
//            }
//
//            // Update user object with image URL
//            val userWithImage = user.copy(profileImageUrl = profileImageUrl)
//
//            // Save to Firestore
//            db.collection("users")
//                .document(userId)
//                .set(userWithImage)
//                .await()
//
//            Log.d("FirestoreHelper", "User saved successfully with image URL: $profileImageUrl")
//        }
//    }
//
//    // ==================== HEALTH INFORMATION ====================
//
//    suspend fun getHealthInformation(): HealthInformation? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("healthInformation")
//                    .document("info")
//                    .get()
//                    .await()
//                snapshot.toObject(HealthInformation::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting health info: ${e.message}")
//                null
//            }
//        }
//    }
//
//    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("healthInformation")
//                .document("info")
//                .set(healthInfo)
//                .await()
//        }
//    }
//
//    // ==================== FIREBASE STORAGE OPERATIONS ====================
//
//    /**
//     * Uploads profile image to Firebase Storage
//     * Path: profile_images/{currentUserId}/profile_image.jpg
//     * Only generates UUID for the filename to prevent caching issues
//     */
//    private suspend fun uploadProfileImage(bitmap: Bitmap): String? {
//        return try {
//            val userId = getCurrentUserId()
//
//            // Use consistent path with UUID filename to avoid caching issues
//            val imageRef = storageRef.child("profile_images/$userId/${UUID.randomUUID()}.jpg")
//
//            // Compress bitmap to JPEG format (85% quality for balance)
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos)
//            val imageData = baos.toByteArray()
//
//            // Upload the image
//            imageRef.putBytes(imageData).await()
//
//            // Get the download URL
//            val downloadUrl = imageRef.downloadUrl.await()
//
//            Log.d("FirestoreHelper", "Profile image uploaded: $downloadUrl")
//            downloadUrl.toString()
//
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Error uploading profile image: ${e.message}", e)
//            null
//        }
//    }
//
//    /**
//     * Deletes profile image from Firebase Storage
//     */
//    private suspend fun deleteProfileImage(imageUrl: String?): Boolean {
//        if (imageUrl.isNullOrEmpty()) return false
//
//        return try {
//            val imageRef = storage.getReferenceFromUrl(imageUrl)
//            imageRef.delete().await()
//            Log.d("FirestoreHelper", "Profile image deleted successfully")
//            true
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
//            false
//        }
//    }
//
//    /**
//     * Updates profile image - deletes old and uploads new
//     * Used internally by writeUser
//     */
//    private suspend fun updateProfileImage(oldImageUrl: String?, newBitmap: Bitmap): String? {
//        // Delete old image if it exists
//        if (!oldImageUrl.isNullOrEmpty()) {
//            deleteProfileImage(oldImageUrl)
//        }
//
//        // Upload new image
//        return uploadProfileImage(newBitmap)
//    }
//
//    /**
//     * Deletes user's profile image and updates Firestore
//     * Call this when user explicitly deletes their profile picture
//     */
//    suspend fun deleteUserProfileImage() {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val user = getUser(userId)
//                val imageUrl = user?.profileImageUrl
//
//                if (imageUrl != null) {
//                    // Delete from Storage
//                    deleteProfileImage(imageUrl)
//
//                    // Update Firestore to remove image URL
//                    val updatedUser = user.copy(profileImageUrl = null)
//                    db.collection("users")
//                        .document(userId)
//                        .set(updatedUser)
//                        .await()
//
//                    Log.d("FirestoreHelper", "User profile image deleted")
//                }
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting user profile image: ${e.message}", e)
//                throw e
//            }
//        }
//    }
//}
//
//object FirestoreHelper {
//    @SuppressLint("StaticFieldLeak")
//    private val db = Firebase.firestore
//    private val auth = Firebase.auth
//    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
//    private val storageRef: StorageReference = storage.reference
//
//    fun getVerifiedUser(): Pair<String, String> {
//        val user = FirebaseAuth.getInstance().currentUser
//        requireNotNull(user?.uid) { "No authenticated user UID found." }
//        requireNotNull(user.email) { "Authenticated user has no email." }
//        return user.uid to user.email!!
//    }
//
//    // Get current user ID
//    private fun getCurrentUserId(): String {
//        return auth.currentUser?.uid ?: throw Exception("No authenticated user")
//    }
//
//    // ==================== EMERGENCY CONTACTS ====================
//
//    /**
//     * Writes a new emergency contact to the user's emergencyContacts subcollection
//     * Each contact is stored as a separate document in the subcollection
//     */
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val contactRef = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document() // Auto-generate document ID
//
//            val contactWithId = contact.copy(contactId = contactRef.id)
//            contactRef.set(contactWithId).await()
//
//            Log.d("FirestoreHelper", "Emergency contact saved: ${contactRef.id}")
//        }
//    }
//
//    /**
//     * Reads all emergency contacts for the current user
//     * Returns a list of all contacts from the emergencyContacts subcollection
//     */
//    suspend fun readAllEmergencyContacts(): List<EmergencyContact> {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val snapshot = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .get()
//                .await()
//
//            val contacts = snapshot.documents.mapNotNull { doc ->
//                doc.toObject(EmergencyContact::class.java)
//            }
//
//            Log.d("FirestoreHelper", "Found ${contacts.size} emergency contacts")
//            contacts
//        }
//    }
//
//    /**
//     * Deletes a specific emergency contact by ID
//     */
//    suspend fun deleteEmergencyContact(contactId: String) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contactId)
//                .delete()
//                .await()
//
//            Log.d("FirestoreHelper", "Emergency contact deleted: $contactId")
//        }
//    }
//
//    /**
//     * Updates an existing emergency contact
//     */
//    suspend fun updateEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contact.contactId)
//                .set(contact)
//                .await()
//
//            Log.d("FirestoreHelper", "Emergency contact updated: ${contact.contactId}")
//        }
//    }
//
//    /**
//     * Gets the first emergency contact (for backward compatibility)
//     */
//    suspend fun getEmergencyContact(): EmergencyContact? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("emergencyContacts")
//                    .limit(1)
//                    .get()
//                    .await()
//                snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting emergency contact: ${e.message}")
//                null
//            }
//        }
//    }
//
//    // ==================== USER OPERATIONS ====================
//
//    suspend fun getUser(userId: String): User? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .get()
//                    .await()
//                snapshot.toObject(User::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting user: ${e.message}")
//                null
//            }
//        }
//    }
//
//    /**
//     * Writes user data with optional profile image
//     * Uses current authenticated user - no UUID generation needed
//     * Automatically handles old image deletion when new image is provided
//     */
//    suspend fun writeUser(user: User, bitmap: Bitmap?) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//
//            // Get existing user to check for old profile image
//            val existingUser = getUser(userId)
//            val oldImageUrl = existingUser?.profileImageUrl
//
//            // Upload new profile image if provided
//            val profileImageUrl = if (bitmap != null) {
//                // Delete old image and upload new one
//                updateProfileImage(oldImageUrl, bitmap)
//            } else {
//                // Keep existing image URL if no new image provided
//                oldImageUrl
//            }
//
//            // Update user object with image URL
//            val userWithImage = user.copy(profileImageUrl = profileImageUrl)
//
//            // Save to Firestore
//            db.collection("users")
//                .document(userId)
//                .set(userWithImage)
//                .await()
//
//            Log.d("FirestoreHelper", "User saved successfully with image URL: $profileImageUrl")
//        }
//    }
//
//    // ==================== HEALTH INFORMATION ====================
//
//    /**
//     * Gets health information for current user
//     * No need to pass userId - uses getCurrentUserId()
//     */
//    suspend fun getHealthInformation(): HealthInformation? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("healthInformation")
//                    .document("info")
//                    .get()
//                    .await()
//                snapshot.toObject(HealthInformation::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting health info: ${e.message}")
//                null
//            }
//        }
//    }
//
//    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("healthInformation")
//                .document("info")
//                .set(healthInfo)
//                .await()
//        }
//    }
//
//    // ==================== FIREBASE STORAGE OPERATIONS ====================
//
//    /**
//     * Uploads profile image to Firebase Storage
//     * Path: profile_images/{currentUserId}/{randomUUID}.jpg
//     * Only generates UUID for the filename to prevent caching issues
//     */
//    private suspend fun uploadProfileImage(bitmap: Bitmap): String? {
//        return try {
//            val userId = getCurrentUserId()
//
//            // Use consistent path with UUID filename to avoid caching issues
//            val imageRef = storageRef.child("profile_images/$userId/${UUID.randomUUID()}.jpg")
//
//            // Compress bitmap to JPEG format (85% quality for balance)
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos)
//            val imageData = baos.toByteArray()
//
//            // Upload the image
//            imageRef.putBytes(imageData).await()
//
//            // Get the download URL
//            val downloadUrl = imageRef.downloadUrl.await()
//
//            Log.d("FirestoreHelper", "Profile image uploaded: $downloadUrl")
//            downloadUrl.toString()
//
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Error uploading profile image: ${e.message}", e)
//            null
//        }
//    }
//
//    /**
//     * Deletes profile image from Firebase Storage
//     */
//    private suspend fun deleteProfileImage(imageUrl: String?): Boolean {
//        if (imageUrl.isNullOrEmpty()) return false
//
//        return try {
//            val imageRef = storage.getReferenceFromUrl(imageUrl)
//            imageRef.delete().await()
//            Log.d("FirestoreHelper", "Profile image deleted successfully")
//            true
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
//            false
//        }
//    }
//
//    /**
//     * Updates profile image - deletes old and uploads new
//     * Used internally by writeUser
//     */
//    private suspend fun updateProfileImage(oldImageUrl: String?, newBitmap: Bitmap): String? {
//        // Delete old image if it exists
//        if (!oldImageUrl.isNullOrEmpty()) {
//            deleteProfileImage(oldImageUrl)
//        }
//
//        // Upload new image
//        return uploadProfileImage(newBitmap)
//    }
//
//    /**
//     * Deletes user's profile image and updates Firestore
//     * Call this when user explicitly deletes their profile picture
//     */
//    suspend fun deleteUserProfileImage() {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val user = getUser(userId)
//                val imageUrl = user?.profileImageUrl
//
//                if (imageUrl != null) {
//                    // Delete from Storage
//                    deleteProfileImage(imageUrl)
//
//                    // Update Firestore to remove image URL
//                    val updatedUser = user.copy(profileImageUrl = null)
//                    db.collection("users")
//                        .document(userId)
//                        .set(updatedUser)
//                        .await()
//
//                    Log.d("FirestoreHelper", "User profile image deleted")
//                }
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting user profile image: ${e.message}", e)
//                throw e
//            }
//        }
//    }
//
//    // Add these functions to your FirestoreHelper object
//
//    /**
//     * Deletes all user data including subcollections and storage files
//     * This is a complete cleanup of user data from Firebase
//     */
//    suspend fun deleteAllUserData(userId: String) {
//        return withContext(Dispatchers.IO) {
//            try {
//                // 1. Delete all emergency contacts
//                val emergencyContactsSnapshot = db.collection("users")
//                    .document(userId)
//                    .collection("emergencyContacts")
//                    .get()
//                    .await()
//
//                emergencyContactsSnapshot.documents.forEach { doc ->
//                    doc.reference.delete().await()
//                }
//                Log.d("FirestoreHelper", "Deleted emergency contacts")
//
//                // 2. Delete health information
//                db.collection("users")
//                    .document(userId)
//                    .collection("healthInformation")
//                    .document("info")
//                    .delete()
//                    .await()
//                Log.d("FirestoreHelper", "Deleted health information")
//
//                // 3. Delete profile image from storage
//                val user = getUser(userId)
//                user?.profileImageUrl?.let { imageUrl ->
//                    deleteProfileImage(imageUrl)
//                }
//                Log.d("FirestoreHelper", "Deleted profile image")
//
//                // 4. Delete all files in user's storage folder
//                try {
//                    val userStorageRef = storageRef.child("profile_images/$userId")
//                    val listResult = userStorageRef.listAll().await()
//                    listResult.items.forEach { item ->
//                        item.delete().await()
//                    }
//                    Log.d("FirestoreHelper", "Deleted all storage files")
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Error deleting storage files: ${e.message}")
//                }
//
//                // 5. Finally, delete the user document
//                db.collection("users")
//                    .document(userId)
//                    .delete()
//                    .await()
//                Log.d("FirestoreHelper", "Deleted user document")
//
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting user data: ${e.message}", e)
//                throw e
//            }
//        }
//    }
//
//    /**
//     * Sends account deletion confirmation email and deletes user account
//     * This function:
//     * 1. Sends a verification email to the user
//     * 2. Waits for user to verify
//     * 3. Deletes all Firestore data
//     * 4. Deletes the Firebase Auth account
//     */
//    suspend fun deleteUserAccount(): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                val user = auth.currentUser ?: throw Exception("No authenticated user")
//                val userId = user.uid
//
//                // Send verification email before deletion
//                user.sendEmailVerification().await()
//                Log.d("FirestoreHelper", "Verification email sent")
//
//                // Note: In a real-world scenario, you'd want to wait for email verification
//                // For now, we'll proceed with deletion after a delay
//                // In production, implement a cloud function that triggers on email verification
//
//                // Delete all Firestore data
//                deleteAllUserData(userId)
//
//                // Delete Firebase Auth account
//                user.delete().await()
//                Log.d("FirestoreHelper", "User account deleted from Firebase Auth")
//
//                true
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting user account: ${e.message}", e)
//                false
//            }
//        }
//    }
//
//    /**
//     * Initiates account deletion process with re-authentication
//     * Firebase requires recent authentication before account deletion
//     */
////    suspend fun deleteUserAccountWithReauth(email: String, password: String): Boolean {
////        return withContext(Dispatchers.IO) {
////            try {
////                val user = auth.currentUser ?: throw Exception("No authenticated user")
////
////                // Re-authenticate user before deletion (Firebase security requirement)
////                val credential = EmailAuthProvider.getCredential(email, password)
////                user.reauthenticate(credential).await()
////                Log.d("FirestoreHelper", "User re-authenticated successfully")
////
////                // Now delete the account
////                val userId = user.uid
////                deleteAllUserData(userId)
////                user.delete().await()
////
////                Log.d("FirestoreHelper", "User account deleted successfully")
////                true
////            } catch (e: Exception) {
////                Log.e("FirestoreHelper", "Error deleting account with reauth: ${e.message}", e)
////                false
////            }
////        }
////    }
//
//
//    suspend fun deleteUserAccountWithReauth(email: String, password: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                val user = auth.currentUser ?: throw Exception("No authenticated user")
//
//                // Re-authenticate
//                val credential = EmailAuthProvider.getCredential(email, password)
//                user.reauthenticate(credential).await()
//
//                // Send notification email BEFORE deletion
//                sendSimpleEmail(
//                    toEmail = email,
//                    subject = "Account Deleted",
//                    body = "Your account has been successfully deleted. We're sorry to see you go!"
//                )
//
//                // Delete account
//                val userId = user.uid
//                deleteAllUserData(userId)
//                user.delete().await()
//
//                true
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error: ${e.message}", e)
//                false
//            }
//        }
//    }
//    // Simple email sender (requires SMTP setup)
//    private fun sendSimpleEmail(toEmail: String, subject: String, body: String) {
//        // This is a simplified example - you'd need proper SMTP configuration
//        // Most apps use a backend service for sending emails
//    }
//
//
//}

//object FirestoreHelper {
//    @SuppressLint("StaticFieldLeak")
//    private val db = Firebase.firestore
//    private val auth = Firebase.auth
//    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
//    private val storageRef: StorageReference = storage.reference
//
//
//    // ==================== NEW: BULK UPDATE FUNCTION ====================
//
//    suspend fun updateUserData(
//        userId: String,
//        updatedUser: User,
//        updatedContacts: List<EmergencyContact>,
//        updatedHealth: HealthInformation?
//    ) {
//        return withContext(Dispatchers.IO) {
//            try {
//                // 1. Update/Write User Profile (handles image URL)
//                // Note: The original writeUser accepts a Bitmap, but since we are only updating
//                // text fields here, we must assume the user object already contains the existing
//                // image URL, or that you've handled image selection separately outside of this function.
//                // We'll use a modified write function that doesn't expect a Bitmap for this text update scenario.
//
//                // Assuming you have a simplified writeUser for text-only updates:
//                db.collection("users")
//                    .document(userId)
//                    .set(updatedUser)
//                    .await()
//                Log.d("FirestoreHelper", "User data updated successfully: $userId")
//
//
//                // 2. Update/Write Emergency Contacts
//                // Since there is no explicit 'delete' for removed contacts in this update,
//                // we'll assume the contacts list only contains existing or new/modified contacts.
//                // If deletion is required, the list management in AccountScreen needs to flag deletions.
//                updatedContacts.forEach { contact ->
//                    if (contact.contactId.isBlank()) {
//                        // Contact is new, use writeEmergencyContact to generate ID
//                        writeEmergencyContact(contact)
//                    } else {
//                        // Contact exists, use updateEmergencyContact
//                        updateEmergencyContact(contact)
//                    }
//                }
//                Log.d("FirestoreHelper", "Emergency contacts processed: ${updatedContacts.size}")
//
//
//                // 3. Update/Write Health Information (Using 'info' document)
//                if (updatedHealth != null) {
//                    writeHealthInformation(updatedHealth)
//                    Log.d("FirestoreHelper", "Health data updated successfully.")
//                } else {
//                    // Optional: If updatedHealth is null and you want to delete the document
//                    db.collection("users")
//                        .document(userId)
//                        .collection("healthInformation")
//                        .document("info")
//                        .delete()
//                        .await()
//                    Log.d("FirestoreHelper", "Health data deleted/not present.")
//                }
//
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error performing bulk update: ${e.message}", e)
//                throw e
//            }
//        }
//    }
//
//    fun getVerifiedUser(): Pair<String, String> {
//        val user = FirebaseAuth.getInstance().currentUser
//        requireNotNull(user?.uid) { "No authenticated user UID found." }
//        requireNotNull(user.email) { "Authenticated user has no email." }
//        return user.uid to user.email!!
//    }
//
//    // Get current user ID
//    private fun getCurrentUserId(): String {
//        return auth.currentUser?.uid ?: throw Exception("No authenticated user")
//    }
//
//    // ==================== EMERGENCY CONTACTS ====================
//
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            val contactRef = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document()
//
//            val contactWithId = contact.copy(contactId = contactRef.id)
//            contactRef.set(contactWithId).await()
//
//            Log.d("FirestoreHelper", "Emergency contact saved: ${contactRef.id}")
//        }
//    }
//
//    /**
//     * FIX: Updated to accept userId to ensure only the current user's contacts are fetched.
//     */
//    suspend fun readAllEmergencyContacts(userId: String): List<EmergencyContact> {
//        return withContext(Dispatchers.IO) {
//            val snapshot = db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .get()
//                .await()
//
//            val contacts = snapshot.documents.mapNotNull { doc ->
//                doc.toObject(EmergencyContact::class.java)
//            }
//
//            Log.d("FirestoreHelper", "Found ${contacts.size} emergency contacts for user $userId")
//            contacts
//        }
//    }
//
//    suspend fun deleteEmergencyContact(contactId: String) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contactId)
//                .delete()
//                .await()
//
//            Log.d("FirestoreHelper", "Emergency contact deleted: $contactId")
//        }
//    }
//
//    suspend fun updateEmergencyContact(contact: EmergencyContact) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("emergencyContacts")
//                .document(contact.contactId)
//                .set(contact)
//                .await()
//
//            Log.d("FirestoreHelper", "Emergency contact updated: ${contact.contactId}")
//        }
//    }
//
//    suspend fun getEmergencyContact(): EmergencyContact? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("emergencyContacts")
//                    .limit(1)
//                    .get()
//                    .await()
//                snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting emergency contact: ${e.message}")
//                null
//            }
//        }
//    }
//
//    // ==================== USER OPERATIONS ====================
//
//    suspend fun getUser(userId: String): User? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .get()
//                    .await()
//                snapshot.toObject(User::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting user: ${e.message}")
//                null
//            }
//        }
//    }
//
//    suspend fun writeUser(user: User, bitmap: Bitmap?) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//
//            // Get existing user to check for old profile image
//            val existingUser = getUser(userId)
//            val oldImageUrl = existingUser?.profileImageUrl
//
//            // Upload new profile image if provided
//            val profileImageUrl = if (bitmap != null) {
//                updateProfileImage(oldImageUrl, bitmap)
//            } else {
//                oldImageUrl
//            }
//
//            // Update user object with image URL
//            val userWithImage = user.copy(profileImageUrl = profileImageUrl)
//
//            // Save to Firestore
//            db.collection("users")
//                .document(userId)
//                .set(userWithImage)
//                .await()
//
//            Log.d("FirestoreHelper", "User saved successfully with image URL: $profileImageUrl")
//        }
//    }
//
//    // ==================== HEALTH INFORMATION ====================
//
//    /**
//     * FIX: Updated to accept userId to ensure only the current user's health info is fetched.
//     */
//    suspend fun getHealthInformation(userId: String): HealthInformation? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val snapshot = db.collection("users")
//                    .document(userId)
//                    .collection("healthInformation")
//                    .document("info")
//                    .get()
//                    .await()
//                snapshot.toObject(HealthInformation::class.java)
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error getting health info: ${e.message}")
//                null
//            }
//        }
//    }
//
//    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//        return withContext(Dispatchers.IO) {
//            val userId = getCurrentUserId()
//            db.collection("users")
//                .document(userId)
//                .collection("healthInformation")
//                .document("info")
//                .set(healthInfo)
//                .await()
//        }
//    }
//
//    // ==================== FIREBASE STORAGE OPERATIONS ====================
//
//    private suspend fun uploadProfileImage(bitmap: Bitmap): String? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val fileName = "${System.currentTimeMillis()}.jpg"
//                val imageRef = storageRef.child("profile_images/$userId/$fileName")
//
//                // Compress bitmap to JPEG format
//                val baos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos)
//                val imageData = baos.toByteArray()
//
//                // Upload with metadata
//                val metadata = storageMetadata {
//                    contentType = "image/jpeg"
//                }
//
//                // Upload the image with proper error handling
//                val uploadTask = imageRef.putBytes(imageData, metadata)
//
//                uploadTask.await()
//
//                // Get the download URL
//                val downloadUrl = imageRef.downloadUrl.await()
//
//                Log.d("FirestoreHelper", "Profile image uploaded: $downloadUrl")
//                downloadUrl.toString()
//
//            } catch (e: StorageException) {
//                Log.e("FirestoreHelper", "Storage error: ${e.errorCode} - ${e.message}", e)
//                null
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error uploading profile image: ${e.message}", e)
//                null
//            }
//        }
//    }
//
//    private suspend fun deleteProfileImage(imageUrl: String?): Boolean {
//        if (imageUrl.isNullOrEmpty()) return false
//
//        return withContext(Dispatchers.IO) {
//            try {
//                val imageRef = storage.getReferenceFromUrl(imageUrl)
//                imageRef.delete().await()
//                Log.d("FirestoreHelper", "Profile image deleted successfully")
//                true
//            } catch (e: StorageException) {
//                // Ignore 404 errors - file might already be deleted
//                if (e.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
//                    Log.d("FirestoreHelper", "Image already deleted or doesn't exist")
//                    true
//                } else {
//                    Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
//                    false
//                }
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}", e)
//                false
//            }
//        }
//    }
//
//    private suspend fun updateProfileImage(oldImageUrl: String?, newBitmap: Bitmap): String? {
//        // Delete old image if it exists
//        if (!oldImageUrl.isNullOrEmpty()) {
//            deleteProfileImage(oldImageUrl)
//        }
//
//        // Upload new image
//        return uploadProfileImage(newBitmap)
//    }
//
//    suspend fun deleteUserProfileImage() {
//        return withContext(Dispatchers.IO) {
//            try {
//                val userId = getCurrentUserId()
//                val user = getUser(userId)
//                val imageUrl = user?.profileImageUrl
//
//                if (imageUrl != null) {
//                    // Delete from Storage
//                    deleteProfileImage(imageUrl)
//
//                    // Update Firestore to remove image URL
//                    val updatedUser = user.copy(profileImageUrl = null)
//                    db.collection("users")
//                        .document(userId)
//                        .set(updatedUser)
//                        .await()
//
//                    Log.d("FirestoreHelper", "User profile image deleted")
//                }
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting user profile image: ${e.message}", e)
//                throw e
//            }
//        }
//    }
//
//    // ==================== DELETE ACCOUNT ====================
//
//    suspend fun deleteAllUserData(userId: String) {
//        return withContext(Dispatchers.IO) {
//            try {
//                // 1. Delete all emergency contacts
//                try {
//                    val emergencyContactsSnapshot = db.collection("users")
//                        .document(userId)
//                        .collection("emergencyContacts")
//                        .get()
//                        .await()
//
//                    emergencyContactsSnapshot.documents.forEach { doc ->
//                        doc.reference.delete().await()
//                    }
//                    Log.d("FirestoreHelper", "Deleted emergency contacts")
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Error deleting contacts: ${e.message}")
//                }
//
//                // 2. Delete health information
//                try {
//                    db.collection("users")
//                        .document(userId)
//                        .collection("healthInformation")
//                        .document("info")
//                        .delete()
//                        .await()
//                    Log.d("FirestoreHelper", "Deleted health information")
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Error deleting health info: ${e.message}")
//                }
//
//                // 3. Delete profile image from storage
//                try {
//                    val user = getUser(userId)
//                    user?.profileImageUrl?.let { imageUrl ->
//                        deleteProfileImage(imageUrl)
//                    }
//                    Log.d("FirestoreHelper", "Deleted profile image")
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Error deleting profile image: ${e.message}")
//                }
//
//                // 4. Delete all files in user's storage folder
//                try {
//                    val userStorageRef = storageRef.child("profile_images/$userId")
//                    val listResult = userStorageRef.listAll().await()
//                    listResult.items.forEach { item ->
//                        try {
//                            item.delete().await()
//                        } catch (e: Exception) {
//                            Log.e("FirestoreHelper", "Error deleting item: ${e.message}")
//                        }
//                    }
//                    Log.d("FirestoreHelper", "Deleted all storage files")
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Error deleting storage folder: ${e.message}")
//                }
//
//                // 5. Finally, delete the user document
//                db.collection("users")
//                    .document(userId)
//                    .delete()
//                    .await()
//                Log.d("FirestoreHelper", "Deleted user document")
//
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting user data: ${e.message}", e)
//                // Don't throw - continue with deletion even if some parts fail
//            }
//        }
//    }
//
//    suspend fun deleteUserAccountWithReauth(email: String, password: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                val user = auth.currentUser ?: throw Exception("No authenticated user")
//
//                // Re-authenticate
//                val credential = EmailAuthProvider.getCredential(email, password)
//                user.reauthenticate(credential).await()
//                Log.d("FirestoreHelper", "User re-authenticated successfully")
//
//                // Delete account
//                val userId = user.uid
//                deleteAllUserData(userId)
//                user.delete().await()
//
//                Log.d("FirestoreHelper", "User account deleted successfully")
//                true
//            } catch (e: Exception) {
//                Log.e("FirestoreHelper", "Error deleting account: ${e.message}", e)
//                false
//            }
//        }
//    }
//}


object FirestoreHelper {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference


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