package com.android.roundup.apprepository

import com.android.roundup.models.MODSearchResponse
import com.android.roundup.networkhelper.ServiceResult

interface RoundUpRepository {
    suspend fun getSearchResults(searchTag: String): ServiceResult<MODSearchResponse?>
}
