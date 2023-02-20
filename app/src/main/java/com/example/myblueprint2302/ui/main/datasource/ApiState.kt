package com.example.myblueprint2302.ui.main.datasource

sealed class ApiState<out T> {
    // APIコールが実行中である
    object Proceeding : ApiState<Nothing>()
    // APIコールが成功した
    data class Success<out T>(val value: T) : ApiState<T>()
    // APIコールが失敗した
    data class Error(val error: Throwable) : ApiState<Nothing>()
}