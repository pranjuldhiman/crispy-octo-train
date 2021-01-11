package com.rs.roundupclasses.models

import com.rs.roundupclasses.scan.model.DataModel

data class ResponseModel(
    val request_id: String? = "",
    val text: String? = "",
    val error: String? = "",
    val latex: String? = "",
    val data: List<DataModel>? = null
   // @Json(name="data")
   // var data: MutableList<DashBoardData>? = null

)
