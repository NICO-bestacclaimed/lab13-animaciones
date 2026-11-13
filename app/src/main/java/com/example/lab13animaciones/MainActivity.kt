package com.example.lab13animaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Menu() } }
    }
}

@Composable
fun Menu() {
    val pantallas = listOf<Pair<String, @Composable () -> Unit>>(
        "1. AnimatedVisibility" to { Ejercicio1() },
        "2. animateColorAsState" to { Ejercicio2() },
        "3. Tamaño y posición" to { Ejercicio3() },
        "4. AnimatedContent" to { Ejercicio4() },
        "Final: Caza el monstruo" to { EjercicioFinal() },
    )
    var actual by remember { mutableStateOf<Int?>(null) }
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.systemBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val i = actual
            if (i == null) {
                Text("Laboratorio 13: Animaciones", style = MaterialTheme.typography.headlineSmall)
                pantallas.forEachIndexed { idx, (n, _) -> Button({ actual = idx }, Modifier.fillMaxWidth()) { Text(n) } }
            } else {
                TextButton({ actual = null }) { Text("← Volver") }
                pantallas[i].second()
            }
        }
    }
}
