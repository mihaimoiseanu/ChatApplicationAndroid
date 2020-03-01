package com.coders.chatapplication.presentation.ui.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.FriendshipModel
import com.coders.chatapplication.domain.model.FriendshipStatus
import com.coders.chatapplication.presentation.commons.AsyncDiffUtil
import com.coders.chatapplication.presentation.commons.goneUnless
import com.coders.chatapplication.presentation.ui.views.AvatarImageView

class FriendRequestAdapter(
	private val changeStatus: (model: FriendshipModel, newStatus: FriendshipStatus) -> Unit,
	private val thisUserId: Long
) : RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>() {

	private val diffUtil = AsyncDiffUtil(this, object : DiffUtil.ItemCallback<FriendshipModel>() {
		override fun areItemsTheSame(oldItem: FriendshipModel, newItem: FriendshipModel): Boolean {
			return oldItem.userModel.id == newItem.userModel.id
		}

		override fun areContentsTheSame(
			oldItem: FriendshipModel,
			newItem: FriendshipModel
		): Boolean {
			return oldItem == newItem
		}
	})

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context).inflate(
				R.layout.item_friendship_request, parent, false
			)
		)
	}

	fun update(requests: List<FriendshipModel>) {
		diffUtil.update(requests.toMutableList())
	}

	override fun getItemCount(): Int = diffUtil.current().size


	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindView(diffUtil.current()[position])
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val acceptButton = itemView.findViewById<Button>(R.id.accept_button)
		private val declineButton = itemView.findViewById<Button>(R.id.decline_button)
		private val cancelButton = itemView.findViewById<Button>(R.id.cancel_button)
		private val name = itemView.findViewById<TextView>(R.id.name)
		private val avatar = itemView.findViewById<AvatarImageView>(R.id.avatar)

		fun bindView(model: FriendshipModel) {
			avatar.setText(
				"${model.userModel.firstName?.get(
					0
				)}",
				((model.userModel.id ?: 0) % 255).toInt()
			)
			name.text = "${model.userModel.firstName} ${model.userModel.lastName}"
			when (model.friendshipStatus) {
				FriendshipStatus.PENDING -> {
					val sentByMe = thisUserId == model.lastUserActioned
					acceptButton.goneUnless(!sentByMe)
					declineButton.goneUnless(!sentByMe)
					cancelButton.goneUnless(sentByMe)
				}
				else -> { /*do nothing */
				}
			}
			acceptButton.setOnClickListener {
				changeStatus(model, FriendshipStatus.ACCEPTED)
			}
			declineButton.setOnClickListener {
				changeStatus(model, FriendshipStatus.NONE)
			}
			cancelButton.setOnClickListener {
				changeStatus(model, FriendshipStatus.NONE)
			}
		}
	}
}