package com.imbres.despesas.components

import android.annotation.SuppressLint
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.imbres.despesas.R
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SnackBarDisplay(
    msg: String, onGoBack: () -> Boolean, error: Boolean,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorColor = getErrorColor(error)

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MyCustomSnackbar(snackbarData, Modifier, errorColor)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
        ) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = msg,
                    //actionLabel = "Retry",
                    //withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
                when (result) {
                    SnackbarResult.Dismissed -> {
                        // Ação quando o snackbar é dispensado
                        if (!error) {
                            //navigateToStart(navController)
                            onGoBack()
                        }
                    }

                    SnackbarResult.ActionPerformed -> {
                        // Ação quando o botão "Retry" é pressionado
                    }
                }
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            MyCustomSnackbar(snackbarData, Modifier, errorColor)
        }
    )

    Column(
        modifier = Modifier
    ) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = msg,
                //actionLabel = "Retry",
                //withDismissAction = true,
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.Dismissed -> {
                    // Ação quando o snackbar é dispensado
                    if (!error) {
                        //navigateToStart(navController)
                        onGoBack()
                    }
                }

                SnackbarResult.ActionPerformed -> {
                    // Ação quando o botão "Retry" é pressionado
                }
            }
        }
    }
}

@Composable
private fun getErrorColor(error: Boolean): Color {
    return if (error) Color.Red else colorResource(id = R.color.blue_500)
}

@Composable
private fun MyCustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    errorColor: Color,
) {
    Snackbar(
        modifier = modifier.padding(10.dp),
        containerColor = errorColor,
        action = {
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.dismiss() }) {
                    Text(text = actionLabel, color =Color.White)
                }
            }
        }
    ) {
        Text(text = snackbarData.visuals.message, color =Color.White)
    }
}

