package com.example.jetpack.presistence.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jetpack.model.DogBreed
import java.util.*

@Dao
interface DogDao {

    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("SELECT * FROM DOGBREED")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("DELETE from dogbreed")
    suspend fun deleteAll()

    @Query("Select * from dogbreed where id = :id")
    suspend fun getDogById(id: Long): DogBreed?
}