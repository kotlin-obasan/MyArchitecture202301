package com.example.myblueprint2302.ui.main.data.dto

import androidx.annotation.Keep

@Keep
data class LoginUser(
    val mail_address: String,
    val password: String,
)