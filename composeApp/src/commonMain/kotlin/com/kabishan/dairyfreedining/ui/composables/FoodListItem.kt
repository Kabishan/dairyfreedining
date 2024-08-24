package com.kabishan.dairyfreedining.ui.composables

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodListItem(
    foodItem: String,
    modifier: Modifier = Modifier
) {
    val text: List<String> = foodItem.split(" (", "(", limit = 2)

    if (text.size == 2) {
        ListItem(
            headlineContent = { Text(text = text[0], style = DairyFreeDiningTheme.typography.labelLarge) },
            supportingContent = { Text(text = "(${text[1]}", style = DairyFreeDiningTheme.typography.labelMedium) },
            modifier = modifier
        )
    } else {
        ListItem(
            headlineContent = { Text(text = foodItem, style = DairyFreeDiningTheme.typography.labelLarge) },
            modifier = modifier
        )
    }
    HorizontalDivider()
}

@Preview
@Composable
private fun FoodListItemPreview() {
    FoodListItem(foodItem = "Burger")
}