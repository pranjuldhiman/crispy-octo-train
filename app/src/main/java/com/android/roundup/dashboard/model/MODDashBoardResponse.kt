package com.android.roundup.dashboard.model

import com.android.roundup.models.Banners

data class MODDashBoardResponse(
    val `data`: List<Data>,
    val status: Int = 0,
    val `banners`: List<Banners>

    )
