package com.coders.chatapplication.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.coders.chatapplication.R
import com.coders.chatapplication.presentation.ui.login.LoginActivity
import com.coders.chatapplication.presentation.ui.rooms.RoomsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.failure.observe(this, {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        })

        viewModel.onSuccess.observe(this, {
            startActivity(Intent(this@SplashActivity, RoomsActivity::class.java))
            finish()
        })

        viewModel.checkSession()
    }
}
