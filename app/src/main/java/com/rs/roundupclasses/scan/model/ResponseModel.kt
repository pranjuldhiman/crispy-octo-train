package com.rs.roundupclasses.scan.model

data class ResponseModel(
    val request_id: String? = "",
    val text: String? = "",
    val error: String? = "",
    val latex: String? = "",
    val wolfram: String? = "",
    val data: List<DataModel>? = null
)
