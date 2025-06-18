package com.pera.sudoku.model

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update

@Dao
interface SavedGameDao{

    @Insert
    fun insert(game: SavedGame)

    @Update
    fun update(game: SavedGame)

    @Delete
    fun delete(game: SavedGame)

    @Query("SELECT * FROM saved_games")
    fun loadAll(): Array<SavedGame>

    @Query("SELECT * FROM saved_games WHERE date = :valDate")
    fun loadByDate(valDate: Long): Array<SavedGame>
}

@Database(entities = [SavedGame::class], version = 1)
@TypeConverters(Converters::class)
abstract class SudokuDatabase : RoomDatabase(){
    abstract fun savedGameDao(): SavedGameDao
}