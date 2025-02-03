package com.imbres.despesas.components

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore

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

        if (signUp) {
            userExist(emailNewUser)
            if (!signUpUserExists.value)
                createUserInFirebase(
                    email = emailNewUser,
                    password = passwordNewUser,
                    name = nameNewUser,
                )
        } else {
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
                        }
                    }
                    .addOnFailureListener { exception ->
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
    }

    fun signInUp2(
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
                            userExist(emailNewUser)
                            createUserInFirebase(
                                email = emailNewUser,
                                password = passwordNewUser,
                                name = nameNewUser,
                            )
                        }
                    }
                }
                .addOnFailureListener { exception ->
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

    fun userExist(emailUser: String) {
        val db = Firebase.firestore

        db.collection("users") // Substitua "users" pelo nome da sua coleção
            .whereEqualTo("email", emailUser) // Filtra documentos com o email fornecido
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val email =
                        document.getString("email") // Substitua "nome" pelo nome do seu campo
                    if (email != null) {
                        // E-mail já cadastrado
                        Log.d("SignUp exception ", "E-mail já cadastrado / nome: $email")
                        signUpUserExists.value = true
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Erro ao verificar e-mail
                Log.d("SignUp exception ", "erro ao verificar e-mail")
                signUpFail.value = true
            }
            .addOnCompleteListener { exception ->
                // Prosseguir com cadastro
                Log.d("SignUp exception ", "$exception")
                signUpUserExists.value = false
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