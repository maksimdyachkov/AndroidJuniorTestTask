package com.youarelaunched.challenge.ui.vendors

import com.youarelaunched.challenge.data.model.Vendor

sealed interface VendorsUiState {
    data object Loading : VendorsUiState
    data object Empty : VendorsUiState
    data object Error : VendorsUiState
    data class Success(val vendors: List<Vendor>) : VendorsUiState
}
