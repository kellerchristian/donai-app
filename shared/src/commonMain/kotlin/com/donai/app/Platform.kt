package com.donai.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform