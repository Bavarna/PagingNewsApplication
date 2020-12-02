package com.pagingnewsapp.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.pagingnewsapp.model.Article

@Dao
interface NewsDBDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Article>)

    @Query("SELECT * FROM Article WHERE " +
            "title LIKE :queryString OR description LIKE :queryString")
    fun newsByName(queryString: String): PagingSource<Int, Article>

    @Query("DELETE FROM Article")
    suspend fun clearArticles()


}