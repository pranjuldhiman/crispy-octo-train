package com.rs.roundupclasses.dashboard.model

data class Videos(
    val id: String?,
    val category_id: String?,
    val topic_id: String?,
    val subject_id: String?,
    val class_id: String?,
    val title: String?,
    val description: String?,
    val thumbnail: String?,
    val mediatype: String?,
    val mediaurl: String?,
    val videourl: String?,
    val pdfurl: String?,
    val medialength: String?,
    val is_active: String?,
    val created_datetime: String?
)