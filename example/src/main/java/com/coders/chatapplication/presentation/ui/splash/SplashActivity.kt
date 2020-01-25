package com.coders.chatapplication.presentation.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.coders.chatapplication.R
import com.coders.chatapplication.presentation.ui.login.LoginActivity
import com.coders.chatapplication.presentation.ui.rooms.RoomsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

	private val viewModel by viewModel<SplashViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		viewModel.failure.observe(this, Observer {
			startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
			finish()
		})

		viewModel.onSuccess.observe(this, Observer {
			startActivity(Intent(this@SplashActivity, RoomsActivity::class.java))
			finish()
		})

		viewModel.checkSession()
	}
}
