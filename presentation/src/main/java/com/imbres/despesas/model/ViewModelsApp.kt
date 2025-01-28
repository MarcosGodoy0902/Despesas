package com.imbres.despesas.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QueryDocumentSnapshot

class ViewModelButton : ViewModel() {
    val lostPasswordInProgress = mutableStateOf(true)
    val lostPasswordSucess = mutableStateOf(false)
    val lostPasswordFail = mutableStateOf(false)

    fun login(email: String, password: String) {
        val userDataDocument: MutableState<QueryDocumentSnapshot?> = mutableStateOf(null)
    }

    fun resendPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
            lostPasswordInProgress.value = false
            if (it.isSuccessful) {
                lostPasswordSucess.value = true
            }
        }.addOnFailureListener {
            lostPasswordFail.value = true
        }
    }
}