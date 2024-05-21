package com.details_view_composables.myapplication2.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trail_model.myapplication2.models.Trail
import com.utils.myapplication2.utils.formatElapsedTime
import android.graphics.Bitmap
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.asImageBitmap



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
        TrailDetailsHeader(trail)
        StopwatchDisplay(elapsed)
        StopwatchControls(isRunning, onStart, onStop, onReset, onSave)
        Spacer(modifier = Modifier.height(16.dp))
        RecordedTimesList(recordedTimes, onDelete)
        Spacer(modifier = Modifier.height(16.dp))
        PhotosSection(photos, onDeletePhoto)
    }
}

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
            StopwatchDisplay(elapsed)
            StopwatchControls(isRunning, onStart, onStop, onReset, onSave)
            Spacer(modifier = Modifier.height(16.dp))
            RecordedTimesList(recordedTimes, onDelete)
            Spacer(modifier = Modifier.height(16.dp))
            PhotosSection(photos, onDeletePhoto)
        }
    }
}

@Composable
fun TrailDetailsHeader(trail: Trail) {
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

@Composable
fun PhotosSection(photos: List<Bitmap>, onDeletePhoto: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Zdjęcia ze szlaku",
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyColumn {
            itemsIndexed(photos) { index, photo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        bitmap = photo.asImageBitmap(),
                        contentDescription = "Trail photo",
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                            .padding(end = 8.dp)
                    )
                    IconButton(onClick = { onDeletePhoto(index) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
