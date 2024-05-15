package com.details_view.myapplication2.views


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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

    if (trail != null) {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            PortraitDetailsView(trail, elapsed, isRunning, onStart = {
                isRunning = true
                viewModel.startStopwatch(trailId)
            }, onStop = {
                isRunning = false
                viewModel.stopStopwatch(trailId)
            }, onReset = {
                isRunning = false
                elapsed = 0L
                viewModel.resetStopwatch(trailId)
            })
        } else {
            LandscapeDetailsView(trail, elapsed, isRunning, onStart = {
                isRunning = true
                viewModel.startStopwatch(trailId)
            }, onStop = {
                isRunning = false
                viewModel.stopStopwatch(trailId)
            }, onReset = {
                isRunning = false
                elapsed = 0L
                viewModel.resetStopwatch(trailId)
            })
        }
    }
}

@Composable
fun PortraitDetailsView(
    trail: Trail,
    elapsed: Long,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
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
                onClick = onReset
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun LandscapeDetailsView(
    trail: Trail,
    elapsed: Long,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
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
                    onClick = onReset
                ) {
                    Text("Reset")
                }
            }
        }
    }
}
