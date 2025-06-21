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

data class NewBoard(
    val grids: List<Grid>,
    val message: String,
    val results: Int
)

data class Board(
    val newboard: NewBoard
)

//enums for internal states, avoid problems with strings translations
enum class Difficulties {
    Hard,
    Medium,
    Easy
}

enum class GameState{
    LOADING,
    LOADING_ERROR,
    PLAYING,
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

//function to get time in hh:mm:ss format from a long value
@Suppress("ConstantConditionIf")
fun Long.toTimeString(): String{
    if(this < 60L) return this.toString() //don't know why this is marked as always true, the function is working correctly
    else{
        var mins = this / 60
        var secs = this % 60
        if (mins < 60L) return "$mins:$secs"
        else{
            val hours = this / 3600
            mins = (this % 3600) / 60
            secs = this % 60
            return "$hours:$mins:$secs"
        }
    }
}

//DB entity
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

