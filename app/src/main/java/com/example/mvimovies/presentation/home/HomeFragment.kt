package com.example.mvimovies.presentation.home

import android.os.Bundle
import android.util.Log
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
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mvimovies.MainActivity
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
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showNavBar()
        binding.progressBar.visibility = View.VISIBLE
        setupRecyclerView()
        observeState()
        setOnClickListener()
        viewModel.processIntent(HomeIntent.LoadMovies)
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(
            onFavoriteClick = { movieId, isFavorite ->
                viewModel.processIntent(HomeIntent.UpdateFavorite(movieId, isFavorite))
            },
            onMovieClick = { movie ->
                val action = HomeFragmentDirections.actionNavigationHomeToDetailsFragment(movie)
                findNavController().navigate(action)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
//            setHasFixedSize(true)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    adapter.loadStateFlow.collectLatest {loadStates ->
                        val errorState = loadStates.mediator?.refresh as? LoadState.Error
                        errorState?.let { loadError ->
                            val error = loadError.error
                            showError(error.message?:"Unknown error")
                        }
                    }
                }
                launch {
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
    }

    private fun handleSuccessState(state: HomeState.Success) {

        viewLifecycleOwner.lifecycleScope.launch {
            state.movies.collectLatest { pagingData ->
                adapter.submitData(pagingData)
                binding.apply {
                    Log.d("hitler", "handle success state")
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
//                binding.recyclerView.layoutManager?.scrollToPosition(state.scrollPosition)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            Log.d("hitler", "Loading")
            progressBar.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        binding.apply {
            Log.d("hitler", "error $message")
            progressBar.visibility = View.GONE
            errorMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            errorMessage.text = message
        }
    }

    private fun setOnClickListener(){
        binding.ivLinear.setOnClickListener {
            setLinearLayout()
        }
        binding.ivGrid.setOnClickListener {
            setGridLayout()
        }
    }

    private fun setLinearLayout(){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.ivGrid.alpha = 0.5f
        binding.ivLinear.alpha = 1.0f
    }

    private fun setGridLayout(){
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.ivLinear.alpha = 0.5f
        binding.ivGrid.alpha = 1.0f
    }
}