package com.kabishan.dairyfreedining.coach_marks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object CoachMarkDefaults {
    const val DELAY_TO_SHOW_COACH_MARK_IN_SECONDS = 500L

    val cornerRadius: Dp get() = 8.dp
    val shadowElevation: Dp get() = 2.dp
    val padding: PaddingValues get() = PaddingValues(horizontal = 8.dp, vertical = 6.dp)

    object Arrow {
        val height: Dp get() = 12.dp
        val width: Dp get() = 16.dp
        val bias: Float get() = 0.5f
    }
}