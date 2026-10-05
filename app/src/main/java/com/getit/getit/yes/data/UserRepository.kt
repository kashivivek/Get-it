package com.getit.getit.yes.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

data class UserProfile(val name: String, val place: String, val email: String, val photo: ByteArray?)

object UserRepository {
    private val auth get() = FirebaseAuth.getInstance()
    private val db get() = FirebaseFirestore.getInstance()

    private fun userDoc() = auth.currentUser?.uid?.let { db.collection("users").document(it) }

    suspend fun signUp(email: String, password: String, name: String, place: String) {
        val user = auth.createUserWithEmailAndPassword(email, password).await().user
            ?: error("User creation returned no user")
        db.collection("users").document(user.uid).set(
            mapOf(
                "name" to name,
                "email" to email,
                "place" to place,
                "createdAt" to FieldValue.serverTimestamp(),
            )
        ).await()
    }

    suspend fun profile(): UserProfile? {
        val doc = userDoc()?.get()?.await() ?: return null
        return UserProfile(
            name = doc.getString("name").orEmpty(),
            place = doc.getString("place").orEmpty(),
            email = auth.currentUser?.email.orEmpty(),
            photo = doc.getBlob("photo")?.toBytes(),
        )
    }

    suspend fun updateProfile(name: String, place: String) {
        val doc = userDoc() ?: return
        doc.set(
            mapOf("name" to name, "place" to place, "updatedAt" to FieldValue.serverTimestamp()),
            SetOptions.merge(),
        ).await()
    }

    /** Stored inline in the user doc (kept small by the caller) so no Cloud Storage / Blaze plan is needed. */
    suspend fun updatePhoto(jpeg: ByteArray?) {
        val doc = userDoc() ?: return
        val value: Any = jpeg?.let { Blob.fromBytes(it) } ?: FieldValue.delete()
        doc.set(mapOf("photo" to value, "updatedAt" to FieldValue.serverTimestamp()), SetOptions.merge()).await()
    }

    suspend fun sendPasswordReset() {
        val email = auth.currentUser?.email ?: error("Account has no email")
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun deleteAccount(password: String) {
        val user = auth.currentUser ?: return
        val email = user.email ?: error("Account has no email")
        user.reauthenticate(EmailAuthProvider.getCredential(email, password)).await()
        db.collection("users").document(user.uid).delete().await()
        user.delete().await()
    }
}
