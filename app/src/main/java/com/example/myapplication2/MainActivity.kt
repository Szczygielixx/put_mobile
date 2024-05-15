package com.example.myapplication2

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import com.main_view_model.myapplication2.view_models.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Alignment
import androidx.compose.material.Text
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import com.main_view_model.myapplication2.view_models.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}
@Composable
fun ListItem(trail: Trail, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = trail.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    fontSize = 24.sp, // Increase font size
                    textAlign = TextAlign.Center, // Center the text
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                IconButton(
                    onClick = { onClick(trail.id) },
                    modifier = Modifier.size(48.dp) // Increase size of the IconButton
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "Details",
                        modifier = Modifier.size(36.dp) // Increase size of the Icon
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.poland_flag), // Replace with the appropriate image resource
                contentDescription = "Image for ${trail.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f) // Adjust aspect ratio as needed
            )
        }
    }
}

@Composable
fun MyComposeApp(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    val items by viewModel.items.collectAsState()
    ResponsiveList(data = items, onItemSelect = { trailId ->
        navController.navigate("details/$trailId")
    })

    FloatingActionButton(
        onClick = { /* Do something! */ },
        content = { Icon(Icons.Filled.Add, contentDescription = "Details") },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}

@Composable
fun ResponsiveList(data: List<Trail>, onItemSelect: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val columns = if (isPortrait) 1 else 2

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(data) { trail ->
            ListItem(trail = trail, onClick = { onItemSelect(trail.id) })
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            MyComposeApp(navController)
        }
        composable("details/{trailId}") { backStackEntry ->
            DetailsScreen(trailId = backStackEntry.arguments?.getString("trailId") ?: "No ID")
        }
    }
}



@Composable
fun DetailsScreen(trailId: String) {
    val viewModel: MainViewModel = viewModel()
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


