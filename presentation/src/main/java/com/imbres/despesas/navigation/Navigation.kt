package com.imbres.despesas.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.ui.features.splash.SplashApp
import com.imbres.despesas.utils.Screen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    dataStoreManager: DataStoreManager,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable<Screen.Splash> {
            SplashApp(
                onGoToNextScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}