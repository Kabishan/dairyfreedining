package com.kabishan.dairyfreedining.submission

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.submission_screen
import dairyfreedining.composeapp.generated.resources.submission_screen_button_label
import dairyfreedining.composeapp.generated.resources.submission_screen_category_label
import dairyfreedining.composeapp.generated.resources.submission_screen_description
import dairyfreedining.composeapp.generated.resources.submission_screen_food_label
import dairyfreedining.composeapp.generated.resources.submission_screen_restaurant_label
import dairyfreedining.composeapp.generated.resources.submission_screen_success
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SubmissionScreen(
    navController: NavController,
    viewModel: SubmissionViewModel = viewModel(
        factory = SubmissionViewModelFactory(
            repository = SubmissionRepository()
        )
    )
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(resource = Res.string.submission_screen),
                navController = navController
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        SubmissionScreenContent(
            innerPadding = innerPadding,
            submissionScreenStateHolder = viewModel.submissionScreenStateHolder,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun SubmissionScreenContent(
    innerPadding: PaddingValues,
    submissionScreenStateHolder: SubmissionViewModel.SubmissionScreenStateHolder,
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    val focusManager = LocalFocusManager.current
    val showProgressBar = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 8.dp)
    ) {
        val submissionState = submissionScreenStateHolder.submissionState.value

        LaunchedEffect(submissionState) {
            when (submissionState) {
                is SubmissionState.ShowError -> {
                    showProgressBar.value = false
                    snackbarHostState.showSnackbar(getString(submissionState.message))
                }

                SubmissionState.ShowSuccess -> {
                    showProgressBar.value = false
                    focusManager.clearFocus()
                    snackbarHostState.showSnackbar(getString(Res.string.submission_screen_success))
                }

                SubmissionState.ShowLoading -> showProgressBar.value = true
                SubmissionState.Initial -> showProgressBar.value = false
            }
            submissionScreenStateHolder.resetSubmissionState()
        }

        if (showProgressBar.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column {
            Text(
                text = stringResource(resource = Res.string.submission_screen_description),
                modifier = Modifier.padding(top = 8.dp)
            )
            SubmissionTextField(
                value = submissionScreenStateHolder.submissionTextFieldState.value.restaurant,
                onValueChange = {
                    submissionScreenStateHolder.handleSubmissionTextFieldEvent(
                        SubmissionTextFieldEvent.RestaurantTextChanged(it)
                    )
                },
                labelText = stringResource(resource = Res.string.submission_screen_restaurant_label),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            SubmissionTextField(
                value = submissionScreenStateHolder.submissionTextFieldState.value.category,
                onValueChange = {
                    submissionScreenStateHolder.handleSubmissionTextFieldEvent(
                        SubmissionTextFieldEvent.CategoryTextChanged(it)
                    )
                },
                labelText = stringResource(resource = Res.string.submission_screen_category_label),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            SubmissionTextField(
                value = submissionScreenStateHolder.submissionTextFieldState.value.food,
                onValueChange = {
                    submissionScreenStateHolder.handleSubmissionTextFieldEvent(
                        SubmissionTextFieldEvent.FoodTextChanged(it)
                    )
                },
                labelText = stringResource(resource = Res.string.submission_screen_food_label),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Button(
                onClick = { submissionScreenStateHolder.submitFood() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(resource = Res.string.submission_screen_button_label),
                    style = DairyFreeDiningTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun SubmissionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        onValueChange = { onValueChange(it) },
        label = { Text(text = labelText) },
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape
    )
}

@Preview
@Composable
private fun SubmissionScreenContentPreview() {
    DairyFreeDiningTheme {
        SubmissionScreenContent(
            innerPadding = PaddingValues(0.dp),
            submissionScreenStateHolder = SubmissionViewModel.SubmissionScreenStateHolder(
                submissionState = mutableStateOf(SubmissionState.Initial),
                resetSubmissionState = {},
                submissionTextFieldState = mutableStateOf(
                    SubmissionTextFieldState(
                        "McDonald's", "Burger", "Hamburger"
                    )
                ),
                handleSubmissionTextFieldEvent = {},
                submitFood = {}
            )
        )
    }
}