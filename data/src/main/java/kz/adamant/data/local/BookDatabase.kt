package kz.adamant.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.adamant.data.local.dao.BooksDao
import kz.adamant.data.local.dao.GenresDao
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.local.models.GenreEntity

@Database(entities = [BookEntity::class, GenreEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun booksDao(): BooksDao

    abstract fun genresDao(): GenresDao

    companion object {

        private var INSTANCE: BookDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): BookDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        BookDatabase::class.java, "Books.db")
                        .build()
                }
                return INSTANCE!!
            }
        }
    }

}