package com.pera.sudoku.model

import retrofit2.http.GET

interface ApiService {
    @GET(" ")
    suspend fun getBoard(): Board

    //get 5000 boards, check repository for reasoning. 5000 seems like a good middle ground to avoid errors and spend a decently low amount of time
    @GET("?query={newboard(limit:5000){grids{value,solution,difficulty},results,message}}")
    suspend fun getManyBoards(): Board
}