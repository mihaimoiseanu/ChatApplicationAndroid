package com.coders.chatapplication.data.net.models

import com.coders.chatapplication.commons.data.mapper.DomainMappable
import com.coders.chatapplication.domain.model.MessageModel

data class MessageResponse(
	val id: Long? = null,
	val message: String? = null,
	val senderId: Long? = null,
	val roomId: Long? = null,
	val sentAt: Long? = null
):DomainMappable<MessageModel>{
	override fun asDomain(): MessageModel {
		return MessageModel(
			id, message!!, senderId!!, sentAt
		)
	}
}