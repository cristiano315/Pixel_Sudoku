package com.pera.sudoku.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//api
data class Grid(
    val difficulty: String,
    val solution: List<List<Int>>,
    val value: List<List<Int>>
)

val defaultMatrix: List<List<Int>> = List(9) { List(9) { 0 } }

val emptyGrid = Grid(
    difficulty = "easy",
    solution = defaultMatrix,
    value = defaultMatrix
)

data class NewBoard(
    val grids: List<Grid>,
    val message: String,
    val results: Int
)

val emptyBoard = NewBoard(
    grids = listOf(emptyGrid),
    message = "",
    results = 0
)

data class Board(
    val newboard: NewBoard
)

enum class Difficulties {
    Hard,
    Medium,
    Easy
}

enum class GameState{
    LOADING,
    PLAYING,
    PAUSED,
    WON,
    LOST
}

enum class SortFilter{
    TIME_DESCENDING,
    TIME_ASCENDING
}

enum class FilterType{
    DIFFICULTY,
    RESULT
}

enum class Results {
    Win,
    Lose
}

//DB
@Entity(tableName = "saved_games")
data class SavedGame(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var difficulty: Difficulties,
    var time: Long,
    var result: Results,
    var date: Date
) {
    constructor(value: Long) : this(0, Difficulties.Medium, 0L, Results.Lose, Date(0L)){
        this.time = value
    }
}

