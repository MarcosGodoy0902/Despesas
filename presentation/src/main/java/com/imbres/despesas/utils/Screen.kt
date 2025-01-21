package com.imbres.despesas.utils

sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object Splash: Screen

}