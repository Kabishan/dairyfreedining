package com.kabishan.dairyfreedining.coach_marks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import com.pseudoankit.coachmark.shape.Arrow
import com.pseudoankit.coachmark.shape.Balloon

@Composable
fun CoachMarkToolTip(
    text: String,
    arrow: Arrow,
    modifier: Modifier = Modifier
) {
    Balloon(
        arrow = arrow,
        modifier = modifier,
        bgColor = DairyFreeDiningTheme.color.primaryContainer
    ) {
        Text(text = text)
    }
}