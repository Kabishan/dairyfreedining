package com.kabishan.dairyfreedining.ui.composables

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
    val foodItemDetails = foodItem.split(" (", "(", limit = 2)

    ListItem(
        headlineContent = {
            Text(
                text = foodItemDetails[0],
                style = DairyFreeDiningTheme.typography.labelLarge
            )
        },
        supportingContent = {
            if (foodItemDetails.size == 2) {
                Text(
                    text = "(${foodItemDetails[1]}",
                    style = DairyFreeDiningTheme.typography.labelMedium
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun FoodListItemPreview() {
    FoodListItem(foodItem = "Burger")
}