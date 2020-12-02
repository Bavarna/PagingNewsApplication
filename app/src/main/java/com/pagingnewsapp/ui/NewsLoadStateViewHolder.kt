package com.pagingnewsapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.pagingnewsapp.R
import com.pagingnewsapp.databinding.FooterBinding

class NewsLoadStateViewHolder(
    private val binding: FooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        if(loadState is LoadState.Loading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.retryButton.visibility = View.INVISIBLE
            binding.errorMsg.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.retryButton.visibility = View.VISIBLE
            binding.errorMsg.visibility = View.VISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): NewsLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer, parent, false)
            val binding = FooterBinding.bind(view)
            return NewsLoadStateViewHolder(binding, retry)
        }
    }
}
