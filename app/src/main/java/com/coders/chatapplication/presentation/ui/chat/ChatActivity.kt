package com.coders.chatapplication.presentation.ui.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.toastIt
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatActivity : AppCompatActivity() {

	private val chatViewModel by viewModel<ChatViewModel>()
	private val sharedPrefs by inject<SharedPrefs>()

	private val messageList by bindView<RecyclerView>(R.id.message_list)
	private val messageInput by bindView<EditText>(R.id.message_input)
	private val messageSendButton by bindView<ImageView>(R.id.send_button)
	private val toolbar by bindView<Toolbar>(R.id.toolbar)

	private val messageAdapter = ChatAdapter(sharedPrefs.userId)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		chatViewModel.roomId = intent.getLongExtra("room_id", -1)
		supportActionBar?.title = intent.getStringExtra("room_name")
		messageList.layoutManager = LinearLayoutManager(this)
		messageList.adapter = messageAdapter
		chatViewModel.failure.observe(this, Observer {
			toastIt(it.exception.message ?: "error")
		})

		chatViewModel.roomMessages.observe(this, Observer {
			messageAdapter.update(it.toMutableList())
			messageList.post {
				messageList.smoothScrollToPosition(messageAdapter.itemCount)
			}
		})

		messageSendButton.setOnClickListener {
			val message = messageInput.text.toString()
			if (!TextUtils.isEmpty(message)) {
				chatViewModel.sendMessage(message.trim())
			}
			messageInput.text.clear()
		}

		chatViewModel.users.observe(this, Observer {
			messageAdapter.users = it
		})

		chatViewModel.getRoomWithUsers()

		chatViewModel.updateMessages()

		chatViewModel.subscribeToRoom()
	}

	override fun finish() {
		chatViewModel.unsubscribeFromRoom()
		super.finish()
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				finish()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}
