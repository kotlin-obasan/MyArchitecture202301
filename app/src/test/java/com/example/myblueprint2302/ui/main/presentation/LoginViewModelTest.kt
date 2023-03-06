package com.example.myblueprint2302.ui.main.presentation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myblueprint2302.ui.main.data.ApiStatus
import com.example.myblueprint2302.ui.main.data.dto.LoginUser
import com.example.myblueprint2302.ui.main.data.dto.UserInfo
import com.example.myblueprint2302.ui.main.data.repository.LoginRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @MockK
    lateinit var  loginRepository: LoginRepository

    private val mockContext = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginRepository = mockk()
        viewModel = LoginViewModel(mockContext, loginRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `メールアドレスの入力チェック_異常`() {
        assertThat(viewModel.isCorrectMailAddress("test")).isEqualTo(false)
    }

    @Test
    fun `メールアドレスの入力チェック_正常`() {
        assertThat(viewModel.isCorrectMailAddress("test@news.picks.com")).isEqualTo(true)
    }

    @Test
    fun `パスワードの入力チェック_正常`() {
        assertThat(viewModel.isCorrectPassword("123456")).isEqualTo(true)
    }

    @Test
    fun `パスワードの入力チェック_異常`() {
        assertThat(viewModel.isCorrectPassword("123")).isEqualTo(false)
    }

    //todo: テスト通らない
    @Test
    fun `login_正常系`() {
        val user = LoginUser("xxx@sample.com", "password")
        val response = UserInfo("examtoken")

        //モック
        coEvery { loginRepository.login(user) } returns flow {
            emit(ApiStatus.Success(value = response))
        }

        //実際にコール
        viewModel.mailAddress.value = "xxx@sample.com"
        viewModel.password.value = "password"
        viewModel.loginLiveData.observeForever { }
        viewModel.login()

        //比較する
        val loginSuccess = viewModel.loginLiveData.value
        assertThat(loginSuccess).isEqualTo(response)
    }

    //todo: テスト通らない
    @Test
    fun `login_異常系`() {
        val user = LoginUser("wrong@aaa.bbb", "wrongpass")
        val response = Throwable()

        //Mockz
        coEvery { loginRepository.login(user) } returns flow {
            emit(ApiStatus.Error(response))
        }

        //実際にコールする
        viewModel.mailAddress.value = "wrong@aaa.bbb"
        viewModel.password.value = "wrongpass"
        viewModel.loginLiveData.observeForever { }
        viewModel.login()

        //比較
        val loginSuccess = viewModel.loginLiveData.value
        assertThat(loginSuccess).isEqualTo(response)
    }
}