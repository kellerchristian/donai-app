package com.donai.app

import android.os.Build
import com.donai.app.core.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun platformHttpClient(): HttpClient {
    return createHttpClient(CIO)
}