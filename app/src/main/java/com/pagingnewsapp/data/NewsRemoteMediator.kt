package com.pagingnewsapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pagingnewsapp.api.NewsService
import com.pagingnewsapp.db.NewsDatabase
import com.pagingnewsapp.db.RemoteKeys
import com.pagingnewsapp.model.Article
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val query: String,
    private val service: NewsService,
    private val newsDatabase: NewsDatabase
) : RemoteMediator<Int, Article>() {

    init {

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {

        Log.d("testing", "loadType : " + loadType)

        val page = when (loadType) {
            LoadType.REFRESH -> {
                null
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                if (remoteKeys == null) {
//                    // The LoadType is PREPEND so some data was loaded before,
//                    // so we should have been able to get remote keys
//                    // If the remoteKeys are null, then we're an invalid state and we have a bug
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
//                }
//                // If the previous key is null, then we can't request more data
//                val prevKey = remoteKeys.prevKey
//                if (prevKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                remoteKeys.prevKey
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(query)
                val remoteKeys = newsDatabase.withTransaction {
                    newsDatabase.remoteKeysDao().remoteKeysRepoId(query)
                }
                if (remoteKeys == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                if (remoteKeys.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.nextKey
            }
        }

        val apiQuery = query

        try {
            Log.d("testing", "api call : page " + page + " page size : " + state.config.pageSize)
            val apiResponse = service.getNews(apiQuery, page ?: 1, state.config.pageSize)

            val articlesList = apiResponse.articles
            val endOfPaginationReached = articlesList?.isEmpty() ?: false

            var articleListTemp = ArrayList<Article>()
            articlesList?.forEach { it ->
                run {
                    val articleObject = Article(
                        it?.source?.id ?: "",
                        it?.source?.name ?: "",
                        it?.author ?: "",
                        it.title,
                        it?.description ?: "",
                        it?.url ?: "",
                        it?.urlToImage ?: "",
                        it?.publishedAt ?: "",
                        it?.content ?: ""
                    )
                    articleListTemp.add(articleObject)
                }
            }

            newsDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.remoteKeysDao().clearRemoteKeys()
                    newsDatabase.reposDao().clearArticles()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page ?: 1 - 1
                val nextKey = if (endOfPaginationReached) null else {
                    if(page!= null) page + 1
                    else 1
                }
                Log.d("testing", "prevKey : " + prevKey + " next key " + nextKey  + " page " + page)
//                val keys = articlesList?.map {
//                    RemoteKeys(repoId = it.title, prevKey = prevKey, nextKey = nextKey)
//                }
                newsDatabase.remoteKeysDao()
                    .insert(RemoteKeys(query, prevKey = prevKey, nextKey = nextKey))
                newsDatabase.reposDao().insertAll(articleListTemp)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item

//        return newsDatabase.remoteKeysDao().getRemoteKeys().firstOrNull()

        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                newsDatabase.remoteKeysDao().remoteKeysRepoId(repo.title)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                newsDatabase.remoteKeysDao().remoteKeysRepoId(repo.title)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.title?.let { title ->
                newsDatabase.remoteKeysDao().remoteKeysRepoId(title)
            }
        }
    }


}