package com.pagingnewsapp.data


import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pagingnewsapp.api.NewsService
import com.pagingnewsapp.db.NewsDatabase
import com.pagingnewsapp.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that works with local and remote data sources.
 */
class NewsRepository(
    private val service: NewsService,
    private val database: NewsDatabase
) {

    fun getSearchResultStream(query: String): Flow<PagingData<Article>> {
        Log.d("repository", "New query: $query")

        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.reposDao().newsByName(dbQuery) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false, initialLoadSize= 3),
            remoteMediator = NewsRemoteMediator(
                query,
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 5
    }
}
