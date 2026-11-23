package com.youarelaunched.challenge.di

import android.content.Context
import com.youarelaunched.challenge.data.DefaultVendorsRepository
import com.youarelaunched.challenge.data.VendorsRepository
import com.youarelaunched.challenge.network.api.ApiVendors
import com.youarelaunched.challenge.network.api.NetworkDataProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsVendorsRepository(repository: DefaultVendorsRepository): VendorsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesApiVendors(@ApplicationContext context: Context): ApiVendors =
        NetworkDataProvider(workDispatcher = Dispatchers.IO, appContext = context)
}
