package com.android.roundup.models

import com.android.roundup.dashboard.model.Data

data class NotificationResponse(
    val list: List<Notification>,
    val status: Int = 0
)