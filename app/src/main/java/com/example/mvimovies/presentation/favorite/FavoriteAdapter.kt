package com.example.mvimovies.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.mvimovies.databinding.ItemMovieBinding
import com.example.mvimovies.domain.model.Movie

class FavoriteAdapter(
    private val onRemoveClick: (Int) -> Unit,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val movies = mutableListOf<Movie>()

    inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                title.text = movie.title
                overview.text = movie.overview
                favoriteButton.isChecked = movie.isFavorite?:true

                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                    .into(poster)

                favoriteButton.apply {
                    setOnClickListener { onRemoveClick(movie.id?:0) }
                }
                itemView.setOnClickListener {
                    onMovieClick(movie)
                }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun submitList(newList: List<Movie>) {
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }
}
