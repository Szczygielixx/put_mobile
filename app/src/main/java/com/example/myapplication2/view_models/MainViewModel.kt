package com.main_view_model.myapplication2.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.R
import com.trail_model.myapplication2.models.Trail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StopwatchState(
    val isRunning: Boolean = false,
    val startTime: Long = 0L,
    val elapsed: Long = 0L,
    val recordedTimes: List<Long> = emptyList()
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
            _items.value = listOf(
                Trail(
                    id = "1",
                    name = "Szlak Orlich Gniazd",
                    details = "Szlak Orlich Gniazd to jeden z najbardziej malowniczych i popularnych szlaków turystycznych w Polsce, prowadzący przez ruiny średniowiecznych zamków.",
                    imageResId = R.drawable.orle_gniazdo
                ),
                Trail(
                    id = "2",
                    name = "Główny Szlak Beskidzki",
                    details = "Główny Szlak Beskidzki to najdłuższy szlak pieszy w polskich górach, biegnący przez najpiękniejsze partie Beskidów od Ustronia w Beskidzie Śląskim po Wołosate w Bieszczadach.",
                    imageResId = R.drawable.szlak_beskidzki
                ),
                Trail(
                    id = "3",
                    name = "Szlak Piastowski",
                    details = "Szlak Piastowski to trasa turystyczna prowadząca przez miejsca związane z początkami państwa polskiego, w tym Gniezno, Poznań, Kruszwicę i Biskupin.",
                    imageResId = R.drawable.piastowski
                ),
                Trail(
                    id = "4",
                    name = "Główny Szlak Sudecki",
                    details = "Główny Szlak Sudecki to szlak turystyczny przebiegający przez całe Sudety od Świeradowa-Zdroju do Prudnika, obejmujący najwyższe partie tych gór.",
                    imageResId = R.drawable.sudecki
                ),
                Trail(
                    id = "5",
                    name = "Szlak Nadmorski",
                    details = "Szlak Nadmorski to trasa turystyczna biegnąca wzdłuż polskiego wybrzeża Bałtyku, oferująca piękne widoki na morze, plaże i nadmorskie klify.",
                    imageResId = R.drawable.szlak_nadmorski
                ),
                Trail(
                    id = "6",
                    name = "Szlak Zamków Gotyckich",
                    details = "Szlak Zamków Gotyckich prowadzi przez północną Polskę, umożliwiając zwiedzanie imponujących zamków gotyckich, w tym Malborku, Lidzbarka Warmińskiego i Gniewu.",
                    imageResId = R.drawable.szlak_beskidzki
                ),
                Trail(
                    id = "7",
                    name = "Szlak Kopernikowski",
                    details = "Szlak Kopernikowski to trasa turystyczna prowadząca przez miejsca związane z Mikołajem Kopernikiem, w tym Toruń, Frombork i Olsztyn.",
                    imageResId = R.drawable.piastowski
                ),
                Trail(
                    id = "8",
                    name = "Szlak Karpacki",
                    details = "Szlak Karpacki to trasa biegnąca przez polskie Karpaty, oferująca piękne widoki na góry, doliny i malownicze wioski.",
                    imageResId = R.drawable.orle_gniazdo
                ),
                Trail(
                    id = "9",
                    name = "Szlak Wokół Tatr",
                    details = "Szlak Wokół Tatr to trasa rowerowa i piesza, która pozwala na zwiedzanie okolic Tatr zarówno po stronie polskiej, jak i słowackiej.",
                    imageResId = R.drawable.szlak_nadmorski
                ),
                Trail(
                    id = "10",
                    name = "Szlak Papieski",
                    details = "Szlak Papieski prowadzi przez miejsca związane z życiem i działalnością Jana Pawła II, w tym Wadowice, Kraków i Kalwarię Zebrzydowską.",
                    imageResId = R.drawable.sudecki
                ),
                Trail(
                    id = "11",
                    name = "Szlak Wielkich Jezior Mazurskich",
                    details = "Szlak Wielkich Jezior Mazurskich to popularna trasa żeglarska prowadząca przez najpiękniejsze jeziora Mazur, idealna dla miłośników wodnych przygód.",
                    imageResId = R.drawable.piastowski
                ),
                Trail(
                    id = "12",
                    name = "Szlak Rzeki Biebrzy",
                    details = "Szlak Rzeki Biebrzy to trasa turystyczna biegnąca wzdłuż rzeki Biebrzy, oferująca wyjątkowe widoki na unikalne bagna i rozlewiska.",
                    imageResId = R.drawable.szlak_beskidzki
                ),
                Trail(
                    id = "13",
                    name = "Szlak Krutyni",
                    details = "Szlak Krutyni to popularna trasa kajakowa prowadząca przez malownicze tereny Mazur, idealna dla miłośników przyrody i aktywnego wypoczynku.",
                    imageResId = R.drawable.orle_gniazdo
                ),
                Trail(
                    id = "14",
                    name = "Szlak Roztoczański",
                    details = "Szlak Roztoczański to trasa biegnąca przez Roztocze, oferująca piękne widoki na pagórkowaty krajobraz, lasy i doliny.",
                    imageResId = R.drawable.sudecki
                ),
                Trail(
                    id = "15",
                    name = "Szlak Gór Świętokrzyskich",
                    details = "Szlak Gór Świętokrzyskich to trasa prowadząca przez najstarsze góry w Polsce, oferująca piękne widoki i możliwość zwiedzania licznych zabytków.",
                    imageResId = R.drawable.piastowski
                )
            )
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
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(isRunning = false, startTime = 0L, elapsed = 0L)
        }
    }

    fun updateElapsed(trailId: String, elapsed: Long) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(elapsed = elapsed)
        }
    }

    fun saveCurrentTime(trailId: String) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(
                recordedTimes = current.recordedTimes + current.elapsed
            )
        }
    }

    fun deleteTime(trailId: String, index: Int) {
        _stopwatchState.value = _stopwatchState.value.toMutableMap().apply {
            val current = this[trailId] ?: StopwatchState()
            this[trailId] = current.copy(
                recordedTimes = current.recordedTimes.toMutableList().apply { removeAt(index) }
            )
        }
    }
}

