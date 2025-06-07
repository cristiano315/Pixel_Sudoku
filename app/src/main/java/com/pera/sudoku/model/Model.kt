package com.pera.sudoku.model

data class Grid(
    val difficulty: String,
    val solution: List<List<Int>>,
    val value: List<List<Int>>
)

data class NewBoard(
    val grids: List<Grid>,
    val message: String,
    val results: Int
)

data class Board(
    val newboard: NewBoard
)

enum class Difficulties{
    Hard,
    Medium,
    Easy
}