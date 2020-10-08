package kz.aviata.news.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import kz.aviata.news.data.models.ArticlesItem

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticlesItem)

    @Delete
    suspend fun delete(article: ArticlesItem)

    @Query("SELECT * FROM News")
    fun getAllArticles(): LiveData<List<ArticlesItem>>
}