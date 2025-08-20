package com.bieniucieniu.hometooling.feat.storage.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context: Context = get()
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}