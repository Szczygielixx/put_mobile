package com.details_view.myapplication2.views

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import com.main_view_model.myapplication2.view_models.MainViewModel
import com.main_view_model.myapplication2.view_models.StopwatchState
import kotlinx.coroutines.delay
import com.details_view_composables.myapplication2.views.LandscapeDetailsView
import com.details_view_composables.myapplication2.views.PortraitDetailsView
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.padding



@Composable
fun DetailsView(trailId: String, viewModel: MainViewModel) {
    val trail = viewModel.items.collectAsState().value.find { it.id == trailId }
    val stopwatchState by viewModel.stopwatchState.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val configuration = LocalConfiguration.current

    val currentStopwatchState = stopwatchState[trailId] ?: StopwatchState()
    var isRunning by remember { mutableStateOf(currentStopwatchState.isRunning) }
    var startTime by remember { mutableStateOf(currentStopwatchState.startTime) }
    var elapsed by remember { mutableStateOf(currentStopwatchState.elapsed) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            viewModel.addPhoto(trailId, bitmap)
        }
    }

    val checkPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            launcher.launch(null)
        } else {
            // Handle permission denial here
        }
    }

    fun openCamera() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                launcher.launch(null)
            }
            else -> {
                checkPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

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
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { openCamera() }) {
                    Icon(Icons.Default.Camera, contentDescription = "Open Camera")
                }
            }
        ) { contentPadding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)) {
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    PortraitDetailsView(
                        trail,
                        elapsed,
                        isRunning,
                        currentStopwatchState.recordedTimes,
                        photos[trailId].orEmpty(),
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
                        },
                        onDeletePhoto = { index ->
                            viewModel.deletePhoto(trailId, index)
                        }
                    )
                } else {
                    LandscapeDetailsView(
                        trail,
                        elapsed,
                        isRunning,
                        currentStopwatchState.recordedTimes,
                        photos[trailId].orEmpty(),
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
                        },
                        onDeletePhoto = { index ->
                            viewModel.deletePhoto(trailId, index)
                        }
                    )
                }
            }
        }
    }
}
