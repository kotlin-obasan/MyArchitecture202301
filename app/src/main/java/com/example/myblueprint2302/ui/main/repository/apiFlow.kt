package com.example.myblueprint2302.ui.main.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

inline fun <reified T : Any> apiFlow(crossinline call: suspend () -> Response<T>): Flow<ApiStatus<T>> =
    flow<ApiStatus<T>> {
        val response = call()
        if (response.isSuccessful) emit(ApiStatus.Success(value = response.body()!!))
        else emit(ApiStatus.Error(response.code()))
    }.onStart {
        emit(ApiStatus.Proceeding)
    }.flowOn(Dispatchers.IO)
