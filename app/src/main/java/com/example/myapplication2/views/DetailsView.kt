package com.details_view.myapplication2.views


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.main_view_model.myapplication2.view_models.MainViewModel
import com.main_view_model.myapplication2.view_models.StopwatchState
import com.trail_model.myapplication2.models.Trail
import com.utils.myapplication2.utils.formatElapsedTime
import kotlinx.coroutines.delay


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

@Composable
fun PortraitDetailsView(
    trail: Trail,
    elapsed: Long,
    isRunning: Boolean,
    recordedTimes: List<Long>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit,
    onDelete: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = trail.details,
            fontSize = 18.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = formatElapsedTime(elapsed),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onStart,
                enabled = !isRunning,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Start")
            }
            Button(
                onClick = onStop,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Stop")
            }
            Button(
                onClick = onReset,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Reset")
            }
            Button(
                onClick = onSave
            ) {
                Text("Zapisz")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            itemsIndexed(recordedTimes) { index, time ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${index + 1}. ${formatElapsedTime(time)}",
                        fontSize = 18.sp
                    )
                    IconButton(onClick = { onDelete(index) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun LandscapeDetailsView(
    trail: Trail,
    elapsed: Long,
    isRunning: Boolean,
    recordedTimes: List<Long>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit,
    onDelete: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = trail.imageResId),
            contentDescription = "Image for ${trail.name}",
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.0f) // Adjust aspect ratio as needed
                .padding(end = 16.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = trail.name,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = trail.details,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = formatElapsedTime(elapsed),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onStart,
                    enabled = !isRunning,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Start")
                }
                Button(
                    onClick = onStop,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Stop")
                }
                Button(
                    onClick = onReset,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = onSave
                ) {
                    Text("Zapisz")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                itemsIndexed(recordedTimes) { index, time ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${index + 1}. ${formatElapsedTime(time)}",
                            fontSize = 18.sp
                        )
                        IconButton(onClick = { onDelete(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
