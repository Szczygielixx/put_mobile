package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.details_view.myapplication2.views.DetailsView
import com.main_view.myapplication2.views.MainView
import com.main_view_model.myapplication2.view_models.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel()

            AppNavigation(navController, viewModel)
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