package com.pera.sudoku.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.pera.sudoku.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

object SudokuTextStyles{
    private val SudokuFont = FontFamily(Font(R.font.sudokufont))
    val veryBigTitle = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold,
        color = ContentColor
    )
    val bigTitle = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = ContentColor
    )
    val titleScreen = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 100.sp,
        fontWeight = FontWeight.Bold,
        color = ContentColor
    )
    val landscapeTitleScreen = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 120.sp,
        fontWeight = FontWeight.Bold,
        color = ContentColor
    )
    val cellNumber = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 35.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    val numberButton = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 45.sp,
    )
    val genericButton = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 30.sp,
        color = Color.White
    )
    val homeButton = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 55.sp,
        color = Color.White
    )
    val gameItem = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 30.sp,
    )
    val smallGenericButton = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 25.sp,
        color = Color.White
    )
    val cellAnnotations = TextStyle(
        fontFamily = SudokuFont,
        fontSize = 16.sp,
        color = Color.Black
    )
}

@Composable
fun DynamicMaxTextSize(
    baseFontSize: TextUnit = 24.sp,
    maxFontSize: TextUnit = 30.sp,
) : TextUnit{
    val fontScale = LocalConfiguration.current.fontScale
    val scaledFont = (baseFontSize.value * fontScale).coerceAtMost(maxFontSize.value).sp
    return scaledFont
}