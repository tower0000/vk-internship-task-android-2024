package com.tower0000.vktask2024.ui.util

sealed class ResourceState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResourceState<T>(data)
    class Error<T>(message: String) : ResourceState<T>(message = message)
    class Loading<T> : ResourceState<T>()
    class Unspecified<T> : ResourceState<T>()
}