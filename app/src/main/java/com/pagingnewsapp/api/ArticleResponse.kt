package com.pagingnewsapp.api

data class ArticleResponse(
    val source: SourceResponse,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)