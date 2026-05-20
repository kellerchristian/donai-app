package com.donai.app.domain.model.notification

enum class NotificationType {
    PUSH,
    SYSTEM,
    EMAIL
}

enum class DeliveryStatus {
    SENT,
    FAILED,
    READ
}