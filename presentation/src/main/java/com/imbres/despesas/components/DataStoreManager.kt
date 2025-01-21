package com.imbres.despesas.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.imbres.despesas.model.UserDetails
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"
val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(private val context: Context) {

    companion object {
        val CHECKED = booleanPreferencesKey("CHECKED")
        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
    }

    suspend fun saveToDataStore(userDetails: UserDetails) {
        context.preferencesDataStore.edit {
            it[CHECKED] = userDetails.checked
            it[EMAIL] = userDetails.email
            it[PASSWORD] = userDetails.password
        }
    }

    fun getFromDataStore() = context.preferencesDataStore.data.map {
        UserDetails(
            checked = it[CHECKED] ?: false,
            email = it[EMAIL] ?: "",
            password = it[PASSWORD] ?: ""
        )
    }

    suspend fun clearDataStore() = context.preferencesDataStore.edit {
        it.clear()
    }
}