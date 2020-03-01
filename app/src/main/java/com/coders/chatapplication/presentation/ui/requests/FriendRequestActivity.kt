package com.coders.chatapplication.presentation.ui.requests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coders.chatapplication.R
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.commons.toastIt
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FriendRequestActivity : AppCompatActivity() {

	private val viewModel: FriendRequestViewModel by viewModel()
	private val sharedPrefs by inject<SharedPrefs>()
	private val adapter = FriendRequestAdapter(::handleStatusChange, sharedPrefs.userId)

	private val toolbar by bindView<Toolbar>(R.id.toolbar)
	private val swipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.refresh_layout)
	private val requestList by bindView<RecyclerView>(R.id.request_list)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_friend_requests)

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		swipeRefreshLayout.setOnRefreshListener {
			viewModel.updateFriendships()
		}

		requestList.layoutManager = LinearLayoutManager(this)
		requestList.adapter = adapter

		viewModel.requests.observe(this, Observer {
			swipeRefreshLayout.isRefreshing = false
			adapter.update(it)
		})

		viewModel.failure.observe(this, Observer {
			toastIt(it.exception.localizedMessage)
			swipeRefreshLayout.isRefreshing = false
		})
		viewModel.loadRequests()
		viewModel.updateFriendships()
	}

	private fun handleStatusChange(model: FriendshipModel, newStatus: FriendshipStatus) {
		when (newStatus) {
			FriendshipStatus.ACCEPTED -> viewModel.updateFriendship(
				FriendshipModel(
					FriendshipStatus.ACCEPTED,
					model.userModel
				)
			)
			FriendshipStatus.NONE -> viewModel.deleteFriendship(model)
			else -> { /* do nothing */
			}
		}
	}
}