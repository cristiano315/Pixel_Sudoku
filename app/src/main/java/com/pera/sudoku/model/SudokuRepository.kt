package com.pera.sudoku.model

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import javax.inject.Inject

//REDO WITH RETROFIT AND HILT
class SudokuRepository @Inject constructor(private val apiService: ApiService){

    suspend fun fetchNewBoard(): NewBoard{
        return apiService.getBoard().newboard
    }

    suspend fun fetchNewBoardWithDifficulty(difficulty: Difficulties): NewBoard{
        while(true){
            val newBoard = apiService.getBoard().newboard
            if(newBoard.grids[0].difficulty == difficulty.name)
                return newBoard
        }
    }

//    fun fetchNewsBoardWithDifficulty(difficulty: Difficulties, onResult: (NewBoard?) -> Unit){
//        val queue = Volley.newRequestQueue(context)
//
//        val request = StringRequest(Request.Method.GET, url,
//            { response ->
//                val newBoard = Gson().fromJson(response, Board::class.java).newboard
//                if(newBoard.grids[0].difficulty == difficulty.name)
//                    onResult(newBoard)
//                else
//                    fetchNewBoardWithDifficulty(difficulty, onResult)
//            },
//            { error ->
//                error.printStackTrace()
//                onResult(null)
//            })
//
//        queue.add(request)
//    }
}