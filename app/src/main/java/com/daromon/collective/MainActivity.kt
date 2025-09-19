package com.daromon.collective

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.daromon.collective.ui.components.MainWithDrawer
import com.daromon.collective.ui.theme.CollectiveTheme
import com.daromon.collective.ui.features.car.CarViewModel
import com.daromon.collective.ui.features.person.PersonViewModel
import com.daromon.collective.ui.features.car.ServiceRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val carViewModel: CarViewModel by viewModels()
    val serviceRecordViewModel: ServiceRecordViewModel by viewModels()
    val personViewModel: PersonViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollectiveTheme {
                MainWithDrawer(
                    carViewModel = carViewModel,
                    serviceRecordViewModel = serviceRecordViewModel,
                    personViewModel = personViewModel
                )
            }
        }
    }
}


