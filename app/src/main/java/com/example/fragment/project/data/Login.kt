package com.example.fragment.project.data

import com.example.fragment.project.database.user.User
import com.example.miaow.base.http.HttpResponse

data class Login(
    val data: User? = null
) : HttpResponse()

data class Register(
    val data: User? = null
) : HttpResponse()