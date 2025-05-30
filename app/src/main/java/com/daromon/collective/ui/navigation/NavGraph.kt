package com.daromon.collective.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daromon.collective.ui.screens.AddCarScreen
import com.daromon.collective.ui.screens.CarDetailScreen
import com.daromon.collective.ui.screens.CarListScreen
import com.daromon.collective.ui.screens.ServiceHistoryScreen
import com.daromon.collective.viewmodel.CarViewModel
import com.daromon.collective.viewmodel.ServiceRecordViewModel

sealed class Screen(val route: String) {
    object CarListScreen : Screen("car_list")
    object CarDetail : Screen("car_detail/{carId}") {
        fun passId(id: Int) = "car_detail/$id"
    }

    object AddCar : Screen("add_car")
}

@Composable
fun NavGraph(
    startDestination: String = Screen.CarListScreen.route,
    carViewModel: CarViewModel,
    serviceRecordViewModel: ServiceRecordViewModel
) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.CarListScreen.route) {
            CarListScreen(carViewModel, navController)
        }
        composable(Screen.CarDetail.route) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull()
            carId?.let {
                CarDetailScreen(carId = it, navController = navController, carViewModel)
            }
        }
        composable(Screen.AddCar.route) {
            AddCarScreen(navController = navController, carViewModel)
        }
        composable("service_history/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull()
            carId?.let {
                ServiceHistoryScreen(carId = it, viewModel = serviceRecordViewModel)
            }
        }
    }
}