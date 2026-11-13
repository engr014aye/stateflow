package com.saddiqueini.slateflow.data.model

import androidx.compose.ui.graphics.Color

enum class Priority(val displayName: String, val colorHex: Long) {
    LOW("Low", 0xFF10B981),       // Vibrant Emerald
    MEDIUM("Medium", 0xFFF59E0B), // Warm Amber
    HIGH("High", 0xFFF43F5E);     // Radiant Coral / Crimson

    fun getComposeColor(): Color = Color(colorHex)
}
