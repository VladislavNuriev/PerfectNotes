package com.example.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.models.Note
import com.example.notes.NotesCommand
import com.example.notes.NotesViewModel
import com.example.notes.R
import com.example.ui.OtherNotesColors
import com.example.ui.PinnedNotesColors
import com.example.util.DateFormatter


@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(),
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick =  onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_note),
                    contentDescription = "Add note"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        )
        {
            item {
                Title(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "All Notes"
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    query = state.query,
                    onQueryChanged = {
                        viewModel.processCommand(
                            NotesCommand.InputSearchQuery(query = it)
                        )
                    }
                )
            }
            item {
                Spacer(Modifier.height(24.dp))
            }
            item {
                Subtitle(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Pinned"
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier
                        .fillParentMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    itemsIndexed(
                        items = state.pinnedNotes,
                        key = { _, note -> note.id }
                    ) { index, note ->
                        NoteCard(
                            modifier = Modifier.widthIn(max = 160.dp),
                            note = note,
                            backgroundColor = PinnedNotesColors[index % PinnedNotesColors.size],
                            onNoteClick = onNoteClick,
                            onLongClick = {
                                viewModel.processCommand(
                                    NotesCommand.SwitchPinnedStatus(it.id)
                                )
                            }
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(24.dp))
            }
            item {
                Subtitle(modifier = Modifier.padding(horizontal = 24.dp), text = "Others")
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            itemsIndexed(
                items = state.otherNotes,
                key = { _, note -> note.id }
            ) { index, note ->
                NoteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    note = note,
                    backgroundColor = OtherNotesColors[index % OtherNotesColors.size],
                    onNoteClick = onNoteClick,
                    onLongClick = {
                        viewModel.processCommand(
                            NotesCommand.SwitchPinnedStatus(it.id)
                        )
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.DEFAULT)
@Composable
fun MyPreview() {
    Column {
        SearchBar(query = "My little", onQueryChanged = {})
        Title(text = "All Notes")
        Subtitle(text = "Pinned")
//        NoteCard() { }
    }

}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(10.dp)
            ),
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                "Search note...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search notes",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedTextColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}


@Composable
private fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = { onNoteClick(note) },
                onLongClick = { onLongClick(note) }
            )
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = DateFormatter.formatDateToString(note.updatedAt),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = note.content,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}