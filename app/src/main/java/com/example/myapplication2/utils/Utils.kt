package com.utils.myapplication2.utils

fun formatElapsedTime(elapsedNanos: Long): String {
    val totalSeconds = elapsedNanos / 1_000_000_000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> "%d h %02d min %02d s".format(hours, minutes, seconds)
        minutes > 0 -> "%d min %02d s".format(minutes, seconds)
        else -> "%d s".format(seconds)
    }
}
