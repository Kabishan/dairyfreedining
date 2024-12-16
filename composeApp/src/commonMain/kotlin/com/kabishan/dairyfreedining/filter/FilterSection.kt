package com.kabishan.dairyfreedining.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.filter_clear_all
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterSection(
    sectionName: String,
    allItems: List<String>,
    selectedItems: List<String>,
    onSelectionChanged: (String) -> Unit,
    onSelectionCleared: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = sectionName.uppercase(),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (selectedItems.isNotEmpty()) {
            OutlinedButton(
                onClick = { onSelectionCleared() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.filter_clear_all)
                    )

                    Icon(
                        imageVector = Icons.Filled.Close,
                        modifier = Modifier.padding(start = 4.dp),
                        contentDescription = String()
                    )
                }
            }
        }
    }

    HorizontalDivider()

    LazyColumn {
        itemsIndexed(allItems) { index, category ->
            val isSelected = selectedItems.contains(category)

            Row(
                Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = isSelected,
                        onValueChange = {
                            isSelected != isSelected
                            onSelectionChanged(category)
                        }
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null
                )
                Text(
                    text = category,
                    style = DairyFreeDiningTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (index < allItems.size - 1) {
                HorizontalDivider()
            }
        }
    }
}