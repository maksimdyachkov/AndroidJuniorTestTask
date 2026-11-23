package com.youarelaunched.challenge.ui.vendors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youarelaunched.challenge.data.VendorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorsViewModel @Inject constructor(
    private val repository: VendorsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VendorsUiState>(VendorsUiState.Loading)
    val uiState: StateFlow<VendorsUiState> = _uiState.asStateFlow()

    init {
        loadVendors()
    }

    fun loadVendors() {
        viewModelScope.launch {
            _uiState.value = VendorsUiState.Loading
            _uiState.value = try {
                val vendors = repository.getVendors()
                if (vendors.isEmpty()) VendorsUiState.Empty else VendorsUiState.Success(vendors)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                VendorsUiState.Error
            }
        }
    }

    fun onFavoriteClick(vendorId: Int) {
        _uiState.update { state ->
            if (state !is VendorsUiState.Success) return@update state
            state.copy(
                vendors = state.vendors.map { vendor ->
                    if (vendor.id == vendorId) vendor.copy(isFavorite = !vendor.isFavorite) else vendor
                }
            )
        }
    }
}
