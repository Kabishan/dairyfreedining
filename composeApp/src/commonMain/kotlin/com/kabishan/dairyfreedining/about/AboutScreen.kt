package com.kabishan.dairyfreedining.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.ui.composables.CategoryHeader
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.about_me_header
import dairyfreedining.composeapp.generated.resources.about_me_paragraph_1
import dairyfreedining.composeapp.generated.resources.about_screen
import dairyfreedining.composeapp.generated.resources.credits_content_paragraph_1
import dairyfreedining.composeapp.generated.resources.credits_header
import dairyfreedining.composeapp.generated.resources.disclaimer_content_paragraph_1
import dairyfreedining.composeapp.generated.resources.disclaimer_content_paragraph_2
import dairyfreedining.composeapp.generated.resources.disclaimer_content_paragraph_3
import dairyfreedining.composeapp.generated.resources.disclaimer_header
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                stringResource(resource = Res.string.about_screen),
                navController = navController,
                showAboutIcon = false
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AboutScreenContent(innerPadding = innerPadding)
    }
}

@Composable
private fun AboutScreenContent(innerPadding: PaddingValues) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(state = scrollState)
    ) {
        CategoryHeader(
            text = stringResource(resource = Res.string.about_me_header),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(resource = Res.string.about_me_paragraph_1),
            style = DairyFreeDiningTheme.typography.labelLarge,
            modifier = Modifier.padding(8.dp)
        )

        CategoryHeader(
            text = stringResource(resource = Res.string.credits_header),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(resource = Res.string.credits_content_paragraph_1),
            style = DairyFreeDiningTheme.typography.labelLarge,
            modifier = Modifier.padding(8.dp)
        )

        CategoryHeader(
            stringResource(resource = Res.string.disclaimer_header),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(resource = Res.string.disclaimer_content_paragraph_1),
            style = DairyFreeDiningTheme.typography.labelLarge,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            stringResource(resource = Res.string.disclaimer_content_paragraph_2),
            style = DairyFreeDiningTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            stringResource(resource = Res.string.disclaimer_content_paragraph_3),
            style = DairyFreeDiningTheme.typography.labelLarge,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 4.dp)
        )
    }
}

@Preview
@Composable
private fun AboutScreenContentPreview() {
    DairyFreeDiningTheme {
        AboutScreenContent(PaddingValues())
    }
}