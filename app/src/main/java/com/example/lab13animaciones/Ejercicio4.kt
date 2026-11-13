package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Estado { Cargando, Contenido, Error }

@Composable
fun Ejercicio4() {
    var estado by remember { mutableStateOf(Estado.Cargando) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Estado.entries.forEach { e -> Button({ estado = e }) { Text(e.name) } }
        }
        AnimatedContent(
            targetState = estado,
            transitionSpec = { fadeIn(tween(600, delayMillis = 300)) togetherWith fadeOut(tween(300)) },
            label = "estado",
        ) { e ->
            Box(Modifier.fillMaxWidth().height(150.dp), Alignment.Center) {
                when (e) {
                    Estado.Cargando -> CircularProgressIndicator()
                    Estado.Contenido -> Text("¡Contenido cargado!", style = MaterialTheme.typography.headlineSmall)
                    Estado.Error -> Text("Ocurrió un error", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}
