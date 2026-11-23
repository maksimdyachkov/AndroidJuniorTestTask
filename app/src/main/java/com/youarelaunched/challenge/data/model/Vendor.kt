package com.youarelaunched.challenge.data.model

data class Vendor(
    val id: Int,
    val companyName: String,
    val areaServed: String,
    val coverPhotoUrl: String,
    val isFavorite: Boolean,
    val categories: List<Category>,
    val tags: List<String>
)

data class Category(
    val id: Int,
    val name: String,
    val iconUrl: String
)
