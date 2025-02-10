package com.imbres.despesas.components

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore

class ViewModelButton : ViewModel() {
    val signUpInProgress = mutableStateOf(false)
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
        signUpInProgress.value = true

        if (signUp) {
            createUserInFirebase(
                email = emailNewUser,
                password = passwordNewUser,
                name = nameNewUser
            )
        } else {
            if (emailNewUser.isNotEmpty() || passwordNewUser.isNotEmpty()) {
                FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(emailNewUser, passwordNewUser)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            signUpUserExists.value = true
                            signUpInProgress.value = false
                        }
                    }
                    .addOnFailureListener { exception ->
                        when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                // Credenciais inválidas
                                signUpUserInvalidCredentials.value = true
                                signUpInProgress.value = false
                            }

                            is FirebaseAuthInvalidUserException,
                            is FirebaseAuthException,
                            is FirebaseFirestoreException,
                                -> {
                                // Usuário não encontrado
                                signUpFail.value = true
                                signUpInProgress.value = false
                            }

                            else -> {
                                // Outro tipo de erro
                                signUpFail.value = true
                                signUpInProgress.value = false
                            }
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

    private fun createUserInFirebase(email: String, password: String, name: String) {
        val userId: MutableLiveData<String> = MutableLiveData()
        val dbStore = Firebase.firestore
        val db = FirebaseFirestore.getInstance()

        if (email.isNotEmpty() || password.isNotEmpty()) {

            dbStore.collection("users") // Substitua "users" pelo nome da sua coleção
                .whereEqualTo("email", email) // Filtra documentos com o email fornecido
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val email =
                            document.getString("email") // Substitua "nome" pelo nome do seu campo
                        if (email != null) {
                            // E-mail já cadastrado
                            signUpUserExists.value = true
                            signUpInProgress.value = false
                        }
                    }
                }
                .addOnCompleteListener { exception ->
                    // Prosseguir com cadastro
                    signUpInProgress.value = false
                }

            if (!signUpUserExists.value) {
                FirebaseAuth
                    .getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                    }
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
                                    signUpInProgress.value = false
                                }
                                .addOnFailureListener {
                                    signUpFail.value = true
                                    signUpInProgress.value = false
                                }
                        }
                    }
            }
        }
    }
}