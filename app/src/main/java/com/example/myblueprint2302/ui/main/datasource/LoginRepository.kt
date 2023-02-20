package com.example.myblueprint2302.ui.main.datasource

import com.example.myblueprint2302.ui.main.data.LoginUser
import com.example.myblueprint2302.ui.main.data.UserInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val loginAPIInterface: LoginAPIInterface) {

    //loginAPIInterface.login()の返り値はRetrofit2のResponse型
    //↑をapiFlowがラップすることでResult型を出力するFlowを返す
    fun login(loginUser: LoginUser): Flow<ApiState<UserInfo>> = apiFlow {
        loginAPIInterface.login("login", loginUser)
    }
}