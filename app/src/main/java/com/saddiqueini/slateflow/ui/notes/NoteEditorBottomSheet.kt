package com.saddiqueini.slateflow.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.saddiqueini.slateflow.data.model.GlassTint
import com.saddiqueini.slateflow.data.model.NoteEntity
import com.saddiqueini.slateflow.ui.components.LiquidGlassCard
import com.saddiqueini.slateflow.ui.components.bouncyClickable
import com.saddiqueini.slateflow.ui.theme.FluidCyan
import com.saddiqueini.slateflow.ui.theme.ObsidianDeep
import com.saddiqueini.slateflow.ui.theme.PriorityHighColor
import com.saddiqueini.slateflow.ui.theme.TextMuted
import com.saddiqueini.slateflow.ui.theme.TextPrimary
import com.saddiqueini.slateflow.ui.theme.Typography

@Composable
fun NoteEditorDialog(
    note: NoteEntity?,
    onSave: (title: String, content: String, isPinned: Boolean, colorIndex: Int) -> Unit,
    onDelete: ((NoteEntity) -> Unit)?,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var isPinned by remember { mutableStateOf(note?.isPinned ?: false) }
    var selectedColorIndex by remember { mutableIntStateOf(note?.colorIndex ?: 0) }

    val activeTint = GlassTint.fromId(selectedColorIndex)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianDeep.copy(alpha = 0.94f))
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp)
        ) {
            LiquidGlassCard(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(28.dp),
                backgroundColor = activeTint.surfaceColor,
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top Action Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Close Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .bouncyClickable(onClick = onDismiss),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Pin Toggle
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isPinned) FluidCyan.copy(alpha = 0.20f)
                                        else Color.White.copy(alpha = 0.08f)
                                    )
                                    .bouncyClickable(onClick = { isPinned = !isPinned }),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                    contentDescription = "Pin Note",
                                    tint = if (isPinned) FluidCyan else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            if (note != null && onDelete != null) {
                                Spacer(modifier = Modifier.width(10.dp))
                                // Delete Note
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(PriorityHighColor.copy(alpha = 0.15f))
                                        .bouncyClickable(onClick = { onDelete(note) }),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = PriorityHighColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Save Note Button
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(FluidCyan)
                                    .bouncyClickable(
                                        enabled = title.isNotBlank() || content.isNotBlank(),
                                        onClick = {
                                            if (title.isNotBlank() || content.isNotBlank()) {
                                                onSave(title, content, isPinned, selectedColorIndex)
                                            }
                                        }
                                    )
                                    .padding(horizontal = 18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Save",
                                    style = Typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ObsidianDeep
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Liquid Glass Tint Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlassTint.entries.forEach { tint ->
                            val isSelected = tint.id == selectedColorIndex
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(tint.accentColor)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .bouncyClickable(onClick = { selectedColorIndex = tint.id })
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Title Input
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (title.isEmpty()) {
                            Text(
                                text = "Note Title",
                                style = Typography.titleLarge.copy(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                            )
                        }
                        BasicTextField(
                            value = title,
                            onValueChange = { title = it },
                            textStyle = Typography.titleLarge.copy(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            cursorBrush = SolidColor(FluidCyan),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Body Content Input
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (content.isEmpty()) {
                            Text(
                                text = "Start writing your thoughts...",
                                style = Typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    color = TextMuted
                                )
                            )
                        }
                        BasicTextField(
                            value = content,
                            onValueChange = { content = it },
                            textStyle = Typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                color = TextPrimary,
                                lineHeight = 24.sp
                            ),
                            cursorBrush = SolidColor(FluidCyan),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
