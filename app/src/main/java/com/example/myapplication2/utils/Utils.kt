package com.utils.myapplication2.utils


fun formatElapsedTime(elapsedNanos: Long): String {
    val totalSeconds = elapsedNanos / 1_000_000_000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}
