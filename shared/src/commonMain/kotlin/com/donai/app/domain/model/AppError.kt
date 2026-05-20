package com.donai.app.domain.model

sealed class AppError {
    object Network : AppError()
    object Unauthorized : AppError()
    object Unknown : AppError()
}