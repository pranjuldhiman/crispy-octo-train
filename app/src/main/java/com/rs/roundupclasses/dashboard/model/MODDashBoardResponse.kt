package com.rs.roundupclasses.dashboard.model

import com.rs.roundupclasses.models.Banners

data class MODDashBoardResponse(
    val `data`: List<Data>,
    val status: Int = 0,
    val app_id: String = "",
    val app_key: String = "",
    val `banners`: List<Banners>

    )
