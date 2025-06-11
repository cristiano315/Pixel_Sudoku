package com.pera.sudoku.model

import retrofit2.http.GET

interface ApiService {
    @GET(" ")
    suspend fun getBoard(): Board
}