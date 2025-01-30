package com.imbres.despesas.ui.features.sign_up

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imbres.despesas.R
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.components.EmailViewModel
import com.imbres.despesas.components.PasswordViewModel
import com.imbres.despesas.components.SnackBarDisplay
import com.imbres.despesas.components.ValidatingButton
import com.imbres.despesas.components.ValidatingInputEmail
import com.imbres.despesas.components.ValidatingInputPassword
import com.imbres.despesas.components.ViewModelButton
import com.imbres.despesas.model.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onGoBack: () -> Boolean,
    onGoToSignInScreen: () -> Unit,
) {
    Content(
        onGoBack,
        onGoToSignInScreen
    )
}

@Composable
fun Content(
    onGoBack: () -> Boolean,
    onGoToSignInScreen: () -> Unit,
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
                //  process
                val viewModelButton: ViewModelButton = viewModel<ViewModelButton>()
                val onClick = {
                    viewModelButton.login(
                        emailViewModel.email,
                        passwordViewModel.password,
                        "Marcos"
                    )
                }
                val errorButton =
                    !emailViewModel.emailHasErrors && emailViewModel.email.isNotEmpty() && !passwordViewModel.passwordHasErrors && passwordViewModel.password.isNotEmpty()
                ValidatingButton(onClick, errorButton, "Continuar")

                if (viewModelButton.signUpSucess.value || viewModelButton.signUpFail.value) {
                    SnackBarDisplay(
                        msg = "Conta criada com sucesso!",
                        onGoBack,
                        false
                    )
                } else {
                    if (viewModelButton.signUpUserExists.value) {
                        SnackBarDisplay(
                            msg = "E-mail informado já está em uso.",
                            onGoBack,
                            true
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    SignUpScreen(
        //dataStoreManager = DataStoreManager(LocalContext.current),
        onGoBack = { true },
        onGoToSignInScreen = {}
    )
}