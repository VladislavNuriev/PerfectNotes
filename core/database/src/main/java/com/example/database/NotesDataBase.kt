package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NotesDataBase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        private var instance: NotesDataBase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): NotesDataBase {
            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    NotesDataBase::class.java,
                    "notes.db"
                )
                    .build()
                instance = db
                return db
            }
        }
    }
}