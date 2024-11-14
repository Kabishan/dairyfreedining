package com.kabishan.dairyfreedining.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.search_bar_leading
import dairyfreedining.composeapp.generated.resources.search_bar_trailing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = placeholderText)
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(resource = Res.string.search_bar_leading)
            )
        },
        trailingIcon = {
            if (query.trim().isNotBlank()) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = stringResource(resource = Res.string.search_bar_trailing),
                    modifier = Modifier.clickable {
                        onQueryChange(String())
                    }
                )
            }
        },
        singleLine = true,
        shape = RectangleShape,
        modifier = modifier.fillMaxWidth().height(64.dp)
    )
}

@Preview
@Composable
private fun DairyFreeDiningSearchBarPreview() {
    DairyFreeDiningTheme {
        SearchBar(
            query = "Test",
            onQueryChange = {},
            placeholderText = "Search something..."
        )
    }
}