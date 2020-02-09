package com.coders.chatapplication.presentation.ui.searchfriends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.presentation.commons.AsyncDiffUtil
import com.coders.chatapplication.presentation.ui.views.AvatarImageView

class SearchFriendsAdapter : RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder>() {

	private val diffUtil = AsyncDiffUtil(this, object : DiffUtil.ItemCallback<UserModel>() {
		override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
			return oldItem == newItem
		}
	})

	fun update(searchedUsers: List<UserModel>) {
		diffUtil.update(searchedUsers.toMutableList())
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
		)
	}

	override fun getItemCount(): Int = diffUtil.current().size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindView(diffUtil.current()[position])
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		fun bindView(userModel: UserModel) {
			itemView.apply {
				findViewById<AvatarImageView>(R.id.avatar).setText("${userModel.firstName?.get(0)}")
				findViewById<TextView>(R.id.name).text =
					"${userModel.firstName} ${userModel.lastName}"
			}
			itemView.setOnClickListener {

			}
		}
	}
}