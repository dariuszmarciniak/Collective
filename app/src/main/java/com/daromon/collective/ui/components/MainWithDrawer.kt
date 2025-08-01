package com.daromon.collective.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daromon.collective.R
import com.daromon.collective.ui.navigation.NavGraph
import com.daromon.collective.ui.navigation.Screen
import com.daromon.collective.viewmodel.CarViewModel
import com.daromon.collective.viewmodel.PersonViewModel
import com.daromon.collective.viewmodel.ServiceRecordViewModel
import kotlinx.coroutines.launch

@Composable
fun MainWithDrawer(
    carViewModel: CarViewModel,
    serviceRecordViewModel: ServiceRecordViewModel,
    personViewModel: PersonViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.home_page)) },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Home.route) { popUpTo(0) }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.cars)) },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.CarListScreen.route) { popUpTo(0) }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(

                    label = { Text(stringResource(R.string.notes)) },
                    selected = false,
                    onClick = {
                        navController.navigate("notes") { popUpTo(0) }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.settings)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Settings.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.persons)) },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Persons.route) { popUpTo(0) }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        NavGraph(
            navController = navController,
            carViewModel = carViewModel,
            serviceRecordViewModel = serviceRecordViewModel,
            personViewModel = personViewModel,
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
}