package com.example.myblueprint2302.ui.main.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import retrofit2.Response

inline fun <reified T : Any> apiFlow(crossinline call: suspend () -> Response<T>): Flow<ApiState<T>> =
    flow<ApiState<T>> {
        val response = call()
        if (response.isSuccessful) emit(ApiState.Success(value = response.body()!!))
        else throw HttpException(response)
    }.catch {
        emit(ApiState.Error(it))
    }.onStart {
        emit(ApiState.Proceeding)
    }.flowOn(Dispatchers.IO)
