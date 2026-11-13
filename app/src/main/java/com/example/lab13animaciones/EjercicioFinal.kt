package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/** Prototipo de videojuego tipo Tetris simplificado (tablero 8x14, 7 piezas, sin niveles ni pieza siguiente). */
private const val FILAS = 14
private const val COLS = 8
private typealias Celdas = List<Pair<Int, Int>>

private val FORMAS: List<Celdas> = listOf(
    listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3), // I
    listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1), // O
    listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2), // T
    listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1), // S
    listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2), // Z
    listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1), // L
    listOf(0 to 1, 1 to 1, 2 to 1, 2 to 0), // J
)
private val COLORES = listOf(
    Color(0xFF00BCD4), Color(0xFFFFEB3B), Color(0xFF9C27B0), Color(0xFF4CAF50),
    Color(0xFFF44336), Color(0xFFFF9800), Color(0xFF2196F3),
)

private class Pieza(val tipo: Int, val celdas: Celdas, val f: Int, val c: Int) {
    fun absolutas() = celdas.map { (r, k) -> (f + r) to (c + k) }
}

private fun nueva() = (FORMAS.indices).random().let { Pieza(it, FORMAS[it], 0, COLS / 2 - 2) }

private fun cabe(tablero: List<Int>, p: Pieza) =
    p.absolutas().all { (r, k) -> r in 0 until FILAS && k in 0 until COLS && tablero[r * COLS + k] == 0 }

private fun rotada(p: Pieza): Pieza {
    val g = p.celdas.map { (r, k) -> k to -r }
    val minR = g.minOf { it.first }; val minK = g.minOf { it.second }
    return Pieza(p.tipo, g.map { (r, k) -> (r - minR) to (k - minK) }, p.f, p.c)
}

@Composable
fun EjercicioFinal() {
    var tablero by remember { mutableStateOf(List(FILAS * COLS) { 0 }) }
    var pieza by remember { mutableStateOf(nueva()) }
    var puntos by remember { mutableIntStateOf(0) }
    var terminado by remember { mutableStateOf(false) }
    var partida by remember { mutableIntStateOf(0) }

    fun mover(df: Int, dc: Int) {
        val p = Pieza(pieza.tipo, pieza.celdas, pieza.f + df, pieza.c + dc)
        if (cabe(tablero, p)) { pieza = p; return }
        if (df == 0) return
        // la pieza toca fondo: se fija, se borran filas completas y entra una nueva
        val t = tablero.toMutableList()
        pieza.absolutas().forEach { (r, k) -> t[r * COLS + k] = pieza.tipo + 1 }
        val filas = t.chunked(COLS).filter { fila -> fila.any { it == 0 } }
        val borradas = FILAS - filas.size
        tablero = (List(borradas) { List(COLS) { 0 } } + filas).flatten()
        puntos += borradas * 100 + 10
        val n = nueva()
        if (cabe(tablero, n)) pieza = n else terminado = true
    }

    fun reiniciar() {
        tablero = List(FILAS * COLS) { 0 }; pieza = nueva(); puntos = 0; terminado = false; partida++
    }

    LaunchedEffect(terminado, partida) {
        while (!terminado) { delay(600); mover(1, 0) }
    }

    val activas = pieza.absolutas().toSet()
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Puntos: ", style = MaterialTheme.typography.titleLarge)
            AnimatedContent(
                targetState = puntos,
                transitionSpec = { (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut()) },
                label = "puntos",
            ) { Text("$it", style = MaterialTheme.typography.titleLarge) }
        }
        Box {
            Column(Modifier.background(Color.Black).padding(2.dp)) {
                for (r in 0 until FILAS) Row {
                    for (k in 0 until COLS) {
                        val v = tablero[r * COLS + k]
                        val objetivo = when {
                            (r to k) in activas -> COLORES[pieza.tipo]
                            v > 0 -> COLORES[v - 1]
                            else -> Color(0xFF263238)
                        }
                        val color by animateColorAsState(objetivo, tween(150), label = "celda")
                        Box(Modifier.padding(1.dp).size(26.dp).clip(RoundedCornerShape(4.dp)).background(color))
                    }
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                terminado, Modifier.matchParentSize(),
                enter = fadeIn(tween(500)) + scaleIn(initialScale = 0.5f),
                exit = fadeOut(),
            ) {
                Column(Modifier.background(Color(0xCC000000)), Arrangement.Center, Alignment.CenterHorizontally) {
                    Text("GAME OVER", color = Color.White, style = MaterialTheme.typography.headlineMedium)
                    Button(::reiniciar) { Text("Jugar de nuevo") }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button({ if (!terminado) mover(0, -1) }) { Text("◀") }
            Button({ if (!terminado) rotada(pieza).let { if (cabe(tablero, it)) pieza = it } }) { Text("⟳") }
            Button({ if (!terminado) mover(0, 1) }) { Text("▶") }
            Button({ if (!terminado) mover(1, 0) }) { Text("▼") }
        }
    }
}
