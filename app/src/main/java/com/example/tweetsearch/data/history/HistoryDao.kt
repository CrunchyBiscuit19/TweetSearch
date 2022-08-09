package com.example.tweetsearch.data.history

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY accessed_time DESC")
    fun getFullHistory(): List<History>

    @Query("SELECT * FROM history WHERE full_path LIKE '%' || :query || '%'")
    fun getQueryHistory(query: String): List<History>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)
}