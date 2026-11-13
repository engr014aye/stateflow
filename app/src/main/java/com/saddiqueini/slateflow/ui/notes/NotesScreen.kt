package com.saddiqueini.slateflow.ui.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saddiqueini.slateflow.R
import com.saddiqueini.slateflow.data.model.GlassTint
import com.saddiqueini.slateflow.data.model.NoteEntity
import com.saddiqueini.slateflow.ui.components.LiquidGlassCard
import com.saddiqueini.slateflow.ui.components.bouncyClickable
import com.saddiqueini.slateflow.ui.theme.FluidCyan
import com.saddiqueini.slateflow.ui.theme.TextMuted
import com.saddiqueini.slateflow.ui.theme.TextPrimary
import com.saddiqueini.slateflow.ui.theme.TextSecondary
import com.saddiqueini.slateflow.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    modifier: Modifier = Modifier
) {
    val notes by viewModel.notes.collectAsState()
    val isGridView by viewModel.isGridView.collectAsState()
    val isEditorOpen by viewModel.isEditorOpen.collectAsState()
    val editingNote by viewModel.editingNote.collectAsState()

    val pinnedNotes = notes.filter { it.isPinned }
    val otherNotes = notes.filter { !it.isPinned }

    Box(modifier = modifier.fillMaxSize()) {
        if (notes.isEmpty()) {
            NotesEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
        } else {
            if (isGridView) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    if (pinnedNotes.isNotEmpty()) {
                        items(pinnedNotes, key = { "pinned_${it.id}" }) { note ->
                            NoteCardItem(
                                note = note,
                                onNoteClick = { viewModel.openEditNote(note) },
                                onTogglePin = { viewModel.togglePin(note) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            )
                        }
                    }

                    items(otherNotes, key = { "note_${it.id}" }) { note ->
                        NoteCardItem(
                            note = note,
                            onNoteClick = { viewModel.openEditNote(note) },
                            onTogglePin = { viewModel.togglePin(note) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (pinnedNotes.isNotEmpty()) {
                        items(pinnedNotes, key = { "pinned_${it.id}" }) { note ->
                            NoteCardItem(
                                note = note,
                                onNoteClick = { viewModel.openEditNote(note) },
                                onTogglePin = { viewModel.togglePin(note) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            )
                        }
                    }

                    items(otherNotes, key = { "note_${it.id}" }) { note ->
                        NoteCardItem(
                            note = note,
                            onNoteClick = { viewModel.openEditNote(note) },
                            onTogglePin = { viewModel.togglePin(note) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        )
                    }
                }
            }
        }

        if (isEditorOpen) {
            NoteEditorDialog(
                note = editingNote,
                onSave = { title, content, isPinned, colorIndex ->
                    viewModel.saveNote(title, content, isPinned, colorIndex)
                },
                onDelete = { noteToDelete ->
                    viewModel.deleteNote(noteToDelete)
                },
                onDismiss = { viewModel.closeEditor() }
            )
        }
    }
}

@Composable
fun NoteCardItem(
    note: NoteEntity,
    onNoteClick: () -> Unit,
    onTogglePin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = GlassTint.fromId(note.colorIndex)
    val formattedDate = remember(note.timestamp) {
        val formatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
        formatter.format(Date(note.timestamp))
    }

    LiquidGlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        backgroundColor = tint.surfaceColor,
        contentPadding = 16.dp,
        onClick = onNoteClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (note.title.isNotEmpty()) {
                    Text(
                        text = note.title,
                        style = Typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            if (note.isPinned) FluidCyan.copy(alpha = 0.20f)
                            else Color.Transparent
                        )
                        .bouncyClickable(onClick = onTogglePin),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = "Pin toggle",
                        tint = if (note.isPinned) FluidCyan else TextMuted.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            if (note.content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.content,
                    style = Typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp
                    ),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = formattedDate,
                style = Typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = TextMuted
                )
            )
        }
    }
}

@Composable
fun NotesEmptyState(modifier: Modifier = Modifier) {
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
                imageVector = Icons.Outlined.NoteAlt,
                contentDescription = null,
                tint = FluidCyan,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.no_notes_title),
            style = Typography.titleLarge.copy(fontSize = 20.sp),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.no_notes_subtitle),
            style = Typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(horizontal = 24.dp),
            lineHeight = 20.sp
        )
    }
}
