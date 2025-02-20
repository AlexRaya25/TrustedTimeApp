package com.rayadev.trustedtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrustedTimeScreen()
        }
    }
}

@Composable
fun TrustedTimeScreen() {
    // State variables to store system and trusted time
    var systemTime by remember { mutableStateOf(getFormattedSystemTime()) }
    var trustedTime by remember { mutableStateOf("Press the button to get trusted time") }

    // Coroutine scope for background execution
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display system time
        Text("📅 System Time", style = MaterialTheme.typography.headlineSmall)
        Text(systemTime, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Display trusted time
        Text("🔒 Trusted Time", style = MaterialTheme.typography.headlineSmall)
        Text(trustedTime, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Button to fetch the trusted time
        Button(onClick = {
            // Update system time
            systemTime = getFormattedSystemTime()

            // Fetch trusted time in the background
            coroutineScope.launch {
                trustedTime = getTrustedTime()
            }
        }) {
            Text("Get Trusted Time")
        }
    }
}

// Function to get trusted time using TrustedTime API
fun getTrustedTime(): String {
    return TrustedTimeApp.trustedTime?.computeCurrentUnixEpochMillis()?.let { millis ->
        val date = Date(millis)
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
    } ?: "Error: Unable to retrieve trusted time"
}

// Function to get the system time formatted as a readable string
fun getFormattedSystemTime(): String {
    val date = Date()
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
}
