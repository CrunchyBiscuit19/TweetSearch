package com.example.tweetsearch.data.history

import kotlinx.coroutines.flow.MutableStateFlow

class HistoryRepository(private val historyDao: HistoryDao) {
    val historyList: MutableStateFlow<List<History>?> = MutableStateFlow(null)

    fun getHistory(query: String) {
        if (query == "") {
            historyList.value = historyDao.getFullHistory()
        } else {
            historyList.value = historyDao.getQueryHistory(query)
        }
    }

    suspend fun insertHistory(history: History) {
        historyDao.insert(history)
    }

    suspend fun deleteHistory(history: History) {
        historyDao.delete(history)
    }
}