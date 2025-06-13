package com.pera.sudoku.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pera.sudoku.R
import com.pera.sudoku.ui.theme.CellBackGroundColor
import com.pera.sudoku.ui.theme.CellBackGroundFocusedColor
import com.pera.sudoku.ui.theme.CellBackGroundRelatedColor
import com.pera.sudoku.ui.theme.CellBorderColor
import com.pera.sudoku.ui.theme.ContainerColor
import com.pera.sudoku.ui.theme.ContentColor
import com.pera.sudoku.ui.theme.SudokuTextStyles
import com.pera.sudoku.ui.theme.cellSize
import com.pera.sudoku.ui.theme.cellThickBorder
import com.pera.sudoku.ui.theme.cellThinBorder
import com.pera.sudoku.viewmodels.GameViewModel


@Composable
fun GameView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
    isPortrait: Boolean
) {
    //LaunchedEffect(Unit) { viewModel.startGame() } need to figure this out
    val board = viewModel.board.collectAsState()
    val timer = viewModel.timer.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val focusedCell = viewModel.focusedCell.collectAsState()

    when {
        isLoading.value -> {
            SudokuLoading()
        }

        !isLoading.value -> {
            if (isPortrait) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(ContainerColor)
                        .padding(vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameTimer(timer.value)
                    SudokuGrid(
                        grid = board.value.grids[0].value,
                        focusedCell = focusedCell.value
                    ) { row: Int, col: Int ->
                        viewModel.focusCell(row, col)
                    }
                    //hints
                    NumbersBar()
                }
            } else {
                Row {
                    GameTimer(timer.value)
                }
            }
        }
    }
}

@Composable
fun SudokuGrid(
    grid: List<List<Int>>,
    focusedCell: Pair<Int, Int>,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column {
        for (row in 0 until 9) {
            Row {
                for (col in 0 until 9) {
                    SudokuCell(row, col, grid[row][col], focusedCell) {
                        onCellClick(row, col)
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuCell(
    row: Int,
    col: Int,
    value: Int,
    focusedCell: Pair<Int, Int>,
    onCellClick: () -> Unit,
) {
    val backGroundColor = { row: Int, col: Int ->
        if (Pair(row, col) == focusedCell) CellBackGroundFocusedColor
        else if (checkIfRelated(row, col, focusedCell)) CellBackGroundRelatedColor
        else CellBackGroundColor
    }
    Box(
        modifier = Modifier
            .size(cellSize)
            .sudokuCellBorder(row, col)
            .clickable(onClick = onCellClick)
            .background(color = backGroundColor(row, col)),
        contentAlignment = Alignment.Center,
    ) {
        if (value != 0)
            Text(
                value.toString(),
                style = SudokuTextStyles.cellNumber
            )
    }
}

//highlight related cells
fun checkIfRelated(row: Int, col: Int, focusedCell: Pair<Int, Int>): Boolean {
    return (row == focusedCell.first
            || col == focusedCell.second
            || (row / 3 == focusedCell.first / 3 && col / 3 == focusedCell.second / 3))
}

@Composable
fun GameTimer(timer: Long) {
    Text(
        modifier = Modifier.padding(60.dp),
        text = stringResource(R.string.time, timer),
        style = SudokuTextStyles.bigTitle
    )
}

fun Modifier.sudokuCellBorder(row: Int, col: Int): Modifier {
    return this
        .size(cellSize)
        .drawBehind {
            val stroke =
                { isThick: Boolean -> if (isThick) cellThickBorder.toPx() else cellThinBorder.toPx() }
            val color = CellBorderColor

            //top border
            drawLine(
                color,
                Offset(0f, 0f),
                Offset(size.width, 0f),
                stroke(row % 3 == 0)
            )

            //left border
            drawLine(
                color,
                Offset(0f, 0f),
                Offset(0f, size.height),
                stroke(col % 3 == 0)
            )

            //bottom border
            drawLine(
                color,
                Offset(0f, size.height),
                Offset(size.width, size.height),
                stroke((row + 1) % 3 == 0 || row == 8)
            )

            //right border
            drawLine(
                color,
                Offset(size.width, 0f),
                Offset(size.width, size.height),
                stroke((col + 1) % 3 == 0 || col == 8)
            )
        }
}

@Composable
fun SudokuLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.offset(y = (-80).dp),
            text = stringResource(R.string.loading),
            style = SudokuTextStyles.veryBigTitle
        )
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
}

@Composable
fun NumbersBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .offset(y = 200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (value in 1 until 10) {
            Button(
                modifier = Modifier
                    .width(40.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = ContentColor
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {}) {
                Text(
                    text = value.toString(),
                    style = SudokuTextStyles.numberButton
                )
            }
        }
    }
}

@Preview
@Composable
fun GamePreview() {
    GameView(isPortrait = true)
}