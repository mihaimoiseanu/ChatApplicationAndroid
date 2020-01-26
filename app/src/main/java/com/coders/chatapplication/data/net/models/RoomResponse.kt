package com.coders.chatapplication.data.net.models

import com.coders.chatapplication.commons.data.mapper.DomainMappable
import com.coders.chatapplication.domain.model.RoomModel

data class RoomResponse(
	val id: Long? = null,
	val name: String,
	val isPrivate: Boolean,
	val createdBy: Long? = null,
	val users: List<Long>? = null
) : DomainMappable<RoomModel> {
	override fun asDomain(): RoomModel {
		return RoomModel(id, name, isPrivate, createdBy, users)
	}
}