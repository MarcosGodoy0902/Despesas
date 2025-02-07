package com.imbres.despesas.ui.features.sign_in

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.components.EmailViewModel
import com.imbres.despesas.components.MyCustomSnackbar
import com.imbres.despesas.components.SnackBarDisplay
import com.imbres.despesas.components.ValidatingButton
import com.imbres.despesas.components.ValidatingInputEmail
import com.imbres.despesas.components.ViewModelButton
import com.imbres.despesas.components.getErrorColor
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LostPasswordScreen(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
) {
    // vars
    val emailViewModel: EmailViewModel = viewModel<EmailViewModel>()
    val scope = rememberCoroutineScope()
    val userDetails by dataStoreManager.getFromDataStore()
        .collectAsState(initial = null)
    val viewModelButton: ViewModelButton = viewModel<ViewModelButton>()
    val onClick = {
        viewModelButton.resendPassword(
            emailViewModel.email,
        )
    }
    val errorButton =
        !emailViewModel.emailHasErrors && emailViewModel.email.isNotEmpty()
    var storeEmail = ""
    val snackbarHostState = remember { SnackbarHostState() }
    var errorColor = getErrorColor(false)

    Scaffold(
        modifier = Modifier,
        topBar = {},
        bottomBar = {},
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                snackbar = { snackbarData ->
                    MyCustomSnackbar(snackbarData, Modifier, errorColor)
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // email
                ValidatingInputEmail(
                    email = emailViewModel.email,
                    updateState = {
                        if (storeEmail.isNotEmpty()) {
                            emailViewModel.updateEmail((storeEmail))
                        } else {
                            emailViewModel.updateEmail(it)
                        }
                        if (it.isEmpty() || it !== userDetails?.email) {
                            scope.launch {
                                dataStoreManager.clearDataStore()
                                emailViewModel.updateEmail(it)
                            }
                        }
                    },
                    validatorHasErrors = emailViewModel.emailHasErrors,
                )

                ValidatingButton(onClick, errorButton, "Entrar")

                var statusMsg = ""

                when {
                    viewModelButton.lostPasswordFail.value -> {
                        statusMsg = "Falha ou erro desconhecido."
                        errorColor = getErrorColor(true)
                        viewModelButton.signUpFail.value = false
                    }

                    viewModelButton.lostPasswordSucess.value -> {
                        statusMsg = "Instruções enviadas, caso o email esteja cadastrado."
                        viewModelButton.lostPasswordSucess.value = false
                    }
                }

                SnackBarDisplay(statusMsg, scope, snackbarHostState)

            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    LostPasswordScreen(
        dataStoreManager = DataStoreManager(LocalContext.current),
        onGoBack = { true },
    )
}