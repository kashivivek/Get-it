package com.getit.getit.yes.data

import androidx.annotation.DrawableRes
import com.getit.getit.yes.R

data class ServiceType(val id: String, val label: String, @DrawableRes val image: Int)

data class Category(val id: String, val label: String, val types: List<ServiceType>)

/** Must stay in sync with the `android:tag` values on the category cards and with firestore.rules. */
object Categories {
    val all = listOf(
        Category(
            "household", "House Hold Works", listOf(
                ServiceType("electrical", "Electrical", R.drawable.electrician),
                ServiceType("plumbing", "Plumbing", R.drawable.plumber),
                ServiceType("carpenter", "Carpenters", R.drawable.carpenter),
                ServiceType("cleaning", "Cleaning", R.drawable.cleaning),
                ServiceType("repair", "Repairing works", R.drawable.repair),
                ServiceType("getit", "Get-it @ Home", R.drawable.athome),
            )
        ),
        Category(
            "lifestyle", "Life Style", listOf(
                ServiceType("event", "Event Planner", R.drawable.event),
                ServiceType("webdesigner", "Web Designer", R.drawable.webdesigner),
                ServiceType("photographer", "Photographers", R.drawable.photogrpaher),
                ServiceType("packers", "Packers and movers", R.drawable.packersandmovers),
            )
        ),
        Category(
            "groceries", "Groceries", listOf(
                ServiceType("kirana", "Kirana", R.drawable.groceries),
                ServiceType("hardware", "Hardware", R.drawable.hardware),
                ServiceType("pharmacy", "Pharmacy", R.drawable.medical),
            )
        ),
        Category(
            "food", "Food", listOf(
                ServiceType("biryani", "Biryani", R.drawable.biryani),
                ServiceType("restaurants", "Restaurants", R.drawable.vegeterian),
                ServiceType("bakers", "Bakers, Pizzas n Sweets", R.drawable.pizza),
                ServiceType("chaat", "Chaat", R.drawable.chat),
            )
        ),
    )

    fun category(id: String): Category? = all.firstOrNull { it.id == id }

    fun type(categoryId: String, typeId: String): ServiceType? =
        category(categoryId)?.types?.firstOrNull { it.id == typeId }
}
