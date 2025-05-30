package com.daromon.collective

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.daromon.collective.ui.navigation.NavGraph
import com.daromon.collective.ui.theme.CollectiveTheme
import com.daromon.collective.viewmodel.CarViewModel
import com.daromon.collective.viewmodel.ServiceRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val carViewModel: CarViewModel by viewModels()
    val serviceRecordViewModel: ServiceRecordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectiveTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph(
                        carViewModel = carViewModel,
                        serviceRecordViewModel = serviceRecordViewModel
                    )
                }
            }
        }
    }
}
