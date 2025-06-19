package com.pera.sudoku.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pera.sudoku.model.Difficulties
import com.pera.sudoku.model.GameState
import com.pera.sudoku.model.NewBoard
import com.pera.sudoku.model.Results
import com.pera.sudoku.model.SavedGame
import com.pera.sudoku.model.SavedGameDao
import com.pera.sudoku.model.SudokuRepository
import com.pera.sudoku.model.defaultMatrix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val repository: SudokuRepository, private val dao: SavedGameDao) : ViewModel() {
    lateinit var newBoard: NewBoard
    private val maxErrors = 2
    private var correctCells = 0

    //active board
    private val _board = MutableStateFlow(defaultMatrix)
    val board: StateFlow<List<List<Int>>> = _board

    //solution
    lateinit var solution: List<List<Int>>

    //timer
    private val _timer = MutableStateFlow(0L)
    val timer: StateFlow<Long> = _timer

    //to avoid reactivating timer
    private var timerJob: Job? = null

    //keep track of game state
    private val _gameState = MutableStateFlow(GameState.LOADING)
    val gameState: StateFlow<GameState> = _gameState

    //keep track of active cell
    private val _focusedCell = MutableStateFlow(Pair<Int, Int>(0, 0))
    val focusedCell: StateFlow<Pair<Int, Int>> = _focusedCell

    //errors
    private val _errorCount = MutableStateFlow(0)
    val errorCount: StateFlow<Int> = _errorCount

    //for red effect
    private val _errorTrigger = MutableStateFlow(false)
    val errorTrigger: MutableStateFlow<Boolean> = _errorTrigger

    fun startGame() {
        getNewBoard()
        startTimer()
        countCorrectCells()
    }

    fun getNewBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            _gameState.value = GameState.LOADING

            val result = repository.fetchNewBoard()
            newBoard = result
            _board.value = result.grids[0].value
            solution = result.grids[0].solution

            _gameState.value = GameState.PLAYING
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

    fun countCorrectCells(){
        correctCells = _board.value.flatten().count { it != 0}
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
                correctCells++
                if (correctCells == 81){
                    winGame()
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
        _gameState.value = GameState.LOST
        timerJob?.cancel()
    }

    fun winGame(){
        _gameState.value = GameState.WON
        timerJob?.cancel()
    }

    fun resetTrigger(){
        _errorTrigger.value = false
    }

    fun saveGame(){
        val entry = SavedGame(_timer.value)
        entry.result = if (_gameState.value == GameState.WON) Results.Win else Results.Lose
        entry.difficulty = Difficulties.valueOf(newBoard.grids[0].difficulty)
        entry.date = Date(System.currentTimeMillis())

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(entry)
        }
    }

    init {
        startGame()
    }
}
