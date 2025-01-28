package com.imbres.despesas.ui.features.lost_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imbres.despesas.components.DataStoreManager
import com.imbres.despesas.components.EmailViewModel
import com.imbres.despesas.components.ValidatingButton
import com.imbres.despesas.components.ValidatingInputEmail
import kotlinx.coroutines.launch

@Composable
fun LostPasswordScreen(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
    onGoToLostPasswordScreen: () -> Unit,
) {
    Content(
        dataStoreManager,
        onGoBack,
        onGoToLostPasswordScreen,
    )
}

@Composable
fun Content(
    dataStoreManager: DataStoreManager,
    onGoBack: () -> Boolean,
    onGoToSignUpScreen: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // vars
        val emailViewModel: EmailViewModel = viewModel<EmailViewModel>()
        val scope = rememberCoroutineScope()
        val userDetails by dataStoreManager.getFromDataStore()
            .collectAsState(initial = null)
        var storeEmail = ""

        if (userDetails?.email?.isNotEmpty() == true) {
            storeEmail = userDetails!!.email
            emailViewModel.updateEmail((storeEmail))
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

        //  process
        val errorButton =
            !emailViewModel.emailHasErrors
        ValidatingButton(errorButton, "Entrar")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    LostPasswordScreen(
        dataStoreManager = DataStoreManager(LocalContext.current),
        onGoBack = { true },
        onGoToLostPasswordScreen = {}
    )
}