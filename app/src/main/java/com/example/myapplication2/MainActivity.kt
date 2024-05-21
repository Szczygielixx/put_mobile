package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.animation.myapplication2.utils.SplashScreen
import com.details_view.myapplication2.views.DetailsView
import com.main_view.myapplication2.views.MainView
import com.main_view_model.myapplication2.view_models.MainViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var splashFinished by remember { mutableStateOf(false) }

            if (splashFinished) {
                val navController = rememberNavController()
                AppNavigation(navController = navController, viewModel = MainViewModel())
            } else {
                SplashScreen(onSplashFinished = { splashFinished = true })
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            MainView(navController, viewModel)
        }
        composable("details/{trailId}") { backStackEntry ->
            val trailId = backStackEntry.arguments?.getString("trailId") ?: "No ID"
            DetailsView(trailId, viewModel)
        }
    }
}