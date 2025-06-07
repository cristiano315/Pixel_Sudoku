package com.pera.sudoku.model

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class SudokuRepository(private val context: Context) {
    private val url = "https://sudoku-api.vercel.app/api/dosuku"

    fun fetchNewBoard(onResult: (NewBoard?) -> Unit){
        val queue = Volley.newRequestQueue(context)

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val newBoard = Gson().fromJson(response, Board::class.java).newboard
                onResult(newBoard)
            },
            { error ->
                error.printStackTrace()
                onResult(null)
            })

        queue.add(request)
    }

    fun fetchNewBoardWithDifficulty(difficulty: Difficulties, onResult: (NewBoard?) -> Unit){
        val queue = Volley.newRequestQueue(context)

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val newBoard = Gson().fromJson(response, Board::class.java).newboard
                if(newBoard.grids[0].difficulty == difficulty.name)
                    onResult(newBoard)
                else
                    fetchNewBoardWithDifficulty(difficulty, onResult)
            },
            { error ->
                error.printStackTrace()
                onResult(null)
            })

        queue.add(request)
    }
}