package com.imbres.despesas.ui.features.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imbres.despesas.R
import com.imbres.despesas.components.EmailViewModel
import com.imbres.despesas.components.MyCustomSnackbar
import com.imbres.despesas.components.PasswordViewModel
import com.imbres.despesas.components.SnackBarDisplay
import com.imbres.despesas.components.ValidatingButton
import com.imbres.despesas.components.ValidatingInputEmail
import com.imbres.despesas.components.ValidatingInputPassword
import com.imbres.despesas.components.ViewModelButton
import com.imbres.despesas.components.getErrorColor

@Composable
fun SignUpScreen(
    onGoBack: () -> Boolean,
) {
    Column {
        Column(
            Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // vars
            val emailViewModel: EmailViewModel = viewModel<EmailViewModel>()
            val passwordViewModel: PasswordViewModel = viewModel<PasswordViewModel>()
            val scope = rememberCoroutineScope()
            var storeEmail = ""
            var storePassword = ""
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
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Títle
                        Text(
                            text = "Cadastro",
                            modifier = Modifier
                                .padding(end = 20.dp),
                            color = colorResource(id = R.color.blue_500),
                            fontSize = 14.sp,
                            fontWeight = FontWeight(700),
                            textAlign = TextAlign.Start
                        )

                        // email
                        ValidatingInputEmail(
                            email = emailViewModel.email,
                            updateState = {
                                if (storeEmail.isNotEmpty()) {
                                    emailViewModel.updateEmail((storeEmail))
                                } else {
                                    emailViewModel.updateEmail(it)
                                }
                            },
                            validatorHasErrors = emailViewModel.emailHasErrors,
                        )

                        // password
                        ValidatingInputPassword(
                            password = passwordViewModel.password,
                            updateState = {
                                if (storePassword.isNotEmpty()) {
                                    passwordViewModel.updatePassword((storePassword))
                                } else {
                                    passwordViewModel.updatePassword(it)
                                }
                            },
                            validatorHasErrors = passwordViewModel.passwordHasErrors,
                        )

                        // user access
                        Column(
                            Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            //  vars
                            val viewModelButton: ViewModelButton = viewModel<ViewModelButton>()
                            val onClick = {
                                viewModelButton.signInUp(
                                    emailViewModel.email,
                                    passwordViewModel.password,
                                    "",
                                    true
                                )
                            }
                            val errorButton =
                                !emailViewModel.emailHasErrors && emailViewModel.email.isNotEmpty() && !passwordViewModel.passwordHasErrors && passwordViewModel.password.isNotEmpty()

                            //  process
                            ValidatingButton(
                                onClick,
                                errorButton,
                                "Continuar",
                                viewModelButton.signUpInProgress.value
                            )

                            var statusMsg = ""

                            when {
                                viewModelButton.signUpUserExists.value -> {
                                    statusMsg = "E-mail informado já está em uso."
                                    errorColor = getErrorColor(true)
                                    viewModelButton.signUpUserExists.value = false
                                }

                                viewModelButton.signUpFail.value -> {
                                    statusMsg = "Falha ou erro desconhecido."
                                    errorColor = getErrorColor(true)
                                    viewModelButton.signUpFail.value = false
                                }

                                viewModelButton.signUpSucess.value -> {
                                    statusMsg = "Conta criada com sucesso!"
                                    errorColor = getErrorColor(false)
                                    viewModelButton.signUpSucess.value = false
                                }
                            }
                            SnackBarDisplay(statusMsg, scope, snackbarHostState, onGoBack)
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    SignUpScreen(
        onGoBack = { true },
    )
}