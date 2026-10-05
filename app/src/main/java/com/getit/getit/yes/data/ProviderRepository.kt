package com.getit.getit.yes.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ProviderRepository {
    private val providers get() = FirebaseFirestore.getInstance().collection("providers")

    suspend fun list(category: String, type: String): List<Provider> =
        providers
            .whereEqualTo("category", category)
            .whereEqualTo("type", type)
            .get()
            .await()
            .documents
            .mapNotNull(Provider::from)

    suspend fun add(provider: Provider) {
        val data = provider.toFirestore() + mapOf(
            "createdAt" to FieldValue.serverTimestamp(),
            "createdBy" to FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
        )
        providers.add(data).await()
    }

    suspend fun report(providerId: String, reason: String) {
        FirebaseFirestore.getInstance().collection("reports").add(
            mapOf(
                "providerId" to providerId,
                "reason" to reason,
                "reportedBy" to FirebaseAuth.getInstance().currentUser?.uid.orEmpty(),
                "createdAt" to FieldValue.serverTimestamp(),
            )
        ).await()
    }
}
