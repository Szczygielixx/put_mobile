package com.splash_view_model.myapplication2.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    var splashFinished by mutableStateOf(false)
}
