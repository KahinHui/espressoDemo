package com.kahin.espressodemo.ui.main.activity.compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kahin.espressodemo.ui.main.activity.compose.state.UiState
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel : ViewModel() {

    val state: LiveData<UiState<User>>
        get() = _state

    private val _state = MutableLiveData<UiState<User>>()

    fun logInSuccess(user: User) {
        viewModelScope.launch {
            loading(user = user)

            logIn(user = user)
        }
    }

    fun logOut(user: User) {
        viewModelScope.launch {
            loading(user = user)

            _state.postValue(UiState(loading = true, data = User("", "")))
        }
    }

    private suspend fun loading(user: User) {
        withContext(Dispatchers.Main) {
            _state.postValue(UiState(loading = true, data = user))
        }
    }

    private suspend fun logIn(user: User) {
        withContext(Dispatchers.IO) {
            delay(1000L)
            user.isLogIn = user.name == "123" && user.pwd == "111"
            user.isSuccess = user.isLogIn
            _state.postValue(UiState(loading = false, data = user))
        }
    }
}