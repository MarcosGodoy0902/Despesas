package com.imbres.despesas.ui.features.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imbres.despesas.components.EmailViewModel
import com.imbres.despesas.components.PasswordViewModel
import com.imbres.despesas.components.SnackBarDisplay
import com.imbres.despesas.components.ValidatingButton
import com.imbres.despesas.components.ValidatingInputEmail
import com.imbres.despesas.components.ValidatingInputPassword
import com.imbres.despesas.components.ViewModelButton

@Composable
fun SignUpScreen(
    onGoBack: () -> Boolean,
) {
    Content(
        onGoBack,
    )
}

@Composable
fun Content(
    onGoBack: () -> Boolean,
) {
    Column {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // vars
            val emailViewModel: EmailViewModel = viewModel<EmailViewModel>()
            val passwordViewModel: PasswordViewModel = viewModel<PasswordViewModel>()
            var storeEmail = ""
            var storePassword = ""

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
                validatorHasErrors = passwordViewModel.passwordHasErrors
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
                ValidatingButton(onClick, errorButton, "Continuar")

                when {
                    viewModelButton.signUpUserExists.value -> SnackBarDisplay(
                        msg = "E-mail informado já está em uso.",
                        onGoBack,
                        true
                    )

                    viewModelButton.signUpFail.value -> SnackBarDisplay(
                        msg = "Falha ou erro desconhecido.",
                        onGoBack,
                        true
                    )


                    viewModelButton.signUpSucess.value -> SnackBarDisplay(
                        msg = "Conta criada com sucesso!",
                        onGoBack,
                        false
                    )
                }
            }
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