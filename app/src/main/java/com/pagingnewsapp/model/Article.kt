package com.pagingnewsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Article")
data class Article(

    @SerializedName("sourceId")
    @ColumnInfo(name = "sourceId")
    val sourceId: String,

    @SerializedName("sourceName")
    @ColumnInfo(name = "sourceName")
    val sourceName: String,

    @SerializedName("author")
    @ColumnInfo(name = "author")
    val author: String,

    @PrimaryKey
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String,

    @SerializedName("url")
    @ColumnInfo(name = "url")
    val url: String,

    @SerializedName("urlToImage")
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String,

    @SerializedName("publishedAt")
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @SerializedName("content")
    @ColumnInfo(name = "content")
    val content: String,

//    @PrimaryKey(autoGenerate = true)
//    @SerializedName("id")
//    @ColumnInfo(name = "id")
//    val id: Long = 0

)