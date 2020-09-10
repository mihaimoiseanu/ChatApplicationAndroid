package com.coders.chatapplication.presentation.ui.register

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.coders.chatapplication.R
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.toastIt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val firstNameInput by bindView<EditText>(R.id.input_first_name)
    private val lastNameInput by bindView<EditText>(R.id.input_last_name)
    private val emailInput by bindView<EditText>(R.id.input_email)
    private val passInput by bindView<EditText>(R.id.input_password)
    private val registerButton by bindView<AppCompatButton>(R.id.btn_signup)

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel.failure.observe(this, {
            toastIt(it.exception.message ?: "Error")
        })

        viewModel.registerSuccess.observe(this, {
            finish()
        })

        registerButton.setOnClickListener {
            doRegister()
        }
    }

    private fun doRegister() {
        //for the moment we go like this
        viewModel.register(
            emailInput.text.toString(),
            passInput.text.toString(),
            firstNameInput.text.toString(),
            lastNameInput.text.toString()
        )
    }
}