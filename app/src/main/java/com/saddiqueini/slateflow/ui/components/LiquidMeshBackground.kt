package com.saddiqueini.slateflow.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.saddiqueini.slateflow.ui.theme.AmbientViolet
import com.saddiqueini.slateflow.ui.theme.ElectricIndigo
import com.saddiqueini.slateflow.ui.theme.FluidCyan
import com.saddiqueini.slateflow.ui.theme.MidnightNavy
import com.saddiqueini.slateflow.ui.theme.ObsidianDeep

@Composable
fun LiquidMeshBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "MeshAnimation")

    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase2"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Deep Obsidian to Midnight Navy Base Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    ObsidianDeep,
                    MidnightNavy,
                    Color(0xFF090E17)
                )
            ),
            size = size
        )

        // 2. Cyan Ambient Glowing Orb (Upper right fluid drift)
        val cyanCenter = Offset(
            x = width * (0.70f + 0.18f * (phase1 - 0.5f)),
            y = height * (0.22f + 0.12f * (phase2 - 0.5f))
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    FluidCyan.copy(alpha = 0.16f),
                    FluidCyan.copy(alpha = 0.05f),
                    Color.Transparent
                ),
                center = cyanCenter,
                radius = width * 0.75f
            ),
            center = cyanCenter,
            radius = width * 0.75f
        )

        // 3. Electric Indigo Glowing Orb (Lower left fluid drift)
        val indigoCenter = Offset(
            x = width * (0.25f + 0.15f * (0.5f - phase2)),
            y = height * (0.70f + 0.16f * (phase1 - 0.5f))
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricIndigo.copy(alpha = 0.20f),
                    ElectricIndigo.copy(alpha = 0.06f),
                    Color.Transparent
                ),
                center = indigoCenter,
                radius = width * 0.85f
            ),
            center = indigoCenter,
            radius = width * 0.85f
        )

        // 4. Subtle Ambient Violet Orb (Center drift for organic depth)
        val violetCenter = Offset(
            x = width * (0.50f + 0.12f * (phase2 - 0.5f)),
            y = height * (0.45f + 0.14f * (0.5f - phase1))
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AmbientViolet.copy(alpha = 0.12f),
                    AmbientViolet.copy(alpha = 0.03f),
                    Color.Transparent
                ),
                center = violetCenter,
                radius = width * 0.65f
            ),
            center = violetCenter,
            radius = width * 0.65f
        )
    }
}
