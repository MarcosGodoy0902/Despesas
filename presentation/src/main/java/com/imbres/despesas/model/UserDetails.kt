package com.imbres.despesas.model

data class UserDetails(
    val checked: Boolean = false,
    val email: String = "",
    val password: String = "",
)