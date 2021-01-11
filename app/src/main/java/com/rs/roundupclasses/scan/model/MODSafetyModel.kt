package com.rs.roundupclasses.scan.model

data class MODSafetyModel(
    val isPaid: Int,
    val status: Int? = 0,
    val name: String? = "",
    val email: String? = "",
    val userid: String? = ""
)
