package com.pera.sudoku

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalConfiguration
import com.pera.sudoku.ui.navigation.SudokuNavHost
import com.pera.sudoku.ui.theme.SudokuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //@Inject lateinit var test1234: Testt //remove later
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuTheme {
                val configuration = LocalConfiguration.current
                val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                SudokuNavHost(isPortrait)
            }
        }
    }
}
