package com.youarelaunched.challenge

import android.app.Application
import com.youarelaunched.challenge.data.VendorsRepository
import com.youarelaunched.challenge.network.api.NetworkDataProvider
import kotlinx.coroutines.Dispatchers

class VendorsApplication : Application() {

    val vendorsRepository by lazy {
        VendorsRepository(NetworkDataProvider(Dispatchers.IO, this))
    }
}
