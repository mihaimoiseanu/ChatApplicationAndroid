package com.coders.chatapplication.data.net.models

import com.coders.chatapplication.commons.data.mapper.DomainMappable
import com.coders.chatapplication.domain.model.UserModel

data class UserResponse(
	val id:Long?,
	val email:String?,
	val firstName:String? = null,
	val lastName:String? = null
) : DomainMappable<UserModel>{
	override fun asDomain(): UserModel {
		return UserModel(id, email, firstName, lastName)
	}
}