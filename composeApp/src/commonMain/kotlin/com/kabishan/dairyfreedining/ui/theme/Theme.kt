package com.kabishan.dairyfreedining.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DairyFreeDiningTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography(),
        content = content
    )
}

object DairyFreeDiningTheme {
    val color @Composable
    get() = MaterialTheme.colorScheme

    val typography @Composable
    get() = MaterialTheme.typography
}