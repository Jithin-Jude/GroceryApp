package com.jithin.groceryapp.di


/*
 * --------------------------------------------------------------------------
 * File: RepositoryModule.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.jithin.groceryapp.network.AuthRepository
import com.jithin.groceryapp.network.AuthRepositoryImpl
import com.jithin.groceryapp.network.ProductRepository
import com.jithin.groceryapp.network.ProductRepositoryImpl
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
    abstract fun bindsFeedRepository(impl: ProductRepositoryImpl): ProductRepository

    @Singleton
    @Binds
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}