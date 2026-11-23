package com.youarelaunched.challenge.data

import com.youarelaunched.challenge.data.model.Category
import com.youarelaunched.challenge.data.model.Vendor
import com.youarelaunched.challenge.network.api.ApiVendors
import com.youarelaunched.challenge.network.models.NetworkCategory
import com.youarelaunched.challenge.network.models.NetworkVendor

class VendorsRepository(private val apiVendors: ApiVendors) {

    suspend fun getVendors(): List<Vendor> = apiVendors.getVendors().map { it.toVendor() }
}

private fun NetworkVendor.toVendor() = Vendor(
    id = id,
    companyName = companyName,
    areaServed = areaServed,
    coverPhotoUrl = coverPhoto.mediaUrl,
    isFavorite = favorite,
    categories = categories.orEmpty().map { it.toCategory() },
    tags = tags.orEmpty().map { it.name }
)

private fun NetworkCategory.toCategory() = Category(
    id = id,
    name = name,
    iconUrl = image.mediaUrl
)
