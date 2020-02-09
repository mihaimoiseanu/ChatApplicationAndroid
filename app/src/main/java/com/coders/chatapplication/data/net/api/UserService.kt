package com.coders.chatapplication.data.net.api

import com.coders.chatapplication.data.net.models.FriendshipResponse
import com.coders.chatapplication.data.net.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

	@GET("users/friends")
	suspend fun getFriends(): List<UserResponse>

	@GET("users")
	suspend fun getUsers(@Query("search_term") searchTerm: String? = null): List<UserResponse>

	@GET("users/friendships")
	suspend fun getFriendships(@Query("status") status: FriendshipResponse.Status? = null): List<FriendshipResponse>

	@GET("users/friendships/{other_user_id}")
	suspend fun getFriendship(@Path("other_user_id") otherUserId: Long): FriendshipResponse

	@POST("users/friendships")
	suspend fun requestFriendship(@Body friendshipResponse: FriendshipResponse): FriendshipResponse

	@PUT("users/friendships")
	suspend fun updateFriendship(@Body friendshipResponse: FriendshipResponse): FriendshipResponse

	@DELETE("users/friendships")
	suspend fun deleteFriendships(@Query("other_user_id") otherUserId: Long)

}