package com.main_view.myapplication2.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import com.main_view_model.myapplication2.view_models.MainViewModel
import com.trail_model.myapplication2.models.Trail



@Composable
fun MainView(navController: NavController, viewModel: MainViewModel) {
    val items by viewModel.items.collectAsState()
    ResponsiveList(data = items, onItemSelect = { trailId ->
        navController.navigate("details/$trailId")
    })

    FloatingActionButton(
        onClick = { /* Do something! */ },
        content = { Icon(Icons.Default.Add, contentDescription = "Details") },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}

@Composable
fun ResponsiveList(data: List<Trail>, onItemSelect: (String) -> Unit) {
    val columns = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2

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
fun ListItem(trail: Trail, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        //elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
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
                    modifier = Modifier.weight(1f),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    onClick = { onClick(trail.id) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Details",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = trail.imageResId),
                contentDescription = "Image for ${trail.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
            )
        }
    }
}


