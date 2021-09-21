package com.example.daggerandencryption.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.daggerandencryption.data.LoginRepository
import com.example.daggerandencryption.data.model.Credentials
import com.example.daggerandencryption.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var loginRepository: LoginRepository

    private val _credentialsLiveData = MutableLiveData<Credentials>()

    init {
        loginRepository.observeCredentials()
            .onEach { _credentialsLiveData.value = it }
            .catch { /* error handler */ }
            .launchIn(viewModelScope)
    }

    val credentialsLiveData: LiveData<Credentials>
        get() = _credentialsLiveData

    fun login(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.login(login, password)
        }
    }

}