package com.coders.chatapplication.presentation.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.toastIt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatActivity : AppCompatActivity() {

	private val chatViewModel by viewModel<ChatViewModel>()
	private val sharedPrefs by inject<SharedPrefs>()

	private val messageList by bindView<RecyclerView>(R.id.message_list)
	private val messageInput by bindView<EditText>(R.id.message_input)
	private val messageSendButton by bindView<ImageView>(R.id.send_button)

	private val messageAdapter = ChatAdapter(sharedPrefs.userId)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)
		chatViewModel.roomId = intent.getLongExtra("room_id", -1)
		messageList.layoutManager = LinearLayoutManager(this)
		messageList.adapter = messageAdapter
		chatViewModel.failure.observe(this, Observer {
			toastIt(it.exception.message ?: "error")
		})

		chatViewModel.messageReceived.observe(this, Observer {
			messageAdapter.insert(it)
			messageList.smoothScrollToPosition(messageAdapter.itemCount)
		})

		chatViewModel.roomMessages.observe(this, Observer {
			messageAdapter.update(it.toMutableList())
			chatViewModel.viewModelScope.launch {
				delay(2_000)
				messageList.smoothScrollToPosition(messageAdapter.itemCount)
			}
		})

		messageSendButton.setOnClickListener {
			val message = messageInput.text.toString()
			if (!TextUtils.isEmpty(message)) {
				chatViewModel.sendMessage(message)
			}
			messageInput.text.clear()
		}

		chatViewModel.getRoomMessages()

		chatViewModel.subscribeToRoom()
	}

	override fun finish() {
		chatViewModel.unsubscribeFromRoom()
		super.finish()
	}
}
