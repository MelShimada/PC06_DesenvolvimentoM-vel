package com.example.pc06

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File

@Composable
fun CameraScreen(viewModel: MapViewModel, navController: NavController) {
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION))
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
                    val capture = ImageCapture.Builder().build()
                    imageCapture = capture
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        ctx as androidx.lifecycle.LifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        capture
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            }
        )

    Button(
            onClick = {
                val capture = imageCapture ?: return@Button
                val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                val output = ImageCapture.OutputFileOptions.Builder(file).build()
                capture.takePicture(output, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        navController.popBackStack()
                        if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                                    .addOnSuccessListener { location ->
                                        if (location != null) {
                                            viewModel.savePin(file.absolutePath, location.latitude, location.longitude)
                                        }
                                    }
                            } catch (_: SecurityException) {
                            }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        navController.popBackStack()
                    }
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(androidx.compose.ui.Alignment.BottomCenter)
        ) {
            Text("Take Picture")
        }
    }
}
