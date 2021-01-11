package com.rs.roundupclasses.dashboard.model

import java.io.Serializable

data class Subcategory(
    val category_id: String?,
    val created_datetime: String?,
    val icons: String?,
    val id: String?,
    val is_active: String?,
    val name: String?,
    val thumbnail_banner: String?,
    val thumbnail: String?
):Serializable
