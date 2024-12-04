package com.kabishan.dairyfreedining.coach_marks

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

object CoachMarkUtils {
    fun Dp.toPx(density: Density) = with(density) { toPx() }

    fun buildPath(block: Path.() -> Unit): Path {
        return Path().apply {
            block()
            close()
        }
    }
}