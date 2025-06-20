package com.pera.sudoku.model

import retrofit2.http.GET

interface ApiService {
    @GET(" ")
    suspend fun getBoard(): Board

    @GET("?query={newboard(limit:5000){grids{value,solution,difficulty},results,message}}")
    suspend fun getManyBoards(): Board
}