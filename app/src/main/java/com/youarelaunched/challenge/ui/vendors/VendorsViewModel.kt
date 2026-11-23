package com.youarelaunched.challenge.ui.vendors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.youarelaunched.challenge.VendorsApplication
import com.youarelaunched.challenge.data.VendorsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VendorsViewModel(private val repository: VendorsRepository) : ViewModel() {

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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as VendorsApplication
                VendorsViewModel(application.vendorsRepository)
            }
        }
    }
}
