package com.example.core.domain.models

sealed class Resources<T>(data: T? = null, errorMessage: String? = null) {
    data class Success<T>(var data: T?) : Resources<T>(data = data)
    data class Error<T>(var data: T? = null, var message: String) :
        Resources<T>(data = data, errorMessage = message)
}