package com.example.myblueprint2302.ui.main.data.repository

import com.example.myblueprint2302.ui.main.data.ApiStatus
import com.example.myblueprint2302.ui.main.data.dto.LoginUser
import com.example.myblueprint2302.ui.main.data.dto.UserInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val loginInterface: LoginInterface
) {

    //loginAPIInterface.login()の返り値はRetrofit2のResponse型
    //↑をapiFlowがラップすることでResult型を出力するFlowを返す
    fun login(loginUser: LoginUser): Flow<ApiStatus<UserInfo>> =
        apiFlow {
            loginInterface.login("login", loginUser)
        }
}