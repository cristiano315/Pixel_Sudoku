package com.pera.sudoku.ui.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pera.sudoku.viewmodels.HistoryViewModel

@Composable
fun HistoryView(
    modifier: Modifier = Modifier,
    isPortrait: Boolean,
    viewModel: HistoryViewModel = hiltViewModel(),
    navController: NavController
){
    if(isPortrait){
        Text("ciao")
    }
}