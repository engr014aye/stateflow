package com.saddiqueini.slateflow.ui.tasks

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.saddiqueini.slateflow.data.model.Priority
import com.saddiqueini.slateflow.data.model.TaskEntity
import com.saddiqueini.slateflow.ui.components.LiquidGlassCard
import com.saddiqueini.slateflow.ui.components.PriorityBadge
import com.saddiqueini.slateflow.ui.components.bouncyClickable
import com.saddiqueini.slateflow.ui.theme.FluidCyan
import com.saddiqueini.slateflow.ui.theme.ObsidianDeep
import com.saddiqueini.slateflow.ui.theme.PriorityHighColor
import com.saddiqueini.slateflow.ui.theme.TextMuted
import com.saddiqueini.slateflow.ui.theme.TextPrimary
import com.saddiqueini.slateflow.ui.theme.Typography

@Composable
fun TaskEditorDialog(
    task: TaskEntity?,
    onSave: (title: String, description: String, priority: Priority) -> Unit,
    onDelete: ((TaskEntity) -> Unit)?,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var selectedPriority by remember { mutableStateOf(task?.priority ?: Priority.MEDIUM) }

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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                backgroundColor = Color(0xFF131D33).copy(alpha = 0.90f),
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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
                            if (task != null && onDelete != null) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(PriorityHighColor.copy(alpha = 0.15f))
                                        .bouncyClickable(onClick = { onDelete(task) }),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Task",
                                        tint = PriorityHighColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(FluidCyan)
                                    .bouncyClickable(
                                        enabled = title.isNotBlank(),
                                        onClick = {
                                            if (title.isNotBlank()) {
                                                onSave(title, description, selectedPriority)
                                            }
                                        }
                                    )
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Save Task",
                                    style = Typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ObsidianDeep
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (task == null) "New Action Item" else "Edit Action Item",
                        style = Typography.titleLarge.copy(fontSize = 20.sp),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Title
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (title.isEmpty()) {
                            Text(
                                text = "What needs to be done?",
                                style = Typography.bodyLarge.copy(color = TextMuted)
                            )
                        }
                        BasicTextField(
                            value = title,
                            onValueChange = { title = it },
                            textStyle = Typography.bodyLarge.copy(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            ),
                            cursorBrush = SolidColor(FluidCyan),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Quick Note / Description
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                    ) {
                        if (description.isEmpty()) {
                            Text(
                                text = "Add optional details or context...",
                                style = Typography.bodyMedium.copy(color = TextMuted)
                            )
                        }
                        BasicTextField(
                            value = description,
                            onValueChange = { description = it },
                            textStyle = Typography.bodyMedium.copy(
                                color = TextPrimary,
                                lineHeight = 20.sp
                            ),
                            cursorBrush = SolidColor(FluidCyan),
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Priority Selector
                    Text(
                        text = "Priority Level",
                        style = Typography.labelMedium.copy(color = TextMuted)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Priority.entries.forEach { priority ->
                            val isSelected = selectedPriority == priority
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) priority.getComposeColor().copy(alpha = 0.25f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) priority.getComposeColor()
                                        else Color.White.copy(alpha = 0.10f),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .bouncyClickable(onClick = { selectedPriority = priority })
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                PriorityBadge(
                                    priority = priority,
                                    showLabel = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
