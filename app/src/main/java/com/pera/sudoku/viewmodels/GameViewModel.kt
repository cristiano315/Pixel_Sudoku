package com.pera.sudoku.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pera.sudoku.model.NewBoard
import com.pera.sudoku.model.SudokuRepository
import com.pera.sudoku.model.defaultMatrix
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
    lateinit var newBoard: NewBoard
    private val maxErrors = 2

    private val _board = MutableStateFlow(defaultMatrix)
    val board: StateFlow<List<List<Int>>> = _board

    lateinit var solution: List<List<Int>>

    private val _timer = MutableStateFlow(0L)
    val timer: StateFlow<Long> = _timer

    private var timerJob: Job? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _focusedCell = MutableStateFlow(Pair<Int, Int>(0, 0))
    val focusedCell: StateFlow<Pair<Int, Int>> = _focusedCell

    private val _errorCount = MutableStateFlow(0)
    val errorCount: StateFlow<Int> = _errorCount

    private val _isLost = MutableStateFlow(false)
    val isLost: StateFlow<Boolean> = _isLost

    private val _errorTrigger = MutableStateFlow(false)
    val errorTrigger: MutableStateFlow<Boolean> = _errorTrigger

    fun startGame() {
        getNewBoard()
        startTimer()
    }

    fun getNewBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val result = repository.fetchNewBoard()
            newBoard = result
            _board.value = result.grids[0].value
            solution = result.grids[0].solution

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

    fun focusCell(row: Int, col: Int){
        _focusedCell.value = Pair(row, col)
    }

    fun checkInputNumber(input: Int){
        val row = _focusedCell.value.first
        val col = _focusedCell.value.second
        val isEmpty = (_board.value[row][col] == 0)
        val solValue = solution[row][col]
        if(isEmpty){
            if(input == solValue){
                //rebuild board with updated value
                _board.value = _board.value.mapIndexed { r, currentRow ->
                    if (r == row){
                        currentRow.mapIndexed{ c, currentItem ->
                            if (c == col) solValue
                            else currentItem
                        }
                    }
                    else currentRow
                }
            }
            else {
                _errorCount.value += 1
                _errorTrigger.value = true
                if(_errorCount.value > maxErrors) loseGame()
            }
        }
    }

    fun loseGame(){
        _isLost.value = true
    }

    fun resetTrigger(){
        _errorTrigger.value = false
    }

    fun saveGame(){} //placeholder

    init {
        startGame()
    }
}
