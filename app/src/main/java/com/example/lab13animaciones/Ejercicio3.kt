package com.example.lab13animaciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio3() {
    var activo by remember { mutableStateOf(false) }
    val tam by animateDpAsState(if (activo) 150.dp else 60.dp, label = "tam")
    val x by animateDpAsState(if (activo) 120.dp else 0.dp, label = "x")
    val y by animateDpAsState(if (activo) 60.dp else 0.dp, label = "y")
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Button({ activo = !activo }) { Text("Mover y escalar") }
        // El orden importa: offset va ANTES de background; si va después, el fondo se dibuja sin moverse.
        Box(Modifier.offset(x, y).size(tam).background(Color.Red))
    }
}
