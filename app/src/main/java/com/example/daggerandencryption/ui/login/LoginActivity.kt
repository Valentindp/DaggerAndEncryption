package com.example.daggerandencryption.ui.login

import android.os.Bundle
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import androidx.appcompat.app.AppCompatActivity
import com.example.daggerandencryption.App
import com.example.daggerandencryption.databinding.ActivityLoginBinding
import com.example.daggerandencryption.ui.base.BaseActivity
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(ActivityLoginBinding.inflate(layoutInflater)) {
            setContentView(root)

            viewModel.credentialsLiveData.observe(this@LoginActivity) { credentials ->
                username.setText(credentials.login)
                password.setText(credentials.password)
            }

            password.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        IME_ACTION_DONE -> viewModel.login(username.text.toString(), password.text.toString())
                    }
                    false
                }

                login.setOnClickListener { viewModel.login(username.text.toString(), password.text.toString()) }
            }
        }
    }

}
