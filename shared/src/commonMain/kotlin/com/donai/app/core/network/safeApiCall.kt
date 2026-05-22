package com.donai.app.core.network

suspend inline fun <T> safeApiCall(
    crossinline call: suspend () -> T
): NetworkResult<T> {
    return try {
        NetworkResult.Success(call())
    } catch (e: Exception) {
        NetworkResult.Error(e.message ?: "Unknown error")
    }
}