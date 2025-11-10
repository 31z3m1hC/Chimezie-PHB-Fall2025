package com.unh.personal_health_buddy.Authentication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.unh.personal_health_buddy.database.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

object FirestoreHelper {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun getVerifiedUser(): Pair<String, String> {
        val user = FirebaseAuth.getInstance().currentUser
        requireNotNull(user?.uid) { "No authenticated user UID found." }
        requireNotNull(user.email) { "Authenticated user has no email." }
        return user.uid to user.email!!
    }

    // -------------------- Users --------------------
//    suspend fun writeUser(user: User, profileImage: Bitmap? = null) {
//        try {
//            val (uid, email) = getVerifiedUser()
//            require(user.email == email) { "Email mismatch: form email does not match authenticated email." }
//
//            val userMap = hashMapOf(
//                "firstname" to user.firstname,
//                "lastname" to user.lastname,
//                "dateOfBirth" to user.dateOfBirth,
//                "homeAddress" to user.homeAddress,
//                "gender" to user.gender.name,
//                "email" to user.email,
//                "medication" to user.medication,
//                "phoneNumber" to user.phoneNumber,
//                "allergies" to user.allergies,
//                "city" to user.city,
//                "profileImageUrl" to user.profileImageUrl
//            )
//
//            profileImage?.let { bitmap ->
//                try {
//                    val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance()
//                        .reference.child("profilePics/$uid.jpg")
//
//                    val baos = ByteArrayOutputStream()
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
//                    val imageData = baos.toByteArray()
//
//                    storageRef.putBytes(imageData).await()
//                    val downloadUrl = storageRef.downloadUrl.await().toString()
//
//                    userMap["profileImageUrl"] = downloadUrl
//                    Log.d("FirestoreHelper", "Profile image uploaded successfully")
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Failed to upload profile image", e)
//                }
//            }
//
//            db.collection("users")
//                .document(uid)
//                .set(userMap)
//                .await()
//
//            Log.d("FirestoreHelper", "User written to Firestore with UID: $uid")
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Failed to write user to Firestore", e)
//        }
//    }

//    suspend fun writeUser(user: User, profileImage: Bitmap?) {
//        try {
//            val (uid, email) = getVerifiedUser()
//            require(user.email == email) { "Email mismatch" }
//
//            var imageUrl: String? = null
//
//            //Upload image first if exists
//            if (profileImage != null) {
//                try {
//                    val baos = ByteArrayOutputStream()
//                    profileImage.compress(Bitmap.CompressFormat.JPEG, 90, baos)
//                    val data = baos.toByteArray()
//
//                    val storageRef = FirebaseStorage.getInstance()
//                        .reference.child("profilePics/$uid.jpg")
//
//                    storageRef.putBytes(data).await()
//                    imageUrl = storageRef.downloadUrl.await().toString()
//
//                    Log.d("FirestoreHelper", "Image uploaded: $imageUrl")
//
//                } catch (e: Exception) {
//                    Log.e("FirestoreHelper", "Image upload failed", e)
//                }
//            }
//
//            // Now write Firestore with correct URL
//            val userMap = hashMapOf(
//                "firstname" to user.firstname,
//                "lastname" to user.lastname,
//                "dateOfBirth" to user.dateOfBirth,
//                "homeAddress" to user.homeAddress,
//                "gender" to user.gender.name,
//                "email" to user.email,
//                "medication" to user.medication,
//                "phoneNumber" to user.phoneNumber,
//                "allergies" to user.allergies,
//                "city" to user.city,
//                "profileImageUrl" to (imageUrl ?: user.profileImageUrl)
//            )
//
//            db.collection("users").document(uid).set(userMap).await()
//            Log.d("FirestoreHelper", "User saved to Firestore")
//
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Failed to save user", e)
//        }
//    }
//
//
//
//
//    suspend fun readUser(): User? {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
//        return snapshot.toObject(User::class.java)
//    }


//
//    suspend fun writeUser(user: User, profileBitmap: Bitmap?) {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid
//            ?: throw Exception("User not logged in")
//
//        var profileUrl: String? = null
//
//        profileBitmap?.let { bitmap ->
//            // Upload to Storage with correct extension
//            val imageRef = storage.reference.child("profileImages/$uid.jpg")
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
//            val data = baos.toByteArray()
//
//            imageRef.putBytes(data).await()      // Wait for upload
//            profileUrl = imageRef.downloadUrl.await().toString()  // Get URL
//        }
//
//        // Save user in Firestore
//        val userWithUrl = user.copy(profileImageUrl = profileUrl)
//        db.collection("users")
//            .document(uid)
//            .set(userWithUrl)
//            .await()
//    }
//
//    // ------------------ Read user ------------------
//    suspend fun readUser(): User? {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid
//            ?: return null
//
//        return try {
//            val snapshot = db.collection("users")
//                .document(uid)
//                .get()
//                .await()
//
//            Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
//            snapshot.toObject(User::class.java)
//        } catch (e: Exception) {
//            Log.e("FirestoreHelper", "Failed to read user", e)
//            null
//        }
//    }
//
//
//
//    suspend fun deleteUser() {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
//    }
//
//    // -------------------- Health Info --------------------
//    suspend fun deleteHealthInfo() {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("HealthInfo")
//            .document("details")
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "Health info deleted for UID: $uid")
//    }
//
//
//    suspend fun writeHealthInformation(health: HealthInformation) {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//        db.collection("users")
//            .document(uid)
//            .collection("HealthInfo")
//            .document("details")
//            .set(health)
//            .await()
//        Log.d("FirestoreHelper", "Health information written for UID: $uid")
//    }
//
//
//
//
//    // -------------------- Emergency Contact --------------------
////    suspend fun writeEmergencyContact(contact: EmergencyContact) {
////        val (uid, _) = getVerifiedUser()
////        db.collection("users")
////            .document(uid)
////            .collection("EmergencyContact")
////            .document(contact.contactId)
////            .set(contact)
////            .await()
////        Log.d("FirestoreHelper", "Emergency contact written for UID: $uid")
////    }
//
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//        val docRef = db.collection("users")
//            .document(uid)
//            .collection("EmergencyContacts")
//            .document() // auto ID
//
//        val contactWithId = contact.copy(contactId = docRef.id)
//
//        docRef.set(contactWithId).await()
//        Log.d("FirestoreHelper", "Emergency contact written for UID: $uid")
//    }
//
//    suspend fun readEmergencyContact(): List<EmergencyContact> {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .collection("EmergencyContact")
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "Emergency contacts read for UID: $uid")
//        return snapshot.toObjects(EmergencyContact::class.java)
//    }
//
//    suspend fun deleteEmergencyContact(contactId: String) {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("EmergencyContact")
//            .document(contactId)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "🗑️ Emergency contact deleted for UID: $uid")
//    }
//
//    // -------------------- Chats --------------------
//    suspend fun writeChat(chat: Chats) {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("Chats")
//            .document(chat.chatId)
//            .set(chat)
//            .await()
//        Log.d("FirestoreHelper", "Chat written for UID: $uid")
//    }
//
//    suspend fun readChats(): List<Chats> {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .collection("Chats")
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "Chats read for UID: $uid")
//        return snapshot.toObjects(Chats::class.java)
//    }
//
//    suspend fun deleteChat(chatId: String) {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("Chats")
//            .document(chatId)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "Chat deleted for UID: $uid")
//    }
//
//    // -------------------- Articles --------------------
//    suspend fun writeArticle(article: Articles) {
//        db.collection("articles")
//            .add(article)
//            .await()
//        Log.d("FirestoreHelper", "Article written")
//    }
//
//    suspend fun readArticles(): List<Articles> {
//        val snapshot = db.collection("articles")
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "Articles read")
//        return snapshot.toObjects(Articles::class.java)
//    }
//
//    suspend fun deleteArticle(articleId: String) {
//        db.collection("articles")
//            .document(articleId)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "Article deleted")
//    }
//
//    // -------------------- FAQs --------------------
//    suspend fun writeFAQ(faq: FAQs) {
//        db.collection("faqs")
//            .add(faq)
//            .await()
//        Log.d("FirestoreHelper", "FAQ written")
//    }
//
//    suspend fun readFAQs(): List<FAQs> {
//        val snapshot = db.collection("faqs")
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "FAQs read")
//        return snapshot.toObjects(FAQs::class.java)
//    }
//
//    suspend fun deleteFAQ(faqId: String) {
//        db.collection("faqs")
//            .document(faqId)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "FAQ deleted")
//    }
//
//    // -------------------- Helpers --------------------
//    fun decodeBase64ToBitmap(base64Str: String): Bitmap {
//        val bytes = Base64.decode(base64Str, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }
//}


//    fun decodeBase64ToBitmap(base64Str: String): Bitmap {
//        val bytes = Base64.decode(base64Str, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }
//
//    // -------------------- User --------------------
//    suspend fun writeUser(user: User, profileBitmap: Bitmap?) {
//        val (uid, _) = getVerifiedUser()
//        var profileUrl: String? = null
//
//        profileBitmap?.let { bitmap ->
//            val imageRef = storage.reference.child("profileImages/$uid.jpg")
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
//            val data = baos.toByteArray()
//            imageRef.putBytes(data).await()
//            profileUrl = imageRef.downloadUrl.await().toString()
//        }
//
//        val userWithUrl = user.copy(profileImageUrl = profileUrl)
//        db.collection("users").document(uid).set(userWithUrl).await()
//        Log.d("FirestoreHelper", "User written with UID: $uid")
//    }
//
//    suspend fun readUser(): User? {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users").document(uid).get().await()
//        Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
//        return snapshot.toObject(User::class.java)
//    }
//
//    suspend fun deleteUser() {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users").document(uid).delete().await()
//        Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
//    }
//
//    // -------------------- Health Info --------------------
//    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//        val (uid, _) = getVerifiedUser() // get authenticated UID
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
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .collection("HealthInfo")
//            .document("details")
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "Health information read for UID: $uid")
//        return snapshot.toObject(HealthInformation::class.java)
//    }
//
//    suspend fun deleteHealthInfo() {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("HealthInfo")
//            .document("details")
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "Health info deleted for UID: $uid")
//    }
//
//    // -------------------- Emergency Contacts --------------------
//    suspend fun writeEmergencyContact(contact: EmergencyContact) {
//        val (uid, _) = getVerifiedUser()
//        val docRef = db.collection("users")
//            .document(uid)
//            .collection("EmergencyContacts")
//            .document()
//        val contactWithId = contact.copy(contactId = docRef.id)
//        docRef.set(contactWithId).await()
//        Log.d("FirestoreHelper", "Emergency contact written with ID: ${docRef.id}")
//    }
//
//    suspend fun readEmergencyContact(): List<EmergencyContact> {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .collection("EmergencyContacts")
//            .get()
//            .await()
//        Log.d("FirestoreHelper", "Emergency contacts read for UID: $uid")
//        return snapshot.toObjects(EmergencyContact::class.java)
//    }
//
//
//    suspend fun deleteEmergencyContact(contactId: String) {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("EmergencyContacts")
//            .document(contactId)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "Emergency contact deleted with ID: $contactId")
//    }
//
//    // -------------------- Chats --------------------
//    suspend fun writeChat(chat: Chats) {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("Chats")
//            .document(chat.chatId)
//            .set(chat)
//            .await()
//        Log.d("FirestoreHelper", "Chat written with ID: ${chat.chatId}")
//    }
//
//    suspend fun readChats(): List<Chats> {
//        val (uid, _) = getVerifiedUser()
//        val snapshot = db.collection("users")
//            .document(uid)
//            .collection("Chats")
//            .get()
//            .await()
//        return snapshot.toObjects(Chats::class.java)
//    }
//
//    suspend fun deleteChat(chatId: String) {
//        val (uid, _) = getVerifiedUser()
//        db.collection("users")
//            .document(uid)
//            .collection("Chats")
//            .document(chatId)
//            .delete()
//            .await()
//        Log.d("FirestoreHelper", "Chat deleted with ID: $chatId")
//    }
//
//    // -------------------- Articles --------------------
//    suspend fun writeArticle(article: Articles) {
//        db.collection("articles").add(article).await()
//        Log.d("FirestoreHelper", "Article written")
//    }
//
//    suspend fun readArticles(): List<Articles> {
//        val snapshot = db.collection("articles").get().await()
//        return snapshot.toObjects(Articles::class.java)
//    }
//
//    suspend fun deleteArticle(articleId: String) {
//        db.collection("articles").document(articleId).delete().await()
//        Log.d("FirestoreHelper", "Article deleted with ID: $articleId")
//    }
//
//    // -------------------- FAQs --------------------
//    suspend fun writeFAQ(faq: FAQs) {
//        db.collection("faqs").add(faq).await()
//        Log.d("FirestoreHelper", "FAQ written")
//    }
//
//    suspend fun readFAQs(): List<FAQs> {
//        val snapshot = db.collection("faqs").get().await()
//        return snapshot.toObjects(FAQs::class.java)
//    }
//
//    suspend fun deleteFAQ(faqId: String) {
//        db.collection("faqs").document(faqId).delete().await()
//        Log.d("FirestoreHelper", "FAQ deleted with ID: $faqId")
//    }
//}




        // -------------------- Utility --------------------
        fun decodeBase64ToBitmap(base64Str: String): Bitmap {
            val bytes = Base64.decode(base64Str, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }



        // -------------------- User --------------------
        suspend fun writeUser(user: User, profileBitmap: Bitmap?) {
            val (uid, _) = getVerifiedUser()
            var profileUrl: String? = null

            profileBitmap?.let { bitmap ->
                val imageRef = storage.reference.child("profileImages/$uid.jpg")
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                val data = baos.toByteArray()
                imageRef.putBytes(data).await()
                profileUrl = imageRef.downloadUrl.await().toString()
            }

            val userWithUrl = user.copy(profileImageUrl = profileUrl)
            db.collection("users").document(uid).set(userWithUrl).await()
            Log.d("FirestoreHelper", "User written with UID: $uid")
        }

        suspend fun readUser(): User? {
            val (uid, _) = getVerifiedUser()
            val snapshot = db.collection("users").document(uid).get().await()
            Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
            return snapshot.toObject(User::class.java)
        }

        suspend fun deleteUser() {
            val (uid, _) = getVerifiedUser()
            db.collection("users").document(uid).delete().await()
            Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
        }

        // -------------------- Health Info --------------------
//        suspend fun writeHealthInformation(healthInfo: HealthInformation) {
//            val (uid, _) = getVerifiedUser()
//            db.collection("users")
//                .document(uid)
//                .collection("HealthInformation")
//                .document("primary")
//                .set(healthInfo)
//                .await()
//            Log.d("FirestoreHelper", "HealthInformation saved for UID: $uid")
//        }

    suspend fun writeHealthInformation(healthInfo: HealthInformation) {
        val (uid, _) = getVerifiedUser()

        // Ensure this uses actual UI values
        db.collection("users")
            .document(uid)
            .collection("HealthInformation")
            .document("primary")
            .set(healthInfo)
            .await()
        Log.d("FirestoreHelper", "HealthInformation saved for UID: $uid")
    }


    suspend fun readHealthInformation(): HealthInformation? {
            val (uid, _) = getVerifiedUser()
            val snapshot = db.collection("users")
                .document(uid)
                .collection("HealthInformation")
                .document("primary")
                .get()
                .await()
            Log.d("FirestoreHelper", "HealthInformation read for UID: $uid")
            return snapshot.toObject(HealthInformation::class.java)
        }

        suspend fun deleteHealthInformation() {
            val (uid, _) = getVerifiedUser()
            db.collection("users")
                .document(uid)
                .collection("HealthInformation")
                .document("primary")
                .delete()
                .await()
            Log.d("FirestoreHelper", "HealthInformation deleted for UID: $uid")
        }

        // -------------------- Emergency Contacts --------------------
//        suspend fun writeEmergencyContact(contact: EmergencyContact) {
//            val (uid, _) = getVerifiedUser()
//            val docRef = db.collection("users")
//                .document(uid)
//                .collection("EmergencyContacts")
//                .document() // Firebase generates ID
//            val contactWithId = contact.copy(contactId = docRef.id)
//            docRef.set(contactWithId).await()
//            Log.d("FirestoreHelper", "EmergencyContact written with ID: ${docRef.id}")
//        }


    suspend fun writeEmergencyContact(contact: EmergencyContact) {
        val (uid, _) = getVerifiedUser()

        // Use a fixed document ID if only 1 emergency contact per user
        val docRef = db.collection("users")
            .document(uid)
            .collection("EmergencyContacts")
            .document("primary")

        val contactWithId = contact.copy(contactId = docRef.id)
        docRef.set(contactWithId).await()
        Log.d("FirestoreHelper", "EmergencyContact written with ID: ${docRef.id}")
    }

    suspend fun readEmergencyContact(): EmergencyContact? {
        val (uid, _) = getVerifiedUser()
        val snapshot = db.collection("users")
            .document(uid)
            .collection("EmergencyContacts")
            .limit(1)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(EmergencyContact::class.java)
    }


        suspend fun deleteEmergencyContact(contactId: String) {
            val (uid, _) = getVerifiedUser()
            db.collection("users")
                .document(uid)
                .collection("EmergencyContacts")
                .document(contactId)
                .delete()
                .await()
            Log.d("FirestoreHelper", "EmergencyContact deleted with ID: $contactId")
        }

        // -------------------- Chats --------------------
        suspend fun writeChat(chat: Chats) {
            val (uid, _) = getVerifiedUser()
            db.collection("users")
                .document(uid)
                .collection("Chats")
                .document(chat.chatId)
                .set(chat)
                .await()
            Log.d("FirestoreHelper", "Chat written with ID: ${chat.chatId}")
        }

        suspend fun readChats(): List<Chats> {
            val (uid, _) = getVerifiedUser()
            val snapshot = db.collection("users")
                .document(uid)
                .collection("Chats")
                .get()
                .await()
            return snapshot.toObjects(Chats::class.java)
        }

        suspend fun deleteChat(chatId: String) {
            val (uid, _) = getVerifiedUser()
            db.collection("users")
                .document(uid)
                .collection("Chats")
                .document(chatId)
                .delete()
                .await()
            Log.d("FirestoreHelper", "Chat deleted with ID: $chatId")
        }

        // -------------------- Articles --------------------
        suspend fun writeArticle(article: Articles) {
            db.collection("articles").add(article).await()
            Log.d("FirestoreHelper", "Article written")
        }

        suspend fun readArticles(): List<Articles> {
            val snapshot = db.collection("articles").get().await()
            return snapshot.toObjects(Articles::class.java)
        }

        suspend fun deleteArticle(articleId: String) {
            db.collection("articles").document(articleId).delete().await()
            Log.d("FirestoreHelper", "Article deleted with ID: $articleId")
        }

        // -------------------- FAQs --------------------
        suspend fun writeFAQ(faq: FAQs) {
            db.collection("faqs").add(faq).await()
            Log.d("FirestoreHelper", "FAQ written")
        }

        suspend fun readFAQs(): List<FAQs> {
            val snapshot = db.collection("faqs").get().await()
            return snapshot.toObjects(FAQs::class.java)
        }

        suspend fun deleteFAQ(faqId: String) {
            db.collection("faqs").document(faqId).delete().await()
            Log.d("FirestoreHelper", "FAQ deleted with ID: $faqId")
        }
    }


