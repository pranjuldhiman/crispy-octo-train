package com.android.roundup.apprepository

import com.android.roundup.models.MODSearchResponse
import com.android.roundup.networkhelper.RetrofitCallbackHandler
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.service.RoundUpRepositoryRetrofit
import com.android.roundup.service.RoundUpRepositoryService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoundUpRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val roundUpService: RoundUpRepositoryService = RoundUpRepositoryRetrofit().prepareService()
): RoundUpRepository {
    override suspend fun getSearchResults(searchTag: String): ServiceResult<MODSearchResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                roundUpService.getSearchResults(searchTag)
            }
        }
    }


}
