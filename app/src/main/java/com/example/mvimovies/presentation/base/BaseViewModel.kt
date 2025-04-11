package com.example.mvimovies.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : BaseState, Intent : BaseIntent> : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state: StateFlow<State?> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Any>()
    val effect: SharedFlow<Any> = _effect.asSharedFlow()

    protected fun setState(state: State) {
        _state.value = state
    }

    protected fun setEffect(effect: Any) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    abstract fun processIntent(intent: Intent)
}