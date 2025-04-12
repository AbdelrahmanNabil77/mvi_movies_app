package com.example.mvimovies.presentation.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.mvimovies.databinding.ItemMovieBinding
import com.example.mvimovies.domain.model.Movie

class HomeAdapter(
    private val onFavoriteClick: (Int, Boolean) -> Unit,
    private val onMovieClick: (Movie) -> Unit,
) : PagingDataAdapter<Movie, HomeAdapter.ViewHolder>(MovieDiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie)
        }
    }

    inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                title.text = movie.title
                overview.text = movie.releaseDate
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                    .into(poster)
                if (movie.isFavorite == false) {
                    favoriteButton.setBackgroundResource(com.example.mvimovies.R.drawable.ic_border_favorite)
                } else {
                    favoriteButton.setBackgroundResource(com.example.mvimovies.R.drawable.ic_solid_red_heart)
                }

                favoriteButton.setOnClickListener {
                    val isFavorite = movie.isFavorite ?: false
                    onFavoriteClick(movie.id ?: 0, !isFavorite)
                }

                itemView.setOnClickListener {
                    onMovieClick(movie)
                }
            }

        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
}
