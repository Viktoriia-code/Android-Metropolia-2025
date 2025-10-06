package com.example.assignment4_retrofit_network

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val repository = WikiRepository()
    var wikiUiState by mutableStateOf<Int?>(null)
        private set

    fun clearHits() {
        wikiUiState = null
    }

    fun getHits(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val serverResp = repository.hitCountCheck(name)
                wikiUiState = serverResp.query.searchinfo.totalhits
            } catch (e: Exception) {
                wikiUiState = -1
            }
        }
    }
}
