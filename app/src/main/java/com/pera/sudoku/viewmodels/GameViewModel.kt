package com.pera.sudoku.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pera.sudoku.model.NewBoard
import com.pera.sudoku.model.SudokuRepository
import com.pera.sudoku.model.emptyBoard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val repository: SudokuRepository) : ViewModel() {
    private val _board = MutableStateFlow<NewBoard>(emptyBoard)
    val board: StateFlow<NewBoard> = _board

    private val _timer = MutableStateFlow(0L)
    val timer: StateFlow<Long> = _timer

    private var timerJob: Job? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    fun startGame() {
        getNewBoard()
        startTimer()
    }

    fun getNewBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val result = repository.fetchNewBoard()
            _board.value = result
            _isLoading.value = false
        }
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000L)
                _timer.value += 1 //update timer
            }
        }
    }

    init {
        startGame()
    }
}
