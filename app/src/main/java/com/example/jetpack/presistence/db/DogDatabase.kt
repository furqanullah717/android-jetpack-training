package com.example.jetpack.presistence.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jetpack.model.DogBreed
import java.util.concurrent.locks.Lock

@Database(entities = [DogBreed::class], version = 2)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao

    companion object {
        @Volatile
        private var instance: DogDatabase? = null
        private val LOCK = Any()
        operator fun invoke(ctx: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(ctx).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java, "dogDatabase"
        ).build()
    }
}