package com.example.lab13animaciones

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio2() {
    var azul by remember { mutableStateOf(true) }
    var resorte by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        if (azul) Color.Blue else Color(0xFF2E7D32),
        if (resorte) spring(Spring.DampingRatioMediumBouncy) else tween(1000),
        label = "color",
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Button({ azul = !azul }) { Text("Cambiar color") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(resorte, { resorte = it })
            Text("  Usar spring (apagado = tween)")
        }
        Box(Modifier.size(150.dp).background(color))
    }
}
