package com.details_view.myapplication2.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Text(
                text = trail.name,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = trail.imageResId),
                contentDescription = "Image for ${trail.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f) // Adjust aspect ratio as needed
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Elapsed time: ${elapsed / 1_000_000_000} seconds and ${(elapsed / 1_000_000) % 1000} milliseconds",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row {
                Button(
                    onClick = {
                        isRunning = true
                        viewModel.startStopwatch(trailId)
                    },
                    enabled = !isRunning,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Start")
                }
                Button(
                    onClick = {
                        isRunning = false
                        viewModel.stopStopwatch(trailId)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Stop")
                }
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
}
