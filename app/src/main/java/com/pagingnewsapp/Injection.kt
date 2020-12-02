package com.pagingnewsapp

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.pagingnewsapp.api.NewsService
import com.pagingnewsapp.data.NewsRepository
import com.pagingnewsapp.db.NewsDatabase
import com.pagingnewsapp.ui.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideNewsRepository(context: Context): NewsRepository {
        return NewsRepository(NewsService.create(), NewsDatabase.getInstance(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideNewsRepository(context))
    }
}
