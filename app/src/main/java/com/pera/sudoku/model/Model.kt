package com.pera.sudoku.model

//api
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

//for viewModel
data class Cell(
    val row: Int,
    val col: Int,
    val value: Int,
    val isEditable: Boolean
)

//data class Cells(
//    val cells: List(9){ row ->
//        List(9) { col ->
//            Cell(row= row, col = col, value = null, isEditable = true)
//        }
//    }
//)