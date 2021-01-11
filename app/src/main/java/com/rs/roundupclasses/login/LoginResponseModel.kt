package com.rs.roundupclasses.login

data class LoginResponseModel(
    val status: Int? = 0,
    val name: String? = "",
    val email: String? = "",
    val userid: String? = ""
)