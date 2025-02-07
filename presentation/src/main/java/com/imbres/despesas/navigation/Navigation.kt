package com.imbres.despesas.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.ui.features.sign_in.LostPasswordScreen
import com.imbres.despesas.ui.features.sign_in.SignInScreen
import com.imbres.despesas.ui.features.sign_up.SignUpScreen
import com.imbres.despesas.ui.features.splash.SplashApp
import com.imbres.despesas.utils.Screen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    dataStoreManager: DataStoreManager,
) {
    val navController = rememberNavController()

    Scaffold(

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