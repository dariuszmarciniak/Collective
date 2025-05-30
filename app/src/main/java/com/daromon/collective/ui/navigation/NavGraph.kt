package com.daromon.collective.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daromon.collective.ui.screens.AddCarScreen
import com.daromon.collective.ui.screens.CarDetailScreen
import com.daromon.collective.ui.screens.CarListScreen
import com.daromon.collective.ui.screens.HomeScreen
import com.daromon.collective.ui.screens.NotesScreen
import com.daromon.collective.ui.screens.ServiceHistoryScreen
import com.daromon.collective.ui.screens.SettingsScreen
import com.daromon.collective.viewmodel.CarViewModel
import com.daromon.collective.viewmodel.ServiceRecordViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CarListScreen : Screen("car_list")
    object CarDetail : Screen("car_detail/{carId}") {
        fun passId(carId: Int) = "car_detail/$carId"
    }

    object AddCar : Screen("add_car")
    object ServiceHistory : Screen("service_history/{carId}") {
        fun passId(carId: Int) = "service_history/$carId"
    }

    object Notes : Screen("notes")
    object Settings : Screen("settings")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    carViewModel: CarViewModel,
    serviceRecordViewModel: ServiceRecordViewModel,
    openDrawer: () -> Unit
) {
    NavHost(
        navController = navController, startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, openDrawer)
        }
        composable(Screen.CarListScreen.route) {
            CarListScreen(
                viewModel = carViewModel, navController = navController, openDrawer = openDrawer
            )
        }
        composable(Screen.AddCar.route) {
            AddCarScreen(
                navController = navController, viewModel = carViewModel, openDrawer = openDrawer
            )
        }
        composable(Screen.CarDetail.route) { backStackEntry ->
            val carId =
                backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: return@composable
            CarDetailScreen(
                carId = carId, navController = navController, viewModel = carViewModel
            )
        }
        composable(Screen.ServiceHistory.route) { backStackEntry ->
            val carId =
                backStackEntry.arguments?.getString("carId")?.toIntOrNull() ?: return@composable
            ServiceHistoryScreen(
                carId = carId, viewModel = serviceRecordViewModel, openDrawer = openDrawer
            )
        }
        composable(Screen.Notes.route) {
            NotesScreen(openDrawer)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(openDrawer)
        }
    }
}