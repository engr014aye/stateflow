package com.saddiqueini.slateflow.data.model

import androidx.compose.ui.graphics.Color

enum class GlassTint(
    val id: Int,
    val title: String,
    val surfaceColor: Color,
    val borderColor: Color,
    val accentColor: Color
) {
    OBSIDIAN(
        id = 0,
        title = "Obsidian",
        surfaceColor = Color(0xFF131A2A).copy(alpha = 0.65f),
        borderColor = Color(0xFF94A3B8).copy(alpha = 0.22f),
        accentColor = Color(0xFF94A3B8)
    ),
    CYAN_ICE(
        id = 1,
        title = "Cyan Ice",
        surfaceColor = Color(0xFF0F2B33).copy(alpha = 0.65f),
        borderColor = Color(0xFF00F5D4).copy(alpha = 0.35f),
        accentColor = Color(0xFF00F5D4)
    ),
    INDIGO_DUSK(
        id = 2,
        title = "Indigo Dusk",
        surfaceColor = Color(0xFF1E1B4B).copy(alpha = 0.65f),
        borderColor = Color(0xFF818CF8).copy(alpha = 0.35f),
        accentColor = Color(0xFF818CF8)
    ),
    EMERALD_MIST(
        id = 3,
        title = "Emerald Mist",
        surfaceColor = Color(0xFF063327).copy(alpha = 0.65f),
        borderColor = Color(0xFF34D399).copy(alpha = 0.35f),
        accentColor = Color(0xFF34D399)
    ),
    AMBER_DAWN(
        id = 4,
        title = "Amber Dawn",
        surfaceColor = Color(0xFF36210A).copy(alpha = 0.65f),
        borderColor = Color(0xFFFBBF24).copy(alpha = 0.35f),
        accentColor = Color(0xFFFBBF24)
    ),
    VIOLET_NEBULA(
        id = 5,
        title = "Violet Nebula",
        surfaceColor = Color(0xFF2E1065).copy(alpha = 0.65f),
        borderColor = Color(0xFFA78BFA).copy(alpha = 0.35f),
        accentColor = Color(0xFFA78BFA)
    );

    companion object {
        fun fromId(id: Int): GlassTint = entries.firstOrNull { it.id == id } ?: OBSIDIAN
    }
}
