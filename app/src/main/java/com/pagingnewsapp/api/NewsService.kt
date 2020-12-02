package com.pagingnewsapp.api

import com.pagingnewsapp.api.NewsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val IN_QUALIFIER = "in:name,description"

interface NewsService {


    @GET(BASE_URL)
    suspend fun getNews(
        @Query(QUERY_PARAM) q: String,
        @Query(PAGE_NUMBER) page: Int,
        @Query(PAGE_SIZE) itemsPerPage: Int,
        @Header(API_KEY_HEADER_FIELD_NAME)key:String = API_KEY,
    ): NewsResponse

    companion object {

        fun create(): NewsService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsService::class.java)
        }
    }
}