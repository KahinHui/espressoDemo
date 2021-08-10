package com.kahin.espressodemo.ui.main.activity.compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogInViewModel : ViewModel() {
    val userData: LiveData<User>
        get() = _userData

    private val _userData = MutableLiveData<User>()

    fun logIn(user: User)/*: Flow<User>*/ {
//        return flow {
            user.isLogIn = user.name == "123" && user.pwd == "111"
            _userData.postValue(user)
//        }
    }

    fun logOut() {
        _userData.postValue(User("", ""))
    }
}