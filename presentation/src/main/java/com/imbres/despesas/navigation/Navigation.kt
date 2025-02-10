package com.imbres.despesas.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imbres.despesas.R
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.ui.features.sign_in.LostPasswordScreen
import com.imbres.despesas.ui.features.sign_in.SignInScreen
import com.imbres.despesas.ui.features.sign_up.SignUpScreen
import com.imbres.despesas.ui.features.splash.SplashApp
import com.imbres.despesas.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    dataStoreManager: DataStoreManager,
) {
    val navController = rememberNavController()
    val currentScreen =
        navController.currentBackStackEntryAsState().value?.destination?.route?.substringAfter(".Screen.")
    val topBarVisible = rememberSaveable { (mutableStateOf(false)) }
    val containerColorControl =
        if (topBarVisible.value) colorResource(R.color.blue_500) else Color.White
    var titleTopBar = ""

    topBarVisible.value = (currentScreen == "SignUp" || currentScreen == "LostPassword")

    when (currentScreen) {
        "SignUp" -> {
            titleTopBar = "Sua conta"
        }

        "LostPassword" -> {
            titleTopBar = "Acessar conta"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        //"Despesas",
                        titleTopBar,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColorControl,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() })
                    {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "null",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    }
                },

                )
        }
    ) { contentPadding ->
        NavHost(
            modifier = modifier.padding(contentPadding),
            navController = navController,
            startDestination = Screen.Splash,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            composable<Screen.Splash> {
                SplashApp(
                    dataStoreManager,
                    onGoToNextScreen = {
                        navController.popBackStack()
                        navController.navigate(Screen.SignIn)
                    }
                )
            }
            composable<Screen.SignUp> {
                SignUpScreen(
                    onGoBack = {
                        navController.popBackStack()
                    },
                )
            }
            composable<Screen.SignIn> {
                SignInScreen(
                    dataStoreManager,
                    onGoBack = {
                        navController.popBackStack()
                    },
                    onGoToSignUpScreen = {
                        navController.navigate(Screen.SignUp)
                    },
                    onGoToLostPasswordScreen = {
                        navController.navigate(Screen.LostPassword)
                    }
                )
            }
            composable<Screen.LostPassword> {
                LostPasswordScreen(
                    dataStoreManager,
                    onGoBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}