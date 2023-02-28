package com.example.myblueprint2302.ui.main.presentation

import android.content.Context
import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.myblueprint2302.ui.main.data.LoginUser
import com.example.myblueprint2302.ui.main.data.UserInfo
import com.example.myblueprint2302.ui.main.repository.LoginRepository
import com.example.myblueprint2302.ui.main.ext.default
import com.example.myblueprint2302.ui.main.repository.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginRepository: LoginRepository
): ViewModel() {

    // メールアドレス
    var mailAddress = MutableLiveData<String>().default("")
    // パスワード
    var password = MutableLiveData<String>().default("")

    // ログインボタンの活性・非活性
    private var buttonEnable = MutableLiveData<Boolean>().default(false)
    fun isButtonEnabled(): LiveData<Boolean> = buttonEnable

    // プログレスの表示・非表示
    private var progressDialog = MutableLiveData<Boolean>().default(false)
    fun isProgressDialog(): LiveData<Boolean> = progressDialog

    // ログイン成功フラグ
    private var loginCompleted = MutableLiveData<Boolean>().default(false)
    fun isLoginCompleted(): LiveData<Boolean> = loginCompleted

    private val isCorrectMail = MutableLiveData<Boolean>().default(false)
    private val isCorrectPass = MutableLiveData<Boolean>().default(false)

    // ログインAPIがエラーだったときのメッセージ
    val errorMessage = MutableLiveData<String>().default("")

    // エラーダイアログ
    val showCommonErrorDialog = MutableLiveData<Boolean>()

    //ログインAPIからの戻り値
    private val loginLiveDataPrivate = MutableLiveData<ApiStatus<UserInfo>>()
    val loginLiveData: LiveData<ApiStatus<UserInfo>> get() = loginLiveDataPrivate

    //todo; デバッグしてる時間がなかったのでMediatorLiveData諦めます
    //ほんとはこうしたかった、、、
//    var buttonEnable = MediatorLiveData<Boolean>().default(false).apply {
//        addSource(mailAddress) {
//            value = isButtonEnable() }
//        addSource(password) { value = isButtonEnable() }
//    }
//
//    private fun isButtonEnable(): Boolean {
//        password.value?.let { password ->
//            //メアドのバリデーション
//            return mailAddress.isEmpty() &&
//                    android.util.Patterns.EMAIL_ADDRESS.matcher(mailAddress.value).matches() &&
//                    //パスワードのバリデーション
//                    password.length >= 6
//        } ?: return false
//    }

    fun isCorrectMailAddress(mail: String): Boolean {
        mailAddress.value = mail

        isCorrectMail.value = PatternsCompat.EMAIL_ADDRESS.matcher(mail).matches()
        buttonEnable.value = isCorrectPass.value == true && isCorrectMail.value == true

        return isCorrectMail.value == true
    }

    fun isCorrectPassword(pass: String): Boolean {
        password.value = pass
        isCorrectPass.value = (pass.length >= REQUIREMENT_PASSWORD_LENGTH) && isLettersOrDigits(pass)
        buttonEnable.value = isCorrectPass.value == true && isCorrectMail.value == true

        return isCorrectPass.value == true
    }

    // 半角英数字チェック
    private fun isLettersOrDigits(chars: String): Boolean {
        return chars.matches("^[a-zA-Z0-9]*$".toRegex())
    }

    //ログイン処理を行う
    fun login() {
        buttonEnable.value = false
        progressDialog.value = true

        //ログインに使うリクエストを生成
        val loginUser = mailAddress.value?.let { mail ->
            password.value?.let { pass -> LoginUser(mail, pass) }
        } ?: return

        viewModelScope.launch {
            try {
                loginRepository.login(loginUser).collectLatest {
                    when(it) {
                        is ApiStatus.Success -> {
                            progressDialog.value = false
                            Log.d("loginRepository result",it.value.toString())
                            loginLiveDataPrivate.postValue(it)

                            //AccessTokenを保存する
                            saveAccessToken(it.value.access_token)
                            //次の画面へ遷移
                            loginCompleted.value = true
                        }
                        is ApiStatus.Error -> {
                            Log.d("loginRepository result", "$it.errorCode")
                            loginLiveDataPrivate.value = it
                            progressDialog.value = false
                            buttonEnable.value = true
                            //エラーメッセージを指定
//                            errorMessage.value = it.error.message
                            //エラーダイアログを表示する
                            showCommonErrorDialog.value = true
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                showCommonErrorDialog.value = true
            }
        }
    }

    private fun saveAccessToken(accessToken: String) {
        // Preferenceから利用者情報を取り出す
        val mainKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREF_FILENAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        val editor = sharedPreferences.edit()
        editor.putString(PREF_KEYNAME, accessToken)
        editor.apply()
    }

    companion object {
        //パスワードの最低文字数
        private const val REQUIREMENT_PASSWORD_LENGTH = 6

        //Preferenceのファイル名
        private const val PREF_FILENAME = "UserInfo"
        //Preferenceに使うkey
        private const val PREF_KEYNAME = "token"
    }
}