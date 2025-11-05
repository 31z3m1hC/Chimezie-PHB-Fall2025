package com.unh.personal_health_buddy.Authentication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unh.personal_health_buddy.database.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

object FirestoreHelper {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private fun getVerifiedUser(): Pair<String, String> {
        val user = FirebaseAuth.getInstance().currentUser
        requireNotNull(user?.uid) { "No authenticated user UID found." }
        requireNotNull(user.email) { "Authenticated user has no email." }
        return user.uid to user.email!!
    }

    // -------------------- Users --------------------
    suspend fun writeUser(user: User, profileImage: Bitmap? = null) {
        val (uid, email) = getVerifiedUser()
        require(user.email == email) { "Email mismatch: form email does not match authenticated email." }

        val userMap = hashMapOf(
            "firstname" to user.firstname,
            "lastname" to user.lastname,
            "dateOfBirth" to user.dateOfBirth,
            "homeAddress" to user.homeAddress,
            "gender" to user.gender.name,
            "email" to user.email,
            "medication" to user.medication
        )

        profileImage?.let {
            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val imageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
            userMap["profileImage"] = imageBase64
        }

        db.collection("users")
            .document(uid)
            .set(userMap)
            .await()

        Log.d("FirestoreHelper", "User written to Firestore with UID: $uid")
    }

    suspend fun readUser(): User? {
        val (uid, _) = getVerifiedUser()
        val snapshot = db.collection("users")
            .document(uid)
            .get()
            .await()
        Log.d("FirestoreHelper", "User read from Firestore with UID: $uid")
        return snapshot.toObject(User::class.java)
    }

    suspend fun deleteUser() {
        val (uid, _) = getVerifiedUser()
        db.collection("users")
            .document(uid)
            .delete()
            .await()
        Log.d("FirestoreHelper", "User deleted from Firestore with UID: $uid")
    }

    // -------------------- Health Info --------------------
    suspend fun deleteHealthInfo() {
        val (uid, _) = getVerifiedUser()
        db.collection("users")
            .document(uid)
            .collection("HealthInfo")
            .document("details")
            .delete()
            .await()
        Log.d("FirestoreHelper", "Health info deleted for UID: $uid")
    }

    // -------------------- Emergency Contact --------------------
    suspend fun writeEmergencyContact(contact: EmergencyContact) {
        val (uid, _) = getVerifiedUser()
        db.collection("users")
            .document(uid)
            .collection("EmergencyContact")
            .document(contact.contactId)
            .set(contact)
            .await()
        Log.d("FirestoreHelper", "Emergency contact written for UID: $uid")
    }

    suspend fun readEmergencyContact(): List<EmergencyContact> {
        val (uid, _) = getVerifiedUser()
        val snapshot = db.collection("users")
            .document(uid)
            .collection("EmergencyContact")
            .get()
            .await()
        Log.d("FirestoreHelper", "Emergency contacts read for UID: $uid")
        return snapshot.toObjects(EmergencyContact::class.java)
    }

    suspend fun deleteEmergencyContact(contactId: String) {
        val (uid, _) = getVerifiedUser()
        db.collection("users")
            .document(uid)
            .collection("EmergencyContact")
            .document(contactId)
            .delete()
            .await()
        Log.d("FirestoreHelper", "🗑️ Emergency contact deleted for UID: $uid")
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
        Log.d("FirestoreHelper", "Chat written for UID: $uid")
    }

    suspend fun readChats(): List<Chats> {
        val (uid, _) = getVerifiedUser()
        val snapshot = db.collection("users")
            .document(uid)
            .collection("Chats")
            .get()
            .await()
        Log.d("FirestoreHelper", "Chats read for UID: $uid")
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
        Log.d("FirestoreHelper", "Chat deleted for UID: $uid")
    }

    // -------------------- Articles --------------------
    suspend fun writeArticle(article: Articles) {
        db.collection("articles")
            .add(article)
            .await()
        Log.d("FirestoreHelper", "Article written")
    }

    suspend fun readArticles(): List<Articles> {
        val snapshot = db.collection("articles")
            .get()
            .await()
        Log.d("FirestoreHelper", "Articles read")
        return snapshot.toObjects(Articles::class.java)
    }

    suspend fun deleteArticle(articleId: String) {
        db.collection("articles")
            .document(articleId)
            .delete()
            .await()
        Log.d("FirestoreHelper", "Article deleted")
    }

    // -------------------- FAQs --------------------
    suspend fun writeFAQ(faq: FAQs) {
        db.collection("faqs")
            .add(faq)
            .await()
        Log.d("FirestoreHelper", "FAQ written")
    }

    suspend fun readFAQs(): List<FAQs> {
        val snapshot = db.collection("faqs")
            .get()
            .await()
        Log.d("FirestoreHelper", "FAQs read")
        return snapshot.toObjects(FAQs::class.java)
    }

    suspend fun deleteFAQ(faqId: String) {
        db.collection("faqs")
            .document(faqId)
            .delete()
            .await()
        Log.d("FirestoreHelper", "FAQ deleted")
    }

    // -------------------- Helpers --------------------
    fun decodeBase64ToBitmap(base64Str: String): Bitmap {
        val bytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
