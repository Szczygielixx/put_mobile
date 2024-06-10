package com.animation.myapplication2.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var visible by remember { mutableStateOf(true) }
    val scale = remember { Animatable(3.0f) }

    LaunchedEffect(Unit) {
        delay(100)
        scale.animateTo(
            targetValue = 0.01f,
            animationSpec = tween(durationMillis = 3000)
        )
        //delay(1000)
        visible = false
        onSplashFinished()
    }

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val radius = canvasWidth * scale.value / 2
                val centerX = canvasWidth / 2
                val centerY = canvasHeight / 2

                translate(left = centerX - radius, top = centerY - radius) {
                    drawRoundRect(
                        color = Color.Green,
                        size = Size(radius * 2, radius * 2),
                        cornerRadius = CornerRadius(radius, radius)
                    )
                }
            }
        }
    }
}