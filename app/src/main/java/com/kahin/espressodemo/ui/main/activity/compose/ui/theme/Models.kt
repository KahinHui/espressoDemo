package com.kahin.espressodemo.ui.main.activity.compose.ui.theme

data class User(
    var name: String,
    var pwd: String,
    var isLogIn: Boolean = false,
    var isSuccess: Boolean? = null,
)