package com.kabishan.dairyfreedining.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.loading_screen_content
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RestaurantTile(
    text: String,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(256.dp)
            .clickable { onClick.invoke() }
    ) {
        KamelImage(
            resource = asyncPainterResource(data = imageUrl),
            contentDescription = String(),
            contentScale = ContentScale.Crop,
            onLoading = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(resource = Res.string.loading_screen_content),
                        style = DairyFreeDiningTheme.typography.titleMedium
                    )
                }
            }
        )
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = DairyFreeDiningTheme.color.primaryContainer
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(4.dp),
                    style = DairyFreeDiningTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun RestaurantTilePreview() {
    DairyFreeDiningTheme {
        RestaurantTile(
            text = "McDonald's",
            imageUrl = "https://godairyfree.org/wp-content/uploads/2006/05/fast-food-mcdonalds-nuggets.jpg",
            onClick = {}
        )
    }
}