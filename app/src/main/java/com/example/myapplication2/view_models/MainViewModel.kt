package com.main_view_model.myapplication2.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Trail(val id: String, val name: String, val details: String)

data class StopwatchState(
    val isRunning: Boolean = false,
    val startTime: Long = 0L,
    val elapsed: Long = 0L
)

class MainViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Trail>>(emptyList())
    val items: StateFlow<List<Trail>> = _items

    private val _stopwatchState = MutableStateFlow<Map<String, StopwatchState>>(emptyMap())
    val stopwatchState: StateFlow<Map<String, StopwatchState>> = _stopwatchState

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _items.value = (1..50).map { i ->
                Trail(
                    id = i.toString(),
                    name = "Szlak $i",
                    details = "Opis szlaku $i"
                )
            }
        }
    }

    fun startStopwatch(trailId: String) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(isRunning = true, startTime = System.nanoTime() - current.elapsed)
        }
    }

    fun stopStopwatch(trailId: String) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(isRunning = false, elapsed = System.nanoTime() - current.startTime)
        }
    }

    fun resetStopwatch(trailId: String) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            this[trailId] = StopwatchState()
        }
    }

    fun updateElapsed(trailId: String, elapsed: Long) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(elapsed = elapsed)
        }
    }
}

