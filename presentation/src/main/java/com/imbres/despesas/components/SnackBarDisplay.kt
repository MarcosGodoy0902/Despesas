package com.imbres.despesas.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.imbres.despesas.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SnackBarDisplay(
    statusMsg: String,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    if (statusMsg.isNotEmpty()) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = statusMsg,
                actionLabel = "OK",
                duration = SnackbarDuration.Short,
            )
            when (result) {
                SnackbarResult.Dismissed -> {
                    Log.d("SNACKBAR", "Dismissed")
                    //statusMsg = ""
                }

                SnackbarResult.ActionPerformed -> {
                    Log.d(
                        "SNACKBAR",
                        "UNDO CLICKED"
                    )
                    //statusMsg = ""
                }
            }
        }
    }
}

@Composable
fun getErrorColor(error: Boolean): Color {
    return if (error) Color.Red else colorResource(id = R.color.blue_500)
}

@Composable
fun MyCustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    errorColor: Color,
) {
    Snackbar(
        modifier = modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp),
        containerColor = errorColor,
        action = {
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.dismiss() }) {
                    Text(text = actionLabel, color = Color.White)
                }
            }
        }
    ) {
        Text(text = snackbarData.visuals.message, color = Color.White)
    }
}
