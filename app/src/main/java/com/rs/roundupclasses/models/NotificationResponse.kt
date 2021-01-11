package com.rs.roundupclasses.models

data class NotificationResponse(
    val list: List<Notification>,
    val status: Int = 0
)