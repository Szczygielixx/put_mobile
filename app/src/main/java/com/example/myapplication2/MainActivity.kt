package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Spacer
import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val viewModel: MainViewModel = viewModel()
            //MyComposeApp(viewModel)
            AppNavigation()
        }
    }
}
/*
@Composable
fun openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivity(intent)
}
*/

@Composable
fun MyComposeApp(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    val items by viewModel.items.collectAsState()
    ResponsiveList(data = items, onItemSelect = { itemId ->
        navController.navigate("details/$itemId")
    })

    FloatingActionButton(
        onClick = { /* Do something! */ },
        content = { Icon(Icons.Filled.Add, contentDescription = "Details") },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}

@Composable
fun isPortrait(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
}
@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val smallestWidth = minOf(screenWidthDp, screenHeightDp)
    return smallestWidth >= 600.dp
}

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}



@Composable
fun ResponsiveList(data: List<String>, onItemSelect: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(data) { item ->
            ListItem(text = item, onClick = { onItemSelect(item) })
        }
    }
}

@Composable
fun ListItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onClick) {
                Icon(Icons.Filled.Info, contentDescription = "Details")
            }
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
        composable("details/{itemId}") { backStackEntry ->
            DetailsScreen(itemId = backStackEntry.arguments?.getString("itemId") ?: "No ID")
            //openCamera()
        }
    }
}
/*
@Composable
fun MyScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Do something! */ },
                content = { Icon(Icons.Filled.Camera, contentDescription = "Camera") },
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = false
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
        }
    }
}

@Composable
fun FloatingButton(){
    FloatingActionButton(
        onClick = { /* Do something! */ },
        content = { Icon(Icons.Filled.Camera, contentDescription = "Details") },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
    val onClick = { /* Do something */ }
}
*/
@Composable
fun DetailsScreen(itemId: String) {
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsed by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = System.nanoTime() - elapsed
            while (isRunning) {
                elapsed = System.nanoTime() - startTime
                delay(10L)
            }
        }
    }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Details for item $itemId")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Elapsed time: ${elapsed / 1_000_000_000} seconds and ${(elapsed / 1_000_000) % 1000} milliseconds")
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    isRunning = true
                },
                enabled = !isRunning
            ) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    isRunning = false
                }
            ) {
                Text("Stop")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    isRunning = false
                    elapsed = 0L
                }
            ) {
                Text("Reset")
            }
        }
    }
}

val customStyle = TextStyle(
    color = Color.Blue,
    fontSize = 18.sp,
    letterSpacing = 0.5.sp
)