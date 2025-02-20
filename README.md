# TrustedTimeApp

Esta es una aplicación sencilla de Android que utiliza la **API Trusted Time** para obtener una marca de tiempo segura y precisa desde **Google Play Services**. La aplicación está construida con **Jetpack Compose** para una experiencia de interfaz moderna.

## Pasos para Construir la App

![Imagen  trustedtime](https://github.com/user-attachments/assets/0bf5bd7b-6868-4061-a49c-76ecb5300282)

### 1. **Crear un nuevo Proyecto en Android**

- Abre Android Studio y crea un nuevo proyecto de **Empty Compose Activity**.

### 2. **Agregar Dependencias**

En tu archivo **`build.gradle` (nivel de aplicación)**, agrega las siguientes dependencias:

```gradle
dependencies {
    // API Trusted Time
    implementation(libs.play.service.time)
}
    // Libs Versions API Trusted Time
    [versions]
    playServiceTime = "16.0.1"

    [libraries]
    play-service-time = { module = "com.google.android.gms:play-services-time", version.ref = "playServiceTime"}
```

### 3. **Inicializar el Cliente TrustedTime**
Crea una clase personalizada `Application` para inicializar el **TrustedTimeClient**.

```kotlin
package com.rayadev.trustedtime

import android.app.Application
import android.util.Log
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient

class TrustedTimeApp: Application() {
    companion object {
        var trustedTime: TrustedTimeClient? = null
    }

    override fun onCreate() {
        super.onCreate()
        TrustedTime.createClient(this).addOnSuccessListener { client ->
            trustedTime = client
        }.addOnFailureListener {
            Log.e("Error TrustedTime", it.message.orEmpty())
        }
    }
}
```

### 4. **Configurar `AndroidManifest.xml`**
En el archivo `AndroidManifest.xml`, declara la clase `TrustedTimeApp` para que se inicialice cuando se inicie la app:

```kotlin
<application
    android:name=".TrustedTimeApp"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/Theme.TrustedTimeExample">
    <activity
        android:name=".MainActivity"
        android:label="@string/app_name">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

### 5. **Crear la Interfaz en Jetpack Compose**
Crea la interfaz de usuario en `MainActivity.kt` para mostrar tanto la **hora del sistema** como la **hora confiable**:

```kotlin
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
    var systemTime by remember { mutableStateOf(getFormattedSystemTime()) }
    var trustedTime by remember { mutableStateOf("Presiona el botón para obtener la hora confiable") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar la hora del sistema
        Text("📅 Hora del Sistema", style = MaterialTheme.typography.headlineSmall)
        Text(systemTime, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la hora confiable
        Text("🔒 Hora Confiable", style = MaterialTheme.typography.headlineSmall)
        Text(trustedTime, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para obtener la hora confiable
        Button(onClick = {
            systemTime = getFormattedSystemTime()
            coroutineScope.launch {
                trustedTime = getTrustedTime()
            }
        }) {
            Text("Obtener Hora Confiable")
        }
    }
}

// Función para obtener la hora confiable
fun getTrustedTime(): String {
    return TrustedTimeApp.trustedTime?.computeCurrentUnixEpochMillis()?.let { millis ->
        val date = Date(millis)
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
    } ?: "Error: No se pudo obtener la hora confiable"
}

// Función para obtener la hora del sistema
fun getFormattedSystemTime(): String {
    val date = Date()
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
}
```
