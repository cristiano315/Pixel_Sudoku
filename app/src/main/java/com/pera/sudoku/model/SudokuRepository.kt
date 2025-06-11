package com.pera.sudoku.model

import javax.inject.Inject

class SudokuRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchNewBoard(): NewBoard {
        return apiService.getBoard().newboard
    }

    suspend fun fetchNewBoardWithDifficulty(difficulty: Difficulties): NewBoard {
        while (true) {
            val newBoard = apiService.getBoard().newboard
            if (newBoard.grids[0].difficulty == difficulty.name)
                return newBoard
        }
    }

}