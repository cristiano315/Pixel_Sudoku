package com.pera.sudoku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pera.sudoku.ui.views.GameView
import com.pera.sudoku.ui.views.HistoryView
import com.pera.sudoku.ui.views.HomeScreenView
import com.pera.sudoku.viewmodels.GameViewModel

const val HOME_SCREEN_ROUTE = "homeScreen"
const val GAME_SCREEN_ROUTE = "gameScreen/"
const val HISTORY_SCREEN_ROUTE = "historyScreen"

@Composable
fun SudokuNavHost(isPortrait: Boolean) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = HOME_SCREEN_ROUTE) {

        composable("homeScreen") {
            HomeScreenView(isPortrait = isPortrait, navController = navController)
        }

        composable(
            route = "gameScreen/{difficulty}",
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) {
            val viewModel: GameViewModel = hiltViewModel()
            GameView(isPortrait = isPortrait, navController = navController, viewModel = viewModel)
        }

        composable("historyScreen") {
            HistoryView(isPortrait = isPortrait, navController = navController)
        }
    }
}

fun getGameRouteWithDifficulty(difficulty: String): String{
    return "$GAME_SCREEN_ROUTE$difficulty"
}