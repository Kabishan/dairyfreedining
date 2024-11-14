package com.kabishan.dairyfreedining.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DairyFreeDiningTheme.color.primaryContainer
    ) {
        Text(
            text = text,
            modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            style = DairyFreeDiningTheme.typography.titleMedium
        )
    }
    HorizontalDivider()
}

@Preview
@Composable
private fun CategoryHeaderPreview() {
    DairyFreeDiningTheme {
        CategoryHeader(text = "Classics")
    }
}