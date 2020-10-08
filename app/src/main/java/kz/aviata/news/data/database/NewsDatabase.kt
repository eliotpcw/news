package kz.aviata.news.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.aviata.news.data.models.ArticlesItem


@Database(
    entities = [ArticlesItem::class],
    version = 3,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): Dao
}
