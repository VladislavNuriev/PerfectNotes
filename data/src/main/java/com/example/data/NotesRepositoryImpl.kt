package com.example.data

import android.content.Context
import com.example.database.NoteEntity
import com.example.database.NotesDataBase
import com.example.domain.NotesRepository
import com.example.domain.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl private constructor(context: Context) : NotesRepository {
    private val notesDatabase = NotesDataBase.getInstance(context)
    private val notesDao = notesDatabase.notesDao()


    override suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val noteEntity = NoteEntity(
            id = 0,
            title = title,
            content = content,
            isPinned = isPinned,
            updatedAt = updatedAt
        )
        notesDao.addNote(noteEntity)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toNoteEntity())
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

    companion object {
        private val LOCK = Any()
        private var instance: NotesRepositoryImpl? = null

        fun getInstance(context: Context): NotesRepositoryImpl {
            instance?.let { return it }

            synchronized(LOCK) {
                instance?.let { return it }
                return NotesRepositoryImpl(context).also {
                    instance = it
                }
            }
        }
    }
}