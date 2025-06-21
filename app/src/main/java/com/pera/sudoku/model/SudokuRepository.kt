package com.pera.sudoku.model

import javax.inject.Inject

class SudokuRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchNewBoard(): NewBoard {
        return apiService.getBoard().newboard
    }

    suspend fun fetchNewBoardWithDifficulty(difficulty: Difficulties): NewBoard {
        /* api does not provide a way to choose a difficulty, so the only way to do it is to get many boards until the requested difficulty is found.
        based on testing, the api tends to provide mostly medium and hard boards, so the function uses the single request for those difficulties.
        the easy difficulty is instead extremely rare, so we request many boards at once, which the api allows. while this method still leads to
        errors, it is the most consistent i have found using this api.
        * */
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