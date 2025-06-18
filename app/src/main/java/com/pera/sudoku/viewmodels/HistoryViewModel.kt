package com.pera.sudoku.viewmodels

import androidx.lifecycle.ViewModel
import com.pera.sudoku.model.SavedGameDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val dao: SavedGameDao): ViewModel() {

}