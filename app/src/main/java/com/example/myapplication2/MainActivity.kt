package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.animation.myapplication2.utils.SplashScreen
import com.details_view.myapplication2.views.DetailsView
import com.main_view.myapplication2.views.MainView
import com.main_view_model.myapplication2.view_models.MainViewModel
import com.splash_view_model.myapplication2.view_models.SplashViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Box(modifier = Modifier.fillMaxSize()) {
                AppNavigation(navController = navController, viewModel = MainViewModel())
                if (!splashViewModel.splashFinished) {
                    SplashScreen(onSplashFinished = { splashViewModel.splashFinished = true })
                }
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