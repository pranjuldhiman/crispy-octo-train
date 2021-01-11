package com.rs.roundupclasses.models

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val is_read: String,
    val created_datetime: String
)