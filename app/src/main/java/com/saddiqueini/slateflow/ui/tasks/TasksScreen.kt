package com.saddiqueini.slateflow.ui.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saddiqueini.slateflow.R
import com.saddiqueini.slateflow.data.model.Priority
import com.saddiqueini.slateflow.data.model.TaskEntity
import com.saddiqueini.slateflow.ui.components.LiquidGlassCard
import com.saddiqueini.slateflow.ui.components.PriorityBadge
import com.saddiqueini.slateflow.ui.components.bouncyClickable
import com.saddiqueini.slateflow.ui.theme.FluidCyan
import com.saddiqueini.slateflow.ui.theme.TextMuted
import com.saddiqueini.slateflow.ui.theme.TextPrimary
import com.saddiqueini.slateflow.ui.theme.TextSecondary
import com.saddiqueini.slateflow.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    modifier: Modifier = Modifier
) {
    val activeTasks by viewModel.activeTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()
    val priorityFilter by viewModel.priorityFilter.collectAsState()
    val isCompletedExpanded by viewModel.isCompletedExpanded.collectAsState()
    val isEditorOpen by viewModel.isEditorOpen.collectAsState()
    val editingTask by viewModel.editingTask.collectAsState()
    val recentlyDeletedTask by viewModel.recentlyDeletedTask.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(recentlyDeletedTask) {
        val task = recentlyDeletedTask
        if (task != null) {
            val result = snackbarHostState.showSnackbar(
                message = "Task \"${task.title}\" deleted",
                actionLabel = "UNDO",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearRecentlyDeleted()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Priority Filter Bar
            PriorityFilterRow(
                selectedFilter = priorityFilter,
                onFilterSelected = { viewModel.setPriorityFilter(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            if (activeTasks.isEmpty() && completedTasks.isEmpty()) {
                TasksEmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Active Tasks
                    items(activeTasks, key = { "active_${it.id}" }) { task ->
                        TaskItemCard(
                            task = task,
                            onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                            onTaskClick = { viewModel.openEditTask(task) },
                            onDelete = { viewModel.deleteTask(task) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        )
                    }

                    // Completed Section Header
                    if (completedTasks.isNotEmpty()) {
                        item(key = "completed_header") {
                            CompletedSectionHeader(
                                count = completedTasks.size,
                                isExpanded = isCompletedExpanded,
                                onToggleExpand = { viewModel.toggleCompletedExpanded() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, bottom = 4.dp)
                            )
                        }

                        if (isCompletedExpanded) {
                            items(completedTasks, key = { "completed_${it.id}" }) { task ->
                                TaskItemCard(
                                    task = task,
                                    onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                                    onTaskClick = { viewModel.openEditTask(task) },
                                    onDelete = { viewModel.deleteTask(task) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem()
                                )
                            }
                        }
                    }
                }
            }
        }

        // Snackbar Host for Undo
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )

        // Editor Dialog
        if (isEditorOpen) {
            TaskEditorDialog(
                task = editingTask,
                onSave = { title, description, priority ->
                    viewModel.saveTask(title, description, priority)
                },
                onDelete = { taskToDelete ->
                    viewModel.deleteTask(taskToDelete)
                },
                onDismiss = { viewModel.closeEditor() }
            )
        }
    }
}

@Composable
fun PriorityFilterRow(
    selectedFilter: Priority?,
    onFilterSelected: (Priority?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All" chip
        item {
            val isSelected = selectedFilter == null
            FilterChip(
                label = "All",
                isSelected = isSelected,
                onClick = { onFilterSelected(null) }
            )
        }

        items(Priority.entries.toTypedArray()) { priority ->
            val isSelected = selectedFilter == priority
            FilterChip(
                label = priority.displayName,
                indicatorColor = priority.getComposeColor(),
                isSelected = isSelected,
                onClick = { onFilterSelected(priority) }
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    indicatorColor: Color? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) FluidCyan.copy(alpha = 0.22f)
                else Color.White.copy(alpha = 0.06f)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) FluidCyan.copy(alpha = 0.6f)
                else Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp)
            )
            .bouncyClickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (indicatorColor != null) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(indicatorColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = label,
            style = Typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) TextPrimary else TextSecondary
            )
        )
    }
}

@Composable
fun TaskItemCard(
    task: TaskEntity,
    onToggleCompletion: () -> Unit,
    onTaskClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = if (task.isCompleted) Color(0xFF0F172A).copy(alpha = 0.40f)
        else Color(0xFF131C31).copy(alpha = 0.65f),
        contentPadding = 14.dp,
        onClick = onTaskClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tactile Spring Bouncy Checkbox
            BouncyCheckbox(
                isChecked = task.isCompleted,
                onCheckedChange = { onToggleCompletion() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Task content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = Typography.bodyLarge.copy(
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (task.isCompleted) TextMuted else TextPrimary
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = Typography.bodyMedium.copy(
                            color = TextMuted,
                            fontSize = 12.sp,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Priority Indicator
            PriorityBadge(
                priority = task.priority,
                showLabel = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Delete action
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .bouncyClickable(onClick = onDelete),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextMuted.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun BouncyCheckbox(
    isChecked: Boolean,
    onCheckedChange: () -> Unit
) {
    val checkScale by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "CheckScale"
    )

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isChecked) FluidCyan
                else Color.White.copy(alpha = 0.08f)
            )
            .border(
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (isChecked) FluidCyan else Color.White.copy(alpha = 0.25f)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .bouncyClickable(onClick = onCheckedChange),
        contentAlignment = Alignment.Center
    ) {
        if (isChecked || checkScale > 0.05f) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color(0xFF070B12),
                modifier = Modifier
                    .size(16.dp)
                    .rotate(if (isChecked) 0f else -45f)
            )
        }
    }
}

@Composable
fun CompletedSectionHeader(
    count: Int,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "ChevronRotation"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .bouncyClickable(onClick = onToggleExpand)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Completed",
                style = Typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$count",
                    style = Typography.labelSmall.copy(color = TextMuted)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Expand or collapse",
            tint = TextMuted,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotationAngle)
        )
    }
}

@Composable
fun TasksEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(FluidCyan.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = null,
                tint = FluidCyan,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.no_tasks_title),
            style = Typography.titleLarge.copy(fontSize = 20.sp),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.no_tasks_subtitle),
            style = Typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(horizontal = 24.dp),
            lineHeight = 20.sp
        )
    }
}
