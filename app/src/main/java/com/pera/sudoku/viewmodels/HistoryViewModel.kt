package com.pera.sudoku.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pera.sudoku.R
import com.pera.sudoku.model.FilterType
import com.pera.sudoku.model.SavedGame
import com.pera.sudoku.model.SavedGameDao
import com.pera.sudoku.model.SortFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val dao: SavedGameDao): ViewModel() {
    private val _games = MutableStateFlow(listOf(SavedGame(0L)))
    val games: StateFlow<List<SavedGame>> = _games

    private var loadFilter = Pair<FilterType, String>(FilterType.DIFFICULTY, "")

    private var sortFilter = SortFilter.TIME_ASCENDING

    init {
        loadGames()
    }

    fun loadGames(filter: String = "", type: FilterType = FilterType.DIFFICULTY, context: Context? = null){
        viewModelScope.launch(Dispatchers.IO) {
            if(context != null) getCorrectFilter(filter, context, type)
            if(loadFilter.second.isBlank()){
                _games.value = dao.loadAll().toList()
            }
            else{
                when(loadFilter.first){
                    FilterType.DIFFICULTY -> _games.value = dao.loadByDifficulty(loadFilter.second).toList()
                    FilterType.RESULT -> _games.value = dao.loadByResult(loadFilter.second).toList()
                }
            }
            sortGames()
        }
    }

    fun deleteGame(game: SavedGame){
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(game)
            loadGames()
        }
    }

    fun getCorrectFilter(filter: String, context: Context?, type: FilterType){
        var correctFiler: String = when(filter){
            context?.getString(R.string.easy) -> "Easy"
            context?.getString(R.string.medium) -> "Medium"
            context?.getString(R.string.hard) -> "Hard"

            context?.getString(R.string.wins) -> "Win"
            context?.getString(R.string.losses) -> "Lose"

            else -> ""
        }
        loadFilter = Pair(type, correctFiler)
    }

    fun sortGames(method: String = "", context: Context? = null){
        if(context != null){
            if(method == context.getString(R.string.time_lowest)){
                sortFilter = SortFilter.TIME_ASCENDING
            }
            else if (method == context.getString(R.string.time_highest)){
                sortFilter = SortFilter.TIME_DESCENDING
            }
        }
        when(sortFilter){
            SortFilter.TIME_ASCENDING -> _games.value = _games.value.sortedBy { it.time }
            SortFilter.TIME_DESCENDING -> _games.value = _games.value.sortedByDescending { it.time }
        }
    }

}