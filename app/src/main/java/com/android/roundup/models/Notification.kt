package com.android.roundup.models

data class Notification(
    val id: String,
    val message: String,
    val is_read: String,
    val created_datetime: String
)