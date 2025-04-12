package com.example.mvimovies.presentation.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mvimovies.MainActivity
import com.example.mvimovies.R
import com.example.mvimovies.databinding.FragmentDetailsBinding
import com.example.mvimovies.domain.model.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = requireActivity() as MainActivity
        mainActivity.hideNavBar()
        showMovieDetails(args.movie)
    }


    private fun showMovieDetails(movie: Movie) {
        binding.apply {
            title.text = movie.title
            overview.text = movie.overview
            Log.d("hitler", "Showing movie details: ${movie.genreIds}")
            rating.text = "Genre: ${getGenre(movie.genreIds)}"
            releaseDate.text = "Released: ${movie.releaseDate}"

            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(poster)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.errorMessage.text = message
        binding.errorMessage.visibility = View.VISIBLE
    }

    private fun getGenre(genreIds: List<Int>?): String {
        if (genreIds == null) {
            return ""
        }
        val genreMap = mapOf(
            28 to "Action",
            12 to "Adventure",
            16 to "Animation",
            35 to "Comedy",
            80 to "Crime",
            99 to "Documentary",
            18 to "Drama",
            10751 to "Family",
            14 to "Fantasy",
            36 to "History",
            27 to "Horror",
            10402 to "Music",
            9648 to "Mystery",
            10749 to "Romance",
            878 to "Science Fiction",
            10770 to "TV Movie",
            53 to "Thriller",
            10752 to "War",
            37 to "Western"
        )

        val genreNames = mutableListOf<String>()
        for (genreId in genreIds) {
            genreMap[genreId]?.let { genreName ->
                genreNames.add(genreName)
            }
        }
        return genreNames.joinToString(", ")
    }

}