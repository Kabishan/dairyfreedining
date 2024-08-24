package com.kabishan.dairyfreedining.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.error_screen_button_content
import dairyfreedining.composeapp.generated.resources.error_screen_content
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorMessage(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(resource = Res.string.error_screen_content),
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = onClick
        ) {
            Text(text = stringResource(resource = Res.string.error_screen_button_content))
        }
    }
}

@Preview
@Composable
private fun ErrorMessagePreview() {
    DairyFreeDiningTheme {
        ErrorMessage {}
    }
}