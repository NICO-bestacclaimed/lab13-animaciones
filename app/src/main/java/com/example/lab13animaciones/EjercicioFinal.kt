package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Prototipo "Caza el monstruo": al tocarlo salta de lugar, se encoge, cambia de color según su vida y pulsa. */
@Composable
fun EjercicioFinal() {
    var vida by remember { mutableIntStateOf(5) }
    var pos by remember { mutableIntStateOf(0) }
    var golpe by remember { mutableStateOf(false) }
    val posiciones = listOf(0.dp to 0.dp, 160.dp to 40.dp, 40.dp to 160.dp, 180.dp to 200.dp)

    val color by animateColorAsState(
        when { vida > 3 -> Color(0xFF2E7D32); vida > 1 -> Color(0xFFF9A800); else -> Color(0xFFC62828) },
        label = "color",
    )
    val pulso by rememberInfiniteTransition(label = "pulso")
        .animateFloat(1f, 1.15f, infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "pulso")
    val x by animateDpAsState(posiciones[pos].first, spring(Spring.DampingRatioMediumBouncy), label = "x")
    val y by animateDpAsState(posiciones[pos].second, spring(Spring.DampingRatioMediumBouncy), label = "y")
    val tam by animateDpAsState(if (golpe) 50.dp else 90.dp, tween(100), finishedListener = { golpe = false }, label = "tam")

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Vida del monstruo: $vida", style = MaterialTheme.typography.titleLarge)
        AnimatedContent(vida > 0, label = "fin") { vivo ->
            if (vivo) {
                Box(Modifier.fillMaxWidth().height(360.dp)) {
                    Box(
                        Modifier.offset(x, y).size(tam).scale(pulso).clip(CircleShape).background(color)
                            .clickable { vida--; golpe = true; pos = (pos + 1) % posiciones.size },
                        Alignment.Center,
                    ) { Text("👾") }
                }
            } else {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¡Victoria! 🏆", style = MaterialTheme.typography.headlineMedium)
                    Button({ vida = 5; pos = 0 }) { Text("Jugar de nuevo") }
                }
            }
        }
    }
}
