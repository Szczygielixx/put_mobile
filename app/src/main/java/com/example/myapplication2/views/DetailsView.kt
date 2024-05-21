package com.details_view.myapplication2.views

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import com.main_view_model.myapplication2.view_models.MainViewModel
import com.main_view_model.myapplication2.view_models.StopwatchState
import kotlinx.coroutines.delay
import com.details_view_composables.myapplication2.views.LandscapeDetailsView
import com.details_view_composables.myapplication2.views.PortraitDetailsView

@Composable
fun DetailsView(trailId: String, viewModel: MainViewModel) {
    val trail = viewModel.items.collectAsState().value.find { it.id == trailId }
    val stopwatchState by viewModel.stopwatchState.collectAsState()
    val configuration = LocalConfiguration.current

    val currentStopwatchState = stopwatchState[trailId] ?: StopwatchState()
    var isRunning by remember { mutableStateOf(currentStopwatchState.isRunning) }
    var startTime by remember { mutableStateOf(currentStopwatchState.startTime) }
    var elapsed by remember { mutableStateOf(currentStopwatchState.elapsed) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = currentStopwatchState.startTime
            while (isRunning) {
                elapsed = System.nanoTime() - startTime
                viewModel.updateElapsed(trailId, elapsed)
                delay(10L)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.updateElapsed(trailId, elapsed)
        }
    }

    if (trail != null) {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            PortraitDetailsView(
                trail,
                elapsed,
                isRunning,
                currentStopwatchState.recordedTimes,
                onStart = {
                    isRunning = true
                    viewModel.startStopwatch(trailId)
                },
                onStop = {
                    isRunning = false
                    viewModel.stopStopwatch(trailId)
                },
                onReset = {
                    isRunning = false
                    elapsed = 0L
                    viewModel.resetStopwatch(trailId)
                },
                onSave = {
                    viewModel.saveCurrentTime(trailId)
                },
                onDelete = { index ->
                    viewModel.deleteTime(trailId, index)
                }
            )
        } else {
            LandscapeDetailsView(
                trail,
                elapsed,
                isRunning,
                currentStopwatchState.recordedTimes,
                onStart = {
                    isRunning = true
                    viewModel.startStopwatch(trailId)
                },
                onStop = {
                    isRunning = false
                    viewModel.stopStopwatch(trailId)
                },
                onReset = {
                    isRunning = false
                    elapsed = 0L
                    viewModel.resetStopwatch(trailId)
                },
                onSave = {
                    viewModel.saveCurrentTime(trailId)
                },
                onDelete = { index ->
                    viewModel.deleteTime(trailId, index)
                }
            )
        }
    }
}
