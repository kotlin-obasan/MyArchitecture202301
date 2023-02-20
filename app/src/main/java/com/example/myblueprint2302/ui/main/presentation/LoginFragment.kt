package com.example.myblueprint2302.ui.main.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myblueprint2302.R
import com.example.myblueprint2302.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        fragmentLoginBinding = binding

        // ログインボタンの活性状態オブザーバ
        loginViewModel.isButtonEnabled().observe(this) {
            binding.buttonLogIn.isEnabled = it
        }

        //Progress dialogのオブザーバー
        loginViewModel.isProgressDialog().observe(this) {
            when (it) {
                true -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                false -> {
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

        // エラーダイアログの表示
        loginViewModel.showCommonErrorDialog.observe(viewLifecycleOwner) {
            loginViewModel.errorMessage.value?.let { message ->
                showCommonErrorDialog(message)
            }
        }

        // ログイン成功時に次の画面に遷移する
        loginViewModel.isLoginCompleted().observe(this) {
            if(it == true) {
                transitionToNextFragment()
            }
        }

        binding.buttonLogIn.setOnClickListener {
            // ログイン処理を行う
            login()
            // とにかく遷移
            transitionToNextFragment()
        }

        // メアドとパスワード入力欄の編集中にバリデーションチェックを行う
        class CustomTextWatcher(private val watchedView: View) : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence?,
                                           start: Int, count: Int, after: Int) {
                // do nothing
            }
            override fun onTextChanged(s: CharSequence?,
                                       start: Int, before: Int, count: Int) {
                when (watchedView.id) {
                    R.id.login_mail_edit -> {
                        loginViewModel.isCorrectMailAddress(s.toString())
                    }
                    R.id.login_password_edit -> {
                        loginViewModel.isCorrectPassword(s.toString())
                    }
                }
            }
        }

        //メールアドレスとパスワードの入力欄にリスナーをセットする
        binding.loginMailEdit.addTextChangedListener(CustomTextWatcher(binding.loginMailEdit))
        binding.loginPasswordEdit.addTextChangedListener(CustomTextWatcher(binding.loginPasswordEdit))
    }

    // bindingにnullを入れる
    override fun onDestroy() {
        fragmentLoginBinding = null
        super.onDestroy()
    }

    private fun transitionToNextFragment() {
//        val action = LoginFragmentDirections.actionLoginFragmentToNextFragment()
//        findNavController().navigate(action)
    }

    private fun login() {
        loginViewModel.login()
    }

    private fun showCommonErrorDialog(message: String) {
//        val action = LoginFragmentDirections
//            .actionLoginFragmentToCommonErrorDialogFragment(message)
//        findNavController().navigate(action)
    }
}