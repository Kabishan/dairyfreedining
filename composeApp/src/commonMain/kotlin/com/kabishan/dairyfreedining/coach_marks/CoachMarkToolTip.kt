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

@Composable
fun CoachMarkToolTip(
    text: String,
    arrow: Arrow,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = CoachMarkDefaults.cornerRadius,
    padding: PaddingValues = CoachMarkDefaults.padding,
    shadowElevation: Dp = CoachMarkDefaults.shadowElevation,
    bgColor: Color = CoachMarkDefaults.bgColor
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .clip(coachMarkShape(arrow, density, cornerRadius))
            .graphicsLayer {
                this.shadowElevation = shadowElevation.toPx(density)
            }
            .background(bgColor)
            .padding(
                start = arrow.startPadding,
                end = arrow.endPadding,
                top = arrow.topPadding,
                bottom = arrow.bottomPadding
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
                left = arrow.startPadding.toPx(density),
                right = size.width - arrow.endPadding.toPx(density),
                top = arrow.topPadding.toPx(density),
                bottom = size.height - arrow.bottomPadding
                    .toPx(density),
                cornerRadius = CornerRadius(radius.toPx(density))
            )
        )

        addPath(arrow.draw(size, density))
    }
}