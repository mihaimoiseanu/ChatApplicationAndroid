package com.coders.chatapplication.data.di

import com.coders.chatapplication.data.db.friendships.FriendshipDao
import com.coders.chatapplication.data.db.messages.MessageDao
import com.coders.chatapplication.data.db.room.RoomDao
import com.coders.chatapplication.data.db.room.UserRoomDao
import com.coders.chatapplication.data.db.user.UserDao
import com.coders.chatapplication.data.net.api.AuthService
import com.coders.chatapplication.data.net.api.RoomsService
import com.coders.chatapplication.data.net.api.UserService
import com.coders.chatapplication.data.net.interceptors.AuthInterceptor
import com.coders.chatapplication.data.net.socket.ChatManagerImpl
import com.coders.chatapplication.data.repository.auth.AuthRepositoryImpl
import com.coders.chatapplication.data.repository.friendship.FriendshipRepositoryImpl
import com.coders.chatapplication.data.repository.message.MessageRepositoryImpl
import com.coders.chatapplication.data.repository.rooms.RoomRepositoryImpl
import com.coders.chatapplication.data.repository.users.UserRepositoryImpl
import com.coders.chatapplication.data.sharedprefs.SharedPrefs
import com.coders.chatapplication.domain.repository.*
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideChatManager(
        okHttpClient: OkHttpClient,
        gson: Gson,
        sharedPrefs: SharedPrefs,
        messageRepository: MessageRepository,
        roomRepository: RoomRepository,
        friendshipRepository: FriendshipRepository
    ): ChatManager {
        return ChatManagerImpl(
            okHttpClient,
            gson,
            sharedPrefs,
            messageRepository,
            roomRepository,
            friendshipRepository
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        authInterceptor: AuthInterceptor,
        chatManager: ChatManager,
        sharedPrefs: SharedPrefs
    ): AuthRepository {
        return AuthRepositoryImpl(authService, authInterceptor, chatManager, sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideRoomRepository(
        roomsService: RoomsService,
        roomDao: RoomDao,
        userRoomDao: UserRoomDao,
        userDao: UserDao,
        messageRepository: MessageRepository
    ): RoomRepository {
        return RoomRepositoryImpl(roomsService, roomDao, userRoomDao, userDao, messageRepository)
    }

    @Provides
    @Singleton
    fun provideFriendshipRepository(
        friendshipDao: FriendshipDao,
        userService: UserService,
        userDao: UserDao
    ): FriendshipRepository =
        FriendshipRepositoryImpl(friendshipDao, userService, userDao)

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository =
        UserRepositoryImpl(userService)

    @Provides
    @Singleton
    fun providesMessageRepository(
        roomsService: RoomsService,
        messageDao: MessageDao
    ): MessageRepository = MessageRepositoryImpl(roomsService, messageDao)
}