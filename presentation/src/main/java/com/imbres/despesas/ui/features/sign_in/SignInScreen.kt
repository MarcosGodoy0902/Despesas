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
import com.imbres.despesas.components.ValidatingInputEmail
import com.imbres.despesas.components.ValidatingInputPassword
import com.imbres.despesas.model.UserDetails
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
    onGoToSignInScreen: () -> Unit,
    onGoToLostPasswordScreen: () -> Unit,
) {
    Content(
        dataStoreManager,
        onGoBack,
        onGoToSignInScreen,
        onGoToLostPasswordScreen
    )
}

@Composable
fun Content(
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
            var emailViewModel: EmailViewModel = viewModel<EmailViewModel>()
            var passwordViewModel: PasswordViewModel = viewModel<PasswordViewModel>()
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

            ValidatingInputPassword(
                password = passwordViewModel.password,
                //updateState = { input -> passwordViewModel.updatePassword(input) },
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

            var checkedMeuUsuario by remember { mutableStateOf(false) }
            checkedMeuUsuario = userDetails?.checked ?: false

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight(700),
                        color = colorResource(id = R.color.blue_500),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = "Esqueci a senha",
                    modifier = Modifier.clickable {
                        onGoToLostPasswordScreen()
                    },
                    color = colorResource(id = R.color.blue_500),
                    fontSize = 12.sp,
                    fontWeight = FontWeight(700),
                    textAlign = TextAlign.Start
                )

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    SignInScreen(
        dataStoreManager = DataStoreManager(LocalContext.current),
        onGoBack = { true },
        onGoToSignInScreen = {},
        onGoToLostPasswordScreen = {}
    )
}