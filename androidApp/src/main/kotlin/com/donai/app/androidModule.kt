package com.donai.app

import com.donai.app.core.auth.AuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp // O Android
import org.koin.dsl.module

val androidModule = module {
    // Motor para Ktor (Soluciona el error en sharedModule)
    single { OkHttp.create() }
    //single { HttpClient(OkHttp) }
    // Firebase
    single { FirebaseAuth.getInstance() }

    // Auth Provider
    single<AuthProvider> {
        FirebaseAuthProvider(get())
    }
}