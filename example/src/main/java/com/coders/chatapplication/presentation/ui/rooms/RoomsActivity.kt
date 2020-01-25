package com.coders.chatapplication.presentation.ui.rooms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.toastIt
import com.coders.chatapplication.presentation.ui.chat.ChatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomsActivity : AppCompatActivity() {

	private val roomList by bindView<RecyclerView>(R.id.room_list)
	private val roomsAdapter = RoomsAdapter(::onRoomClicked)
	private val addRoomButton by bindView<FloatingActionButton>(R.id.create_room_btn)

	private val roomViewModel by viewModel<RoomsViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_rooms)

		roomList.layoutManager = LinearLayoutManager(this)
		roomList.adapter =roomsAdapter

		addRoomButton.setOnClickListener {
			toastIt("Create new room")
		}

		roomViewModel.onRoomsReceived.observe(this, Observer {
			roomsAdapter.update(it)
		})

		roomViewModel.failure.observe(this, Observer {
			toastIt(it.exception.message ?: "Error")
		})

		roomViewModel.getRooms()
	}

	private fun onRoomClicked(roomModel: RoomModel){
		toastIt("Room ${roomModel.name} opening")
		startActivity(
			Intent(this, ChatActivity::class.java).apply {
				putExtra("room_id", roomModel.id)
			}
		)
	}
}
