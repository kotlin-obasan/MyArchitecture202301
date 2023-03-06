package com.example.myblueprint2302.ui.main.data

sealed class ApiStatus<out T> {
    // APIコールが実行中である
    object Proceeding : ApiStatus<Nothing>()
    // APIコールが成功した
    data class Success<out T>(val value: T) : ApiStatus<T>()
    // APIコールが失敗した
    data class Error(val errorCode: Int) : ApiStatus<Nothing>()
}
