package com.imbres.despesas.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.imbres.despesas.components.DataStoreManager.Companion.EMAIL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun IsRegistered(
    preferencesDataStore: DataStore<Preferences>,
    dataStoreManager: DataStoreManager,
) {

    var isRegistered by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val onRegisterSuccess = { isRegistered = true }
    val onLogout = {
        isRegistered = false

        scope.launch {
            dataStoreManager.clearDataStore()
        }
    }

    LaunchedEffect(key1 = Unit) {
        checkRegisterState(preferencesDataStore) { it ->
            isRegistered = it
        }
    }

    if (isRegistered) {
        //HomePage(onLogout,dataStoreManager)
    } else {
        //RegisterPageUI(onRegisterSuccess, dataStoreManager)
    }
}

private suspend fun checkRegisterState(
    preferencesDataStore: DataStore<Preferences>,
    onResult: (Boolean) -> Unit,
) {
    val preferences = preferencesDataStore.data.first()
    val email = preferences[EMAIL]
    val isRegistered = email != null
    onResult(isRegistered)
}