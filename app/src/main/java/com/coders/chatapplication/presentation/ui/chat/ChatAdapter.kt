package com.coders.chatapplication.presentation.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.domain.model.UserModel
import com.coders.chatapplication.presentation.commons.AsyncDiffUtil

class ChatAdapter(
	private val selfId: Long
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

	var users: List<UserModel> = listOf()

	private val diffUtil = AsyncDiffUtil(this, object : DiffUtil.ItemCallback<MessageModel>() {
		override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
			return oldItem == newItem
		}
	})

	val diffFinished: LiveData<Unit>
		get() = diffUtil.onDiffFinished

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val layoutId = if (viewType == MESSAGE_INCOME) {
			R.layout.item_message_income
		} else {
			R.layout.item_message_outcome
		}
		val view = layoutInflater.inflate(layoutId, parent, false)
		return ViewHolder(view)
	}

	override fun getItemViewType(position: Int): Int {
		return if (diffUtil.current()[position].sender == selfId) {
			MESSAGE_OUTCOME
		} else {
			MESSAGE_INCOME
		}
	}

	fun update(messages: MutableList<MessageModel>) {
		diffUtil.update(messages)
	}

	override fun getItemCount(): Int = diffUtil.current().size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindItem(diffUtil.current()[position])
	}

	override fun onViewRecycled(holder: ViewHolder) {
		holder.clearView()
		super.onViewRecycled(holder)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val text = itemView.findViewById<TextView>(R.id.text)
		private val author = itemView.findViewById<TextView>(R.id.author)

		fun bindItem(messageModel: MessageModel) {
			text.text = messageModel.message
			author.text = "${users.find { it.id == messageModel.sender }?.firstName}"
		}

		fun clearView() {
			itemView.findViewById<ImageView>(R.id.image).apply {
				this.setImageResource(0)
				this.visibility = View.GONE
			}
			itemView.findViewById<TextView>(R.id.text).text = ""
		}
	}

	companion object {
		const val MESSAGE_INCOME = 1
		const val MESSAGE_OUTCOME = 2
	}

}