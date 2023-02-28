package com.example.myblueprint2302.ui.main.repository

import com.example.myblueprint2302.ui.main.data.LoginUser
import com.example.myblueprint2302.ui.main.data.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class LoginRepository @Inject constructor(
    private val loginInterface: LoginInterface) {

    //loginAPIInterface.login()の返り値はRetrofit2のResponse型
    //↑をapiFlowがラップすることでResult型を出力するFlowを返す
    fun login(loginUser: LoginUser): Flow<ApiStatus<UserInfo>> =
        apiFlow {
            loginInterface.login("login", loginUser)
        }
}