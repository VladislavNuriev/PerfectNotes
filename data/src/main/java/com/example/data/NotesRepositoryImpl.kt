package com.example.data

import com.example.database.NotesDao
import com.example.domain.NotesRepository
import com.example.domain.models.ContentItem
import com.example.domain.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val imageFileManager: ImageFileManager
) : NotesRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val note = Note(
            id = 0,
            title = title,
            content = content.processForStorage(),
            isPinned = isPinned,
            updatedAt = updatedAt
        )
        notesDao.addNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = getNote(noteId)
        notesDao.deleteNote(noteId)

        note.content
            .filterIsInstance<ContentItem.Image>()
            .map { it.url}
            .forEach {
                imageFileManager.deleteImage(it)
            }
    }

    override suspend fun editNote(note: Note) {
        val oldNote = getNote(note.id)

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val removedUrls = oldUrls - newUrls
        removedUrls.forEach { imageFileManager.deleteImage(it) }
        note.content.processForStorage().let {
            notesDao.addNote(note.copy(content = it).toNoteEntity())
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {
            it.toNotes()
        }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toNote()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map {
            it.toNotes()
        }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    private suspend fun List<ContentItem>.processForStorage(): List<ContentItem> {
        return map { contentItem ->
            when (contentItem) {
                is ContentItem.Text -> contentItem
                is ContentItem.Image -> {
                    if (imageFileManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        imageFileManager.copyImageToInternalStorage(contentItem.url).let {
                            ContentItem.Image(it)
                        }
                    }
                }
            }
        }
    }
}