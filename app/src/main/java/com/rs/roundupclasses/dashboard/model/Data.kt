package com.rs.roundupclasses.dashboard.model

data class Data(
    val category: String?,
    val having_subcategory: String?,
    val subcategory: List<Subcategory>?
)
