package com.kabishan.dairyfreedining.coach_marks

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kabishan.dairyfreedining.coach_marks.CoachMarkUtils.buildPath
import com.kabishan.dairyfreedining.coach_marks.CoachMarkUtils.toPx

sealed interface Arrow {

    val width: Dp
    val height: Dp
    val bias: Float
    val startPadding: Dp
    val endPadding: Dp
    val topPadding: Dp
    val bottomPadding: Dp

    fun draw(size: Size, density: Density): Path

    data class Top(
        override val width: Dp = CoachMarkDefaults.Arrow.width,
        override val height: Dp = CoachMarkDefaults.Arrow.height,
        override val bias: Float = CoachMarkDefaults.Arrow.bias
    ) : Arrow {

        override val topPadding: Dp = height
        override val startPadding: Dp = 0.dp
        override val endPadding: Dp = 0.dp
        override val bottomPadding: Dp = 0.dp

        override fun draw(size: Size, density: Density): Path = buildPath {
            val widthPx = width.toPx(density)
            val heightPx = height.toPx(density)

            moveTo(size.width.times(bias) - widthPx.div(2), heightPx)
            lineTo(size.width.times(bias), 0f)
            lineTo(size.width.times(bias) + widthPx.div(2), heightPx)
        }
    }

    data class End(
        override val width: Dp = CoachMarkDefaults.Arrow.width,
        override val height: Dp = CoachMarkDefaults.Arrow.height,
        override val bias: Float = CoachMarkDefaults.Arrow.bias
    ) : Arrow {

        override val endPadding: Dp = width
        override val topPadding: Dp = 0.dp
        override val startPadding: Dp = 0.dp
        override val bottomPadding: Dp = 0.dp

        override fun draw(size: Size, density: Density): Path = buildPath {
            val widthPx = width.toPx(density)
            val heightPx = height.toPx(density)

            moveTo(size.width - widthPx, size.height.times(bias) - heightPx.div(2))
            lineTo(size.width, size.height.times(bias))
            lineTo(size.width - widthPx, size.height.times(bias) + heightPx.div(2))
        }
    }
}