package com.example.mvimovies.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvimovies.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(
            onFavoriteClick = { movieId, isFavorite ->
                viewModel.processIntent(HomeIntent.UpdateFavorite(movieId, isFavorite))
            },
            onScrollPositionChanged = { position ->
//                viewModel.processIntent(HomeIntent.UpdateScrollPosition(position))
            },
            onMovieClick = { movie ->
                val action = HomeFragmentDirections.actionNavigationHomeToDetailsFragment(movie)
                findNavController().navigate(action)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is HomeState.Success -> handleSuccessState(state)
                        is HomeState.Loading -> showLoading()
                        is HomeState.Error -> showError(state.message)
                        HomeState.Idle -> Unit
                        null -> Unit
                    }
                }
            }
        }
    }

    private fun handleSuccessState(state: HomeState.Success) {
        binding.apply {
            progressBar.visibility = View.GONE
            errorMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            state.movies.collectLatest { pagingData ->
                adapter.submitData(pagingData)
                binding.recyclerView.layoutManager?.scrollToPosition(state.scrollPosition)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            errorMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            errorMessage.text = message
        }
    }
}