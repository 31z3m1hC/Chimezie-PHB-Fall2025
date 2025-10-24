import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.unh.personal_health_buddy.MainActivity
import com.unh.personal_health_buddy.database.Article
import com.unh.personal_health_buddy.database.Chat
import com.unh.personal_health_buddy.database.Contact
import com.unh.personal_health_buddy.database.FAQ
import com.unh.personal_health_buddy.database.HealthInfo
import com.unh.personal_health_buddy.database.User
import kotlinx.coroutines.tasks.await

@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

suspend fun addUserToFirestore(user: User) {
    val userRef = db.collection("users").document(user.userId)
    try {
        userRef.set(user).await()
        Log.d("Firestore", "User added: ${user.userId}")

        val healthInfo = HealthInfo(
            bodyMassIndex = listOf("170cm", "70kg", "24.2"),
            bloodInformation = "A+"
        )

        val chat = Chat(
            avatarUrl = user.avatar,
            chats = listOf("Hello", "How are you?")
        )

        val contact = Contact(
            name = "Okechukwu Onwuegbuchulem",
            phone = listOf("+9377974614"),
            relationship = "Brother"
        )

        userRef.collection("HealthInfo").add(healthInfo).await()
        userRef.collection("Chats").add(chat).await()
        userRef.collection("Contacts").add(contact).await()

        Log.d("Firestore", "Subcollections added for user ${user.userId}")
    } catch (e: Exception) {
        Log.e("Firestore", "Error adding user and subcollections", e)
    }
}

suspend fun addArticle(article: Article) {
    try {
        db.collection("Articles").add(article).await()
        Log.d("Firestore", "Article added: ${article.name}")
    } catch (e: Exception) {
        Log.e("Firestore", "Error adding article", e)
    }
}

suspend fun addFAQ(faq: FAQ) {
    try {
        db.collection("FAQ").add(faq).await()
        Log.d("Firestore", "FAQ added: ${faq.name}")
    } catch (e: Exception) {
        Log.e("Firestore", "Error adding FAQ", e)
    }
}

@Composable
fun FetchFirestoreData() {
    LaunchedEffect(Unit) {

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    Log.d("MainActivity", "User: ${doc.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error fetching users", e)
            }

        db.collection("Articles")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    Log.d("MainActivity", "Article: ${doc.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error fetching articles", e)
            }

        db.collection("FAQ")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    Log.d("MainActivity", "FAQ: ${doc.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error fetching FAQs", e)
            }

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (userDoc in result) {
                    val userId = userDoc.id

                    db.collection("users").document(userId).collection("HealthInfo")
                        .get()
                        .addOnSuccessListener { subResult ->
                            for (doc in subResult) {
                                Log.d("MainActivity", "HealthInfo for $userId: ${doc.data}")
                            }
                        }

                    db.collection("users").document(userId).collection("Chats")
                        .get()
                        .addOnSuccessListener { subResult ->
                            for (doc in subResult) {
                                Log.d("MainActivity", "Chat for $userId: ${doc.data}")
                            }
                        }

                    db.collection("users").document(userId).collection("Contacts")
                        .get()
                        .addOnSuccessListener { subResult ->
                            for (doc in subResult) {
                                Log.d("MainActivity", "Contact for $userId: ${doc.data}")
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error fetching subcollections", e)
            }
    }
}
