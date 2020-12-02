package com.pagingnewsapp.api

data class NewsResponse(
    val status: String?,
    val totalResults: String?,
    val articles: List<ArticleResponse>?
)