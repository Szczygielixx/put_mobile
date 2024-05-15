package com.details_view.myapplication2.views


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.main_view_model.myapplication2.view_models.MainViewModel
import com.main_view_model.myapplication2.view_models.StopwatchState
import kotlinx.coroutines.delay

@Composable
fun DetailsView(trailId: String, viewModel: MainViewModel) {
    val trail = viewModel.items.collectAsState().value.find { it.id == trailId }
    val stopwatchState by viewModel.stopwatchState.collectAsState()

    val currentStopwatchState = stopwatchState[trailId] ?: StopwatchState()
    var isRunning by remember { mutableStateOf(currentStopwatchState.isRunning) }
    var startTime by remember { mutableStateOf(currentStopwatchState.startTime) }
    var elapsed by remember { mutableStateOf(currentStopwatchState.elapsed) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = System.nanoTime() - elapsed
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

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (trail != null) {
            Text("Details for ${trail.name}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(trail.details)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Elapsed time: ${elapsed / 1_000_000_000} seconds and ${(elapsed / 1_000_000) % 1000} milliseconds")
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    isRunning = true
                    viewModel.startStopwatch(trailId)
                },
                enabled = !isRunning
            ) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    isRunning = false
                    viewModel.stopStopwatch(trailId)
                }
            ) {
                Text("Stop")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    isRunning = false
                    elapsed = 0L
                    viewModel.resetStopwatch(trailId)
                }
            ) {
                Text("Reset")
            }
        }
    }
}
