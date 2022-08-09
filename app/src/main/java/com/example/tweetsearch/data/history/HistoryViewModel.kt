package com.example.tweetsearch.data.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : ViewModel() {
    val historyList: MutableStateFlow<List<History>?>
    private val historyRepository: HistoryRepository

    init {
        val historyDao = HistoryRoomDatabase.getInstance(application).historyDao()
        historyRepository = HistoryRepository(historyDao)
        historyList = historyRepository.historyList
    }

    fun getHistory(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.getHistory(query)
        }
    }

    fun insertHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.insertHistory(history)
        }
    }

    fun deleteHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteHistory(history)
        }
    }
}

class HistoryViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}