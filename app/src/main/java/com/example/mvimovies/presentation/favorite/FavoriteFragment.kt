package com.example.mvimovies.presentation.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvimovies.MainActivity
import com.example.mvimovies.R
import com.example.mvimovies.databinding.FragmentFavoriteBinding
import com.example.mvimovies.domain.model.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showNavBar()
        setupRecyclerView()
        observeState()
        viewModel.processIntent(FavoriteIntent.LoadFavorites)
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(
            onRemoveClick = { movieId ->
                viewModel.processIntent(FavoriteIntent.RemoveFavorite(movieId))
            },
            onMovieClick = { movie ->
                val action =
                    FavoriteFragmentDirections.actionNavigationFavoriteToDetailsFragment(movie)
                findNavController().navigate(action)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is FavoriteState.Success -> showFavorites(state.movies)
                        is FavoriteState.Loading -> showLoading()
                        is FavoriteState.Error -> showError(state.message)
                        is FavoriteState.Empty -> showEmpty()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showFavorites(movies: List<Movie>) {
        binding.apply {
            progressBar.visibility = View.GONE
            errorMessage.visibility = View.GONE
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        adapter.submitList(movies)
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            errorMessage.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.GONE
            errorMessage.text = message
        }
    }

    private fun showEmpty() {
        binding.apply {
            progressBar.visibility = View.GONE
            errorMessage.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }
}