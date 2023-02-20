package com.example.myblueprint2302.ui.main.datasource

import com.example.myblueprint2302.ui.main.data.LoginUser
import com.example.myblueprint2302.ui.main.data.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginAPIInterface {

    @Headers("Content-Type: application/json")
    @POST
    suspend fun login(
        @Url url:String,
        @Body user: LoginUser
    ): Response<UserInfo>
}