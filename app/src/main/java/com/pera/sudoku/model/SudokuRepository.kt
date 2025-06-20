package com.pera.sudoku.model

import javax.inject.Inject

class SudokuRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchNewBoard(): NewBoard {
        return apiService.getBoard().newboard
    }

    suspend fun fetchNewBoardWithDifficulty(difficulty: Difficulties): NewBoard {
        while (true) {
            if(difficulty == Difficulties.Easy){
                val newBoard = apiService.getManyBoards().newboard
                newBoard.grids.forEach {
                    if (it.difficulty == difficulty.name) {
                        return NewBoard(listOf(it), "", 0)
                    }
                }
            }
            else{
                val newBoard = apiService.getBoard().newboard
                if (newBoard.grids[0].difficulty == difficulty.name)
                    return newBoard
            }
        }
    }

}