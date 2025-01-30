package com.imbres.despesas.utils

sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object Splash : Screen

    @kotlinx.serialization.Serializable
    data object SignUp : Screen

    @kotlinx.serialization.Serializable
    data object SignIn : Screen

    @kotlinx.serialization.Serializable
    data object LostPassword : Screen

}