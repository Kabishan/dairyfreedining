package com.kabishan.dairyfreedining

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kabishan.dairyfreedining.about.AboutScreen
import com.kabishan.dairyfreedining.details.DetailsScreen
import com.kabishan.dairyfreedining.landing.LandingScreen
import com.kabishan.dairyfreedining.preferences.DataStoreRepository
import com.kabishan.dairyfreedining.preferences.createDataStore
import com.kabishan.dairyfreedining.submission.SubmissionScreen
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DairyFreeDiningApp(
    context: Any? = null
) {
    DairyFreeDiningTheme {
        val navController = rememberNavController()
        val dataStoreRepository = remember {
            DataStoreRepository(dataStore = createDataStore(context))
        }

        NavHost(
            navController = navController,
            startDestination = DestinationScreen.Landing.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            }
        ) {
            composable(route = DestinationScreen.Landing.route) {
                LandingScreen(
                    dataStoreRepository = dataStoreRepository,
                    navController = navController
                )
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
                    dataStoreRepository = dataStoreRepository,
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