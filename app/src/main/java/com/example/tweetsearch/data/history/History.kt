package com.example.tweetsearch.data.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "full_path")
    val fullPath: String,
    @ColumnInfo(name = "short_path")
    val shortPath: String,
    @ColumnInfo(name = "accessed_time")
    val accessedTime: LocalDateTime
) {
    companion object {
        fun shortPathFromFull(fullPath: String): String {
            if (!fullPath.contains("/")) {
                return fullPath
            }
            return fullPath.split("/").last()
        }
    }
}