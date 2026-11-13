package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio1() {
    var visible by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Button({ visible = !visible }) { Text(if (visible) "Ocultar" else "Mostrar") }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(800)),
            exit = fadeOut(tween(800)),
        ) {
            Box(Modifier.size(150.dp).background(Color(0xFF6200EE)))
        }
        Text("Texto debajo: observa cómo el flujo se ajusta cuando el cuadro desaparece.")
    }
}
