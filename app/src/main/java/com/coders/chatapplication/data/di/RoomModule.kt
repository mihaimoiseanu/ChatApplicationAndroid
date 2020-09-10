package com.coders.chatapplication.data.di

import android.content.Context
import androidx.room.Room
import com.coders.chatapplication.data.db.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(context, ChatDatabase::class.java, "chat-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(chatDatabase: ChatDatabase) = chatDatabase.userDao()

    @Provides
    @Singleton
    fun provideMessageDao(chatDatabase: ChatDatabase) = chatDatabase.messageDao()

    @Provides
    @Singleton
    fun provideRoomDao(chatDatabase: ChatDatabase) = chatDatabase.roomDao()

    @Provides
    @Singleton
    fun provideUserRoomDao(chatDatabase: ChatDatabase) = chatDatabase.userRoomDao()

    @Provides
    @Singleton
    fun provideFriendshipDao(chatDatabase: ChatDatabase) = chatDatabase.friendshipDao()
}