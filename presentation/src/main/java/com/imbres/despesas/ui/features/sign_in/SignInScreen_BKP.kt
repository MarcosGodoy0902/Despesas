package com.imbres.despesas.ui.features.sign_in

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
fun SignInScreen_BKP(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
    onGoToSignUpScreen: () -> Unit,
    onGoToLostPasswordScreen: () -> Unit,
) {
    Content_BKP(
        dataStoreManager,
        onGoBack,
        onGoToSignUpScreen,
        onGoToLostPasswordScreen
    )
}

@Composable
fun Content_BKP(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
    onGoToSignUpScreen: () -> Unit,
    onGoToLostPasswordScreen: () -> Unit,
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
            val scope = rememberCoroutineScope()
            val userDetails by dataStoreManager.getFromDataStore()
                .collectAsState(initial = null)
            var storeEmail = ""
            var storePassword = ""

            if (userDetails?.email?.isNotEmpty() == true) {
                storeEmail = userDetails!!.email
                storePassword = userDetails!!.password
                emailViewModel.updateEmail((storeEmail))
                passwordViewModel.updatePassword((storePassword))
            }

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

            // password
            ValidatingInputPassword(
                password = passwordViewModel.password,
                updateState = {
                    if (storePassword.isNotEmpty()) {
                        passwordViewModel.updatePassword((storePassword))
                    } else {
                        passwordViewModel.updatePassword(it)
                    }
                    if (it.isEmpty() || it !== userDetails?.password) {
                        scope.launch {
                            dataStoreManager.clearDataStore()
                            passwordViewModel.updatePassword(it)
                        }
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
                    viewModelButton.signInUp(
                        emailViewModel.email,
                        passwordViewModel.password,
                        "",
                        false
                    )
                }
                val errorButton =
                    !emailViewModel.emailHasErrors && emailViewModel.email.isNotEmpty() && !passwordViewModel.passwordHasErrors && passwordViewModel.password.isNotEmpty()
                ValidatingButton(onClick, errorButton, "Entrar")

                when {
                    viewModelButton.signUpUserInvalidCredentials.value -> SnackBarDisplay(
                        msg = "E-mail e/ou senha inválidos!",
                        onGoBack,
                        true
                    )

                    viewModelButton.signUpFail.value -> SnackBarDisplay(
                        msg = "Falha ou erro desconhecido.",
                        onGoBack,
                        true
                    )

                    viewModelButton.signUpUserExists.value -> SnackBarDisplay(
                        msg = "Acessar tela interna - dados ok.",
                        onGoBack,
                        false
                    )
                }
            }

            // remember user / lost password
            ValidatingUserAcess(
                userDetails,
                scope,
                dataStoreManager,
                emailViewModel,
                passwordViewModel,
                onGoToSignUpScreen,
                onGoToLostPasswordScreen
            )
        }
    }
}

@Composable
private fun ValidatingUserAcess(
    userDetails: UserDetails?,
    scope: CoroutineScope,
    dataStoreManager: DataStoreManager,
    emailViewModel: EmailViewModel,
    passwordViewModel: PasswordViewModel,
    onGoToSignUpScreen: () -> Unit,
    onGoToLostPasswordScreen: () -> Unit,
) {
    var checkedMeuUsuario by remember { mutableStateOf(false) }
    checkedMeuUsuario = userDetails?.checked ?: false

    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedMeuUsuario,
                onCheckedChange = {
                    scope.launch {
                        dataStoreManager.saveToDataStore(
                            UserDetails(
                                checked = it,
                                email = if (it) emailViewModel.email else "",
                                password = if (it) passwordViewModel.password else ""
                            )
                        )
                    }
                },
                enabled = emailViewModel.email.isNotEmpty() && passwordViewModel.password.isNotEmpty() && !emailViewModel.emailHasErrors && !passwordViewModel.passwordHasErrors
            )

            Text(
                text = "Lembrar meu usuário",
                modifier = Modifier
                    .clickable(
                        enabled = emailViewModel.email.isNotEmpty() && passwordViewModel.password.isNotEmpty() && !emailViewModel.emailHasErrors && !passwordViewModel.passwordHasErrors
                    ) {
                        checkedMeuUsuario = !checkedMeuUsuario
                        scope.launch {
                            dataStoreManager.saveToDataStore(
                                UserDetails(
                                    checked = checkedMeuUsuario,
                                    email = if (checkedMeuUsuario) emailViewModel.email else "",
                                    password = if (checkedMeuUsuario) passwordViewModel.password else ""
                                )
                            )
                        }
                    },
                fontSize = 14.sp,
                fontWeight = FontWeight(700),
                color = colorResource(id = R.color.blue_500),
            )
        }
        Text(
            text = "Esqueci a senha",
            modifier = Modifier
                .padding(end = 20.dp)
                .clickable {
                    onGoToLostPasswordScreen()
                },
            color = colorResource(id = R.color.blue_500),
            fontSize = 14.sp,
            fontWeight = FontWeight(700),
            textAlign = TextAlign.Start
        )
    }
    Text(
        text = "Criar uma conta",
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable {
                onGoToSignUpScreen()
            },
        color = colorResource(id = R.color.blue_500),
        fontSize = 14.sp,
        fontWeight = FontWeight(700),
        textAlign = TextAlign.Start
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview_BKP() {
    SignInScreen_BKP(
        dataStoreManager = DataStoreManager(LocalContext.current),
        onGoBack = { true },
        onGoToSignUpScreen = {},
        onGoToLostPasswordScreen = {}
    )
}