package com.kabishan.dairyfreedining.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.DestinationScreen
import com.kabishan.dairyfreedining.navigateTo
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.about_screen
import dairyfreedining.composeapp.generated.resources.navigate_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    navController: NavController?,
    showAboutIcon: Boolean = true,
    showBackIcon: Boolean = true
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = DairyFreeDiningTheme.typography.titleLarge
            )
        },
        actions = {
            if (showAboutIcon) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(resource = Res.string.about_screen),
                    modifier = Modifier
                        .clickable {
                            if (navController != null) {
                                navigateTo(navController, DestinationScreen.About.route)
                            }
                        }
                        .padding(end = 4.dp)
                )
            }
        },
        navigationIcon = {
            if (showBackIcon) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(resource = Res.string.navigate_back),
                    modifier = Modifier
                        .clickable {
                            navController?.popBackStack()
                        }
                        .padding(horizontal = 4.dp)
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    DairyFreeDiningTheme {
        TopBar(
            title = "Screen",
            navController = null
        )
    }
}