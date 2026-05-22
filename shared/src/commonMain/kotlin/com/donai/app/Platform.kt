package com.donai.app

import io.ktor.client.HttpClient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun platformHttpClient(): HttpClient

