package com.jithin.groceryapp.di


/*
 * --------------------------------------------------------------------------
 * File: RepositoryModule.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.jithin.groceryapp.domain.repository.AuthRepository
import com.jithin.groceryapp.data.repository.AuthRepositoryImpl
import com.jithin.groceryapp.domain.repository.CustomerDataRepository
import com.jithin.groceryapp.data.repository.CustomerDataRepositoryImpl
import com.jithin.groceryapp.domain.repository.ProductRepository
import com.jithin.groceryapp.data.repository.ProductRepositoryImpl
import com.jithin.groceryapp.domain.repository.StorageRepository
import com.jithin.groceryapp.data.repository.StorageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Singleton
    @Binds
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun bindsStorageRepositoryRepository(impl: StorageRepositoryImpl): StorageRepository

    @Singleton
    @Binds
    abstract fun bindsCustomerDataRepository(impl: CustomerDataRepositoryImpl): CustomerDataRepository

}