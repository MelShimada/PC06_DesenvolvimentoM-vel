package com.example.pc06

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pc06.data.PhotoPin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PhotoDetailDialog(pin: PhotoPin, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
        title = { Text("Photo") },
        text = {
            Column {
                AsyncImage(model = pin.photoPath, contentDescription = null)
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(pin.timestamp)),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    )
}
