package com.bieniucieniu.hometooling.feat.storage.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class KV(
    @PrimaryKey() val key: String,
    val value: String,
)

@Dao
interface KVDao {
    @Insert
    suspend fun insert(item: KV)

    @Query("SELECT count(*) FROM KV")
    suspend fun count(): Int

    @Query("SELECT * FROM KV")
    suspend fun selectAll(): List<KV>

    @Query("SELECT value FROM KV WHERE `key` = :key")
    suspend fun select(key: String): String?

    @Query("DELETE FROM KV WHERE `key` = :key")
    suspend fun delete(key: String)
}

suspend fun KVDao.insert(key: String, value: String) = insert(KV(key, value))
