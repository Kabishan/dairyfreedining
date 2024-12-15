package com.kabishan.dairyfreedining.coach_marks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.kabishan.dairyfreedining.coach_marks.CoachMarkUtils.toPx
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme

@Composable
fun CoachMarkToolTip(
    text: String,
    arrow: Arrow,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = CoachMarkDefaults.cornerRadius,
    padding: PaddingValues = CoachMarkDefaults.padding,
    shadowElevation: Dp = CoachMarkDefaults.shadowElevation,
    bgColor: Color = DairyFreeDiningTheme.color.primaryContainer
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .clip(coachMarkShape(arrow, density, cornerRadius))
            .graphicsLayer { this.shadowElevation = shadowElevation.toPx(density) }
            .background(bgColor)
            .padding(
                start = arrow.padding.start,
                end = arrow.padding.end,
                top = arrow.padding.top,
                bottom = arrow.padding.bottom
            )
            .padding(padding)
            .then(modifier)
    ) {
        Text(text = text)
    }
}

private fun coachMarkShape(
    arrow: Arrow,
    density: Density,
    radius: Dp
): GenericShape {
    return GenericShape { size, _ ->
        addRoundRect(
            RoundRect(
                left = arrow.padding.start.toPx(density),
                right = size.width - arrow.padding.end.toPx(density),
                top = arrow.padding.top.toPx(density),
                bottom = size.height - arrow.padding.bottom.toPx(density),
                cornerRadius = CornerRadius(radius.toPx(density))
            )
        )

        addPath(arrow.draw(size, density))
    }
}