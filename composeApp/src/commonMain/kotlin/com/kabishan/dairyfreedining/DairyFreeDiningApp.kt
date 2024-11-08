package com.kabishan.dairyfreedining

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kabishan.dairyfreedining.about.AboutScreen
import com.kabishan.dairyfreedining.details.DetailsScreen
import com.kabishan.dairyfreedining.landing.LandingScreen
import com.kabishan.dairyfreedining.submission.SubmissionScreen
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DairyFreeDiningApp() {
    DairyFreeDiningTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = DestinationScreen.Landing.route
        ) {
            composable(route = DestinationScreen.Landing.route) {
                LandingScreen(navController = navController)
            }
            composable(route = DestinationScreen.About.route) {
                AboutScreen(navController = navController)
            }
            composable(route = DestinationScreen.Details.route) {
                val restaurantId = it.arguments?.getString("restaurantId") ?: ""
                val restaurantName = it.arguments?.getString("restaurantName") ?: ""

                DetailsScreen(
                    restaurantId = restaurantId,
                    restaurantName = restaurantName,
                    navController = navController
                )
            }
            composable(route = DestinationScreen.Submission.route) {
                SubmissionScreen(
                    navController = navController
                )
            }
        }
    }
}

fun navigateTo(
    navController: NavController,
    route: String
) {
    navController.navigate(route = route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

sealed class DestinationScreen(val route: String) {
    data object Landing : DestinationScreen("landing")
    data object About : DestinationScreen("about")
    data object Details : DestinationScreen("details/{restaurantId}/{restaurantName}") {
        fun createRoute(
            restaurantId: String,
            restaurantName: String
        ): String {
            return "details/$restaurantId/$restaurantName"
        }
    }
    data object Submission : DestinationScreen("submission")
}