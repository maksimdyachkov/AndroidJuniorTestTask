package com.youarelaunched.challenge.data

import com.youarelaunched.challenge.data.model.Category
import com.youarelaunched.challenge.data.model.Vendor
import com.youarelaunched.challenge.network.api.ApiVendors
import com.youarelaunched.challenge.network.models.NetworkCategory
import com.youarelaunched.challenge.network.models.NetworkVendor
import javax.inject.Inject

interface VendorsRepository {
    suspend fun getVendors(): List<Vendor>
}

class DefaultVendorsRepository @Inject constructor(
    private val apiVendors: ApiVendors
) : VendorsRepository {

    override suspend fun getVendors(): List<Vendor> = apiVendors.getVendors().map { it.toVendor() }
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
    name = name,
    iconUrl = image.mediaUrl
)
