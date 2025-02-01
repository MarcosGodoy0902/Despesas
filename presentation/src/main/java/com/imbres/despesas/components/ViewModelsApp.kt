package com.imbres.despesas.components

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ViewModelButton : ViewModel() {
    val signUpInProgress = mutableStateOf(true)
    val signUpSucess = mutableStateOf(false)
    val signUpUserExists = mutableStateOf(false)
    val signUpFail = mutableStateOf(false)
    val signUpUserInvalidCredentials = mutableStateOf(false)

    val lostPasswordInProgress = mutableStateOf(true)
    val lostPasswordSucess = mutableStateOf(false)
    val lostPasswordFail = mutableStateOf(false)

    fun signInUp(
        emailNewUser: String,
        passwordNewUser: String,
        nameNewUser: String,
        signUp: Boolean,
    ) {
        val userDataDocument: MutableState<QueryDocumentSnapshot?> = mutableStateOf(null)

        if (emailNewUser.isNotEmpty() || passwordNewUser.isNotEmpty() || nameNewUser.isNotEmpty()) {
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(emailNewUser, passwordNewUser)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signUpInProgress.value = false
                        signUpUserExists.value = true
                        signUpFail.value = false
                        signUpUserInvalidCredentials.value = false
                    } else {
                        if (signUp) {
                            createUserInFirebase(
                                email = emailNewUser,
                                password = passwordNewUser,
                                name = nameNewUser,
                            )
                        } else {
                            userExist(emailNewUser)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("SignIn exception ", "$exception")
                    when (exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Credenciais inválidas
                            signUpInProgress.value = false
                            signUpUserInvalidCredentials.value = true
                        }

                        is FirebaseAuthInvalidUserException -> {
                            // Usuário não encontrado
                            signUpInProgress.value = false
                            signUpFail.value = true
                        }

                        is FirebaseAuthException -> {
                            // Outro erro de autenticação
                            signUpInProgress.value = false
                            signUpFail.value = true
                        }

                        else -> {
                            // Outro tipo de erro
                            signUpInProgress.value = false
                            signUpFail.value = true
                        }
                    }
                }
        }
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

    fun userExist(email: String) {
        val credential = EmailAuthProvider.getCredential(email, "asdfA1234")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    signUpUserExists.value = true
                } else {
                    val errorCode = task.exception
                    signUpUserExists.value = false
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Sign exception ", "$exception")
            }
    }

    private fun createUserInFirebase(email: String, password: String, name: String) {
        val userId: MutableLiveData<String> = MutableLiveData()
        val db = FirebaseFirestore.getInstance()

        if (email.isNotEmpty() || password.isNotEmpty()) {
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    userId.value = FirebaseAuth.getInstance().uid
                    if (it.isSuccessful) {
                        val data = hashMapOf(
                            "name" to name,
                            "email" to email,
                        )

                        db.collection("users").document(userId.value.toString())
                            .set(data)
                            .addOnSuccessListener {
                                signUpSucess.value = true
                            }
                            .addOnFailureListener {
                                signUpFail.value = true
                            }
                        signUpInProgress.value = false
                    }
                }
                .addOnFailureListener {
                    signUpFail.value = true
                }
        }
    }
}