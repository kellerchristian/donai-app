package com.donai.app.core.di

import com.donai.app.core.network.createHttpClient
import com.donai.app.data.repository.AuthRepositoryImpl
import com.donai.app.domain.repository.AuthRepository
import com.donai.app.domain.usecase.LoginUseCase
import com.donai.app.domain.usecase.LogoutUseCase
import com.donai.app.domain.usecase.RegisterAuthUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    // Network
    single { createHttpClient(get()) }

    // Repositories
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

    // Use Cases
    factory { LoginUseCase(get()) }
    factory{RegisterAuthUseCase(get())}
    factory { LogoutUseCase(get()) }
}
