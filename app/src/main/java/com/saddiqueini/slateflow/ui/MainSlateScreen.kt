package com.saddiqueini.slateflow.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saddiqueini.slateflow.R
import com.saddiqueini.slateflow.ui.components.GlassSearchBar
import com.saddiqueini.slateflow.ui.components.LiquidMeshBackground
import com.saddiqueini.slateflow.ui.components.LiquidSegmentedControl
import com.saddiqueini.slateflow.ui.components.PrivacyPolicyDialog
import com.saddiqueini.slateflow.ui.components.SlateTab
import com.saddiqueini.slateflow.ui.components.bouncyClickable
import com.saddiqueini.slateflow.ui.notes.NotesScreen
import com.saddiqueini.slateflow.ui.notes.NotesViewModel
import com.saddiqueini.slateflow.ui.tasks.TasksScreen
import com.saddiqueini.slateflow.ui.tasks.TasksViewModel
import com.saddiqueini.slateflow.ui.theme.FluidCyan
import com.saddiqueini.slateflow.ui.theme.ObsidianDeep
import com.saddiqueini.slateflow.ui.theme.TextMuted
import com.saddiqueini.slateflow.ui.theme.TextPrimary
import com.saddiqueini.slateflow.ui.theme.Typography

@Composable
fun MainSlateScreen(
    notesViewModel: NotesViewModel,
    tasksViewModel: TasksViewModel
) {
    var currentTab by remember { mutableStateOf(SlateTab.NOTES) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val notesQuery by notesViewModel.searchQuery.collectAsState()
    val tasksQuery by tasksViewModel.searchQuery.collectAsState()
    val isGridView by notesViewModel.isGridView.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDeep)
    ) {
        // Deep Animated Fluid Mesh Gradient Background
        LiquidMeshBackground()

        // Main Layout with Proper Window Insets
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Title with glowing cyan indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = Typography.displayLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(FluidCyan)
                    )
                }

                // Action Icons (Grid/List toggle & Privacy shield)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (currentTab == SlateTab.NOTES) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.12f),
                                    shape = CircleShape
                                )
                                .bouncyClickable(onClick = { notesViewModel.toggleViewMode() }),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
                                contentDescription = "Toggle Grid or List",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Privacy Shield Info Modal
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                            .bouncyClickable(onClick = { showPrivacyDialog = true }),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Security,
                            contentDescription = "Offline Privacy Information",
                            tint = FluidCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Real-Time Frosted Search Bar
            GlassSearchBar(
                query = if (currentTab == SlateTab.NOTES) notesQuery else tasksQuery,
                onQueryChange = { newQuery ->
                    if (currentTab == SlateTab.NOTES) {
                        notesViewModel.onSearchQueryChanged(newQuery)
                    } else {
                        tasksViewModel.onSearchQueryChanged(newQuery)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Liquid Sliding Glass Pill Tab Selector
            LiquidSegmentedControl(
                selectedTab = currentTab,
                onTabSelected = { currentTab = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Animated Tab Content View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith
                                fadeOut(animationSpec = tween(220))
                    },
                    label = "TabContentAnimation"
                ) { tab ->
                    when (tab) {
                        SlateTab.NOTES -> NotesScreen(
                            viewModel = notesViewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                        SlateTab.TASKS -> TasksScreen(
                            viewModel = tasksViewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // Tactile Spring Liquid FAB (Floating Action Button)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 20.dp)
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            FluidCyan,
                            Color(0xFF00D4B2)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    ),
                    shape = CircleShape
                )
                .bouncyClickable(
                    scaleDown = 0.90f,
                    onClick = {
                        if (currentTab == SlateTab.NOTES) {
                            notesViewModel.openCreateNote()
                        } else {
                            tasksViewModel.openCreateTask()
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = if (currentTab == SlateTab.NOTES) "Create Note" else "Create Task",
                tint = ObsidianDeep,
                modifier = Modifier.size(28.dp)
            )
        }

        // Privacy Policy Dialog
        if (showPrivacyDialog) {
            PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
        }
    }
}
