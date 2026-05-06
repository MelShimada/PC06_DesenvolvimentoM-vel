package com.example.pc06

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNav() {
    val vm: MapViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "map") {
        composable("map") {
            val pins by vm.pins.collectAsState()
            MapScreen(pins = pins, navController = navController)
        }
        composable("camera") {
            CameraScreen(viewModel = vm, navController = navController)
        }
    }
}
