package com.imbres.despesas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.components.preferencesDataStore
import com.imbres.despesas.navigation.Navigation
import com.imbres.despesas.ui.theme.DespesasTheme
import com.imbres.despesas.utils.IsRegistered

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor =
            resources.getColor(R.color.background_app_bar, null)  // mudar cor da status bar
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars =
            false  // Para caracteres claros na status bar - manter "false"
        window.navigationBarColor = resources.getColor(R.color.background_app_bar, null)
        windowInsetsController.isAppearanceLightNavigationBars =
            false // Para caracteres claros na bottom bar - manter "false"

        setContent {
            DespesasTheme {
                val dataContext = LocalContext.current
                val dataStoreManager = DataStoreManager(dataContext)

                IsRegistered(
                    preferencesDataStore,
                    dataStoreManager
                )

                Navigation(
                    modifier = Modifier.fillMaxSize(),
                    dataStoreManager
                )

            }
        }
    }
}