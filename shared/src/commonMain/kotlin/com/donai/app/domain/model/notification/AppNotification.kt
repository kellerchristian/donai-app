package com.donai.app.domain.model.notification

import kotlinx.datetime.Instant

data class AppNotification(
    val id: String,
    val title: String,
    val body: String,
    val type: NotificationType,
    val relatedRequestId: String?,
    val createdAt: Instant,
    val read: Boolean
)