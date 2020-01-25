package com.coders.chatapplication.presentation.ui.chat

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.domain.model.MessageModel
import com.coders.chatapplication.presentation.commons.AsyncDiffUtil
import kotlinx.coroutines.launch

class ChatDiffUtil(
	adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
	itemCallback: DiffUtil.ItemCallback<MessageModel>
) : AsyncDiffUtil<MessageModel>(adapter, itemCallback) {

	fun insertMessage(messageModel: MessageModel) {
		launch {
			val newList = mutableListOf<MessageModel>()
			if (list == null) {
				insert(listOf(messageModel))
				return@launch
			}
			newList.addAll(list as List<MessageModel>)
			newList.add(messageModel)
			newList.sortBy { it.sentAt }
			update(newList)
		}
	}
}