package com.pera.sudoku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pera.sudoku.ui.views.GameView
import com.pera.sudoku.ui.views.HomeScreenView

@Composable
fun SudokuNavHost(isPortrait: Boolean) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "homeScreen") {

        composable("homeScreen") {
            HomeScreenView(isPortrait = isPortrait, navController = navController)
        }

        composable("gameScreen") {
            GameView(isPortrait = isPortrait, navController = navController)
        }
    }
}