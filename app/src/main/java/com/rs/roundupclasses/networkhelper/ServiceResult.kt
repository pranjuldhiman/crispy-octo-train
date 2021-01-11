package com.rs.roundupclasses.networkhelper

sealed class ServiceResult<out R> {
    data class Success<out T>(val data: T) : ServiceResult<T>()
    data class Error(val exception: String) : ServiceResult<Nothing>()
}
