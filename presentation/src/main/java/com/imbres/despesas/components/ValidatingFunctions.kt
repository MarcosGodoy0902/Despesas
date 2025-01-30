package com.imbres.despesas.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imbres.despesas.R

/* Pesquisa:
https://developer.android.com/develop/ui/compose/text/user-input?hl=pt-br
https://github.com/android/snippets/blob/c79a414f423d09d009c92d69fb71e882e6edd39b/compose/snippets/src/main/java/com/example/compose/snippets/text/TextSnippets.kt#L537-L559
*/

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ValidatingInputEmail(
    email: String,
    updateState: (String) -> Unit,
    validatorHasErrors: Boolean,
) {
    val localFocusManager = LocalFocusManager.current
    val emailViewModel: EmailViewModel = viewModel<EmailViewModel>()

    OutlinedTextField(
        value = email,
        onValueChange = updateState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(400),
            color = colorResource(id = R.color.blue_500)
        ),
        label = { Text("Email") },
        leadingIcon = ({
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "",
                tint = colorResource(R.color.blue_500)
            )
        }),
        trailingIcon = ({
            IconButton(onClick = {
                emailViewModel.clearEmail()
                updateState("")
            }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = colorResource(id = R.color.blue_500)
                )
            }
        }),
        isError = validatorHasErrors,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        supportingText = {
            if (validatorHasErrors) {
                Text("Email inválido.")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.blue_500),
            unfocusedBorderColor = colorResource(id = R.color.blue_500),
            focusedLabelColor = colorResource(id = R.color.blue_500),
            unfocusedLabelColor = colorResource(id = R.color.blue_500),
            cursorColor = colorResource(id = R.color.blue_500),
            focusedPlaceholderColor = colorResource(id = R.color.blue_500),
        )
    )
}

class EmailViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set

    val emailHasErrors by derivedStateOf {
        if (email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    fun updateEmail(input: String) {
        email = input
    }

    fun clearEmail() {
        email = ""
    }
}

@Composable
fun ValidatingInputPassword(
    password: String,
    updateState: (String) -> Unit,
    validatorHasErrors: Boolean,
) {
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        value = password,
        onValueChange = updateState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(400),
            color = colorResource(id = R.color.blue_500)
        ),
        label = { Text("Password") },
        leadingIcon = ({
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "",
                tint = colorResource(R.color.blue_500)
            )
        }),
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(
                    imageVector = iconImage,
                    contentDescription = null,
                    tint = colorResource(id = R.color.blue_500)
                )
            }
        },
        isError = validatorHasErrors,
        visualTransformation = if (passwordVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        supportingText = {
            if (validatorHasErrors) {
                Text("Password inválida.")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.blue_500),
            unfocusedBorderColor = colorResource(id = R.color.blue_500),
            focusedLabelColor = colorResource(id = R.color.blue_500),
            unfocusedLabelColor = colorResource(id = R.color.blue_500),
            cursorColor = colorResource(id = R.color.blue_500),
            focusedPlaceholderColor = colorResource(id = R.color.blue_500),
        )
    )
}

class PasswordViewModel : ViewModel() {
    var password by mutableStateOf("")
        private set

    // Flag to track whether validation should be performed
    private var shouldValidate by mutableStateOf(false)

    val passwordHasErrors by derivedStateOf {
        if (!shouldValidate) return@derivedStateOf false

        val minLength = 6
        // Add more validation rules as needed
        password.isEmpty() || password.length < minLength
                || !password.contains(Regex("[A-Z]")) // Example: Requires uppercase
                || !password.contains(Regex("[a-z]")) // Example: Requires lowercase
                || !password.contains(Regex("[0-9]")) // Example: Requires a digit
    }

    fun updatePassword(input: String) {
        password = input
        shouldValidate = true // Enable validation after the first input
    }
}

@Composable
fun ValidatingButton(
    onClick: () -> Unit,
    errorButton: Boolean,
    textAction: String,
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(350.dp)
            .height(50.dp),
        enabled = errorButton,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color(0xFF000000)
        )
    ) {
        Text(
            text = textAction,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight(500)
        )
    }
}
