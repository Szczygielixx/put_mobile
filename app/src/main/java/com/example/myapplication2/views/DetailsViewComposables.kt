package com.details_view_composables.myapplication2.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.google.accompanist.pager.rememberPagerState
import com.trail_model.myapplication2.models.Trail
import com.utils.myapplication2.utils.formatElapsedTime

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PortraitDetailsView(
    trail: Trail,
    elapsed: Long,
    isRunning: Boolean,
    recordedTimes: List<Long>,
    photos: List<Bitmap>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit,
    onDelete: (Int) -> Unit,
    onDeletePhoto: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageSlider(trail.imageResId, photos, onDeletePhoto)
        TrailDetails(trail)
        StopwatchDisplay(elapsed)
        StopwatchControls(isRunning, onStart, onStop, onReset, onSave)
        Spacer(modifier = Modifier.height(16.dp))
        RecordedTimesList(recordedTimes, onDelete)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LandscapeDetailsView(
    trail: Trail,
    elapsed: Long,
    isRunning: Boolean,
    recordedTimes: List<Long>,
    photos: List<Bitmap>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit,
    onDelete: (Int) -> Unit,
    onDeletePhoto: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ImageSlider(trail.imageResId, photos, onDeletePhoto, Modifier.weight(1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TrailDetails(trail)
            StopwatchDisplay(elapsed)
            StopwatchControls(isRunning, onStart, onStop, onReset, onSave)
            Spacer(modifier = Modifier.height(16.dp))
            RecordedTimesList(recordedTimes, onDelete)
        }
    }
}

@Composable
fun TrailDetails(trail: Trail) {
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
}

@Composable
fun StopwatchDisplay(elapsed: Long) {
    Text(
        text = formatElapsedTime(elapsed),
        fontSize = 48.sp, // Increase font size here
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun StopwatchControls(
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onStart,
            enabled = !isRunning,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start",
                tint = Color.Green,
                modifier = Modifier.size(36.dp)
            )
        }
        IconButton(
            onClick = onStop,
            enabled = isRunning,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop",
                tint = Color.Red,
                modifier = Modifier.size(36.dp)
            )
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
}

@Composable
fun RecordedTimesList(recordedTimes: List<Long>, onDelete: (Int) -> Unit) {
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(
    initialImageResId: Int,
    photos: List<Bitmap>,
    onDeletePhoto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val imageCount = photos.size + 1

    Column(modifier = modifier) {
        HorizontalPager(count = imageCount, state = pagerState) { page ->
            if (page == 0) {
                Image(
                    painter = painterResource(id = initialImageResId),
                    contentDescription = "Initial Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                )
            } else {
                val photoIndex = page - 1
                Box {
                    Image(
                        bitmap = photos[photoIndex].asImageBitmap(),
                        contentDescription = "User Photo $photoIndex",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                    )
                    IconButton(
                        onClick = { onDeletePhoto(photoIndex) },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Photo")
                    }
                }
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

