package com.youarelaunched.challenge.ui.vendors

import com.youarelaunched.challenge.MainDispatcherRule
import com.youarelaunched.challenge.data.DefaultVendorsRepository
import com.youarelaunched.challenge.data.model.Category
import com.youarelaunched.challenge.data.model.Vendor
import com.youarelaunched.challenge.network.api.ApiVendors
import com.youarelaunched.challenge.network.models.NetworkCategory
import com.youarelaunched.challenge.network.models.NetworkMediaAttachment
import com.youarelaunched.challenge.network.models.NetworkTag
import com.youarelaunched.challenge.network.models.NetworkVendor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class VendorsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `maps loaded vendors to success state`() {
        val viewModel = VendorsViewModel(DefaultVendorsRepository(FakeApiVendors(listOf(networkVendor))))

        val expected = Vendor(
            id = 1,
            companyName = "North St. Butchery",
            areaServed = "City of London",
            coverPhotoUrl = "cover.png",
            isFavorite = true,
            categories = listOf(Category(name = "Butchery", iconUrl = "icon.svg")),
            tags = listOf("Best meat in town")
        )
        assertEquals(VendorsUiState.Success(listOf(expected)), viewModel.uiState.value)
    }

    @Test
    fun `emits empty state when there are no vendors`() {
        val viewModel = VendorsViewModel(DefaultVendorsRepository(FakeApiVendors(emptyList())))

        assertEquals(VendorsUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `emits error state when loading fails`() {
        val api = FakeApiVendors(listOf(networkVendor), shouldFail = true)
        val viewModel = VendorsViewModel(DefaultVendorsRepository(api))

        assertEquals(VendorsUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `retry after error loads vendors`() {
        val api = FakeApiVendors(listOf(networkVendor), shouldFail = true)
        val viewModel = VendorsViewModel(DefaultVendorsRepository(api))

        api.shouldFail = false
        viewModel.loadVendors()

        assertEquals(1, (viewModel.uiState.value as VendorsUiState.Success).vendors.size)
    }

    @Test
    fun `favorite click toggles the vendor favorite state`() {
        val viewModel = VendorsViewModel(DefaultVendorsRepository(FakeApiVendors(listOf(networkVendor))))

        viewModel.onFavoriteClick(vendorId = 1)

        val vendor = (viewModel.uiState.value as VendorsUiState.Success).vendors.single()
        assertFalse(vendor.isFavorite)
    }
}

private class FakeApiVendors(
    private val vendors: List<NetworkVendor>,
    var shouldFail: Boolean = false
) : ApiVendors {

    override suspend fun getVendors(): List<NetworkVendor> {
        if (shouldFail) error("Failed to load vendors")
        return vendors
    }
}

private val networkVendor = NetworkVendor(
    areaServed = "City of London",
    categories = listOf(
        NetworkCategory(id = 10, image = media("icon.svg"), name = "Butchery")
    ),
    companyName = "North St. Butchery",
    coverPhoto = media("cover.png"),
    profilePhoto = null,
    distance = null,
    favorite = true,
    id = 1,
    shopType = "Butchery",
    tags = listOf(NetworkTag(id = 1, name = "Best meat in town", purpose = "shop")),
    chatId = null,
    type = null
)

private fun media(url: String) = NetworkMediaAttachment(id = 1, mediaType = "image", mediaUrl = url)
