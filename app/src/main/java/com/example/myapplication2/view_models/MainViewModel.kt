package com.main_view_model.myapplication2.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<String>>(emptyList())
    val items: StateFlow<List<String>> = _items

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _items.value = listOf(
                "Szlak 1", "Szlak 2", "Szlak 3", "Szlak 4", "Szlak 5",
                "Szlak 6", "Szlak 7", "Szlak 8", "Szlak 9", "Szlak 10",
                "Szlak 11", "Szlak 12", "Szlak 13", "Szlak 14", "Szlak 15",
                "Szlak 16", "Szlak 17", "Szlak 18", "Szlak 19", "Szlak 20",
                "Szlak 21", "Szlak 22", "Szlak 23", "Szlak 24", "Szlak 25",
                "Szlak 26", "Szlak 27", "Szlak 28", "Szlak 29", "Szlak 30",
                "Szlak 31", "Szlak 32", "Szlak 33", "Szlak 34", "Szlak 35",
                "Szlak 36", "Szlak 37", "Szlak 38", "Szlak 39", "Szlak 40",
                "Szlak 41", "Szlak 42", "Szlak 43", "Szlak 44", "Szlak 45",
                "Szlak 46", "Szlak 47", "Szlak 48", "Szlak 49", "Szlak 50"
            )
        }
    }
}
