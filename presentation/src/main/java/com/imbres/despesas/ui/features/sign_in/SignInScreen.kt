package com.imbres.despesas.ui.features.sign_in

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.imbres.despesas.components.PasswordViewModel
import com.imbres.despesas.components.ValidatingButton
import com.imbres.despesas.components.ViewModelButton
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInScreen(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
    onGoToSignUpScreen: () -> Unit,
    onGoToLostPasswordScreen: () -> Unit,
) {
    // vars
    val emailViewModel: EmailViewModel = viewModel<EmailViewModel>()
    val passwordViewModel: PasswordViewModel = viewModel<PasswordViewModel>()
    val scope = rememberCoroutineScope()
    val userDetails by dataStoreManager.getFromDataStore()
        .collectAsState(initial = null)
    val viewModelButton: ViewModelButton = viewModel<ViewModelButton>()
    val onClick = {
        viewModelButton.signInUp(
            emailViewModel.email,
            passwordViewModel.password,
            "",
            false
        )
    }
    val errorButton =
        !emailViewModel.emailHasErrors && emailViewModel.email.isNotEmpty() && !passwordViewModel.passwordHasErrors && passwordViewModel.password.isNotEmpty()
    var storeEmail = ""
    var storePassword = ""

    if (userDetails?.email?.isNotEmpty() == true) {
        storeEmail = userDetails!!.email
        storePassword = userDetails!!.password
        emailViewModel.updateEmail((storeEmail))
        passwordViewModel.updatePassword((storePassword))
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier,
        topBar = {},
        bottomBar = {},
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val scope = rememberCoroutineScope()

                ValidatingButton(onClick, errorButton, "Entrar")
                if (viewModelButton.signUpUserExists.value) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Usuário localizado.",
                            actionLabel = "OK",
                            duration = SnackbarDuration.Short
                        ).run {
                            when (this) {
                                SnackbarResult.Dismissed -> {
                                    Log.d("SNACKBAR", "Dismissed")
                                    viewModelButton.signUpUserExists.value = false
                                }

                                SnackbarResult.ActionPerformed -> {
                                    Log.d(
                                        "SNACKBAR",
                                        "UNDO CLICKED"
                                    )
                                    viewModelButton.signUpUserExists.value = false
                                }
                            }
                        }
                    }
                }
            }
        }
    )


    /*
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ValidatingButton(onClick, errorButton, "Entrar")
            if (viewModelButton.signUpUserExists.value) {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    content = { padding ->
                        Column(
                            modifier = Modifier.padding(padding)
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Usuário localizado.",
                                    actionLabel = "OK",
                                    duration = SnackbarDuration.Short
                                ).run {
                                    when (this) {
                                        SnackbarResult.Dismissed -> {
                                            Log.d("SNACKBAR", "Dismissed")
                                            viewModelButton.signUpUserExists.value = false
                                        }

                                        SnackbarResult.ActionPerformed -> {
                                            Log.d(
                                                "SNACKBAR",
                                                "UNDO CLICKED"
                                            )
                                            viewModelButton.signUpUserExists.value = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    */
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    SignInScreen(
        dataStoreManager = DataStoreManager(LocalContext.current),
        onGoBack = { true },
        onGoToSignUpScreen = {},
        onGoToLostPasswordScreen = {}
    )
}