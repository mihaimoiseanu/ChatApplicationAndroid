package com.coders.chatapplication.data.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coders.chatapplication.commons.data.mapper.DomainMappable
import com.coders.chatapplication.domain.model.RoomModel

@Entity(tableName = "rooms")
data class RoomEntity(
	@PrimaryKey
	val id: Long,
	var name: String,
	@ColumnInfo(name = "is_private")
	var isPrivate: Boolean
) : DomainMappable<RoomModel> {
	override fun asDomain(): RoomModel {
		return RoomModel(id, name, isPrivate, null, null)
	}
}
