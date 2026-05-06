package com.example.pc06

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pc06.BuildConfig
import com.example.pc06.data.PhotoPin
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.core.content.ContextCompat.checkSelfPermission
import android.Manifest
import android.content.pm.PackageManager

@Composable
fun MapScreen(pins: List<PhotoPin>, navController: NavController) {
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val hasMapsKey = BuildConfig.MAPS_API_KEY.isNotBlank()
    var selectedPin by remember { mutableStateOf<PhotoPin?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 1f)
    }
    var locationEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationEnabled = true
            try {
                locationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            cameraPositionState.move(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(location.latitude, location.longitude),
                                    16f
                                )
                            )
                        }
                    }
            } catch (_: SecurityException) {
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (hasMapsKey) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = locationEnabled)
            ) {
                pins.forEach { pin ->
                    Marker(
                        state = MarkerState(position = LatLng(pin.latitude, pin.longitude)),
                        onClick = {
                            selectedPin = pin
                            true
                        }
                    )
                }
            }
        } else {
            Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Add your Google Maps API key to show the map.")
                Text("Camera flow still works.", modifier = Modifier.padding(top = 8.dp))
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("camera") },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Text("+")
        }

        selectedPin?.let { pin ->
            PhotoDetailDialog(pin = pin) { selectedPin = null }
        }
    }
}
