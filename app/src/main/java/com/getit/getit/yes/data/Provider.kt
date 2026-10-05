package com.getit.getit.yes.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class Provider(
    val id: String,
    val name: String,
    val phone: String,
    val category: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String = "",
    val address: String = "",
    val description: String = "",
) : Parcelable {

    fun toFirestore(): Map<String, Any> = mapOf(
        "name" to name,
        "phone" to phone,
        "category" to category,
        "type" to type,
        "location" to GeoPoint(latitude, longitude),
        "imageUrl" to imageUrl,
        "address" to address,
        "description" to description,
    )

    companion object {
        fun from(doc: DocumentSnapshot): Provider? {
            val location = doc.getGeoPoint("location") ?: return null
            return Provider(
                id = doc.id,
                name = doc.getString("name").orEmpty(),
                phone = doc.getString("phone").orEmpty(),
                category = doc.getString("category").orEmpty(),
                type = doc.getString("type").orEmpty(),
                latitude = location.latitude,
                longitude = location.longitude,
                imageUrl = doc.getString("imageUrl").orEmpty(),
                address = doc.getString("address").orEmpty(),
                description = doc.getString("description").orEmpty(),
            )
        }
    }
}
