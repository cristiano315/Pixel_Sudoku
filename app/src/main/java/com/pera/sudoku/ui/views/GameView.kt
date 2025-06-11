package com.pera.sudoku.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pera.sudoku.R
import com.pera.sudoku.ui.theme.CellColor
import com.pera.sudoku.ui.theme.ContainerColor
import com.pera.sudoku.ui.theme.ContentColor
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
                    SudokuGrid(board.value.grids[0].value)
                    //hints
                    //numbers
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
fun SudokuGrid(grid: List<List<Int>>) {
    Column {
        for (row in 0 until 9) {
            Row {
                for (col in 0 until 9) {
                    SudokuCell(row, col, grid[row][col])
                }
            }
        }
    }
}

@Composable
fun SudokuCell(row: Int, col: Int, value: Int) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .sudokuCellBorder(row, col)
    ) {
        Text(value.toString())
    }
}

@Composable
fun GameTimer(timer: Long) {
    Text(
        modifier = Modifier.padding(60.dp),
        text = stringResource(R.string.time, timer),
        color = ContentColor,
        fontSize = 40.sp
    )
}

fun Modifier.sudokuCellBorder(row: Int, col: Int): Modifier {
    return this
        .size(cellSize)
        .drawBehind {
            val stroke =
                { isThick: Boolean -> if (isThick) cellThickBorder.toPx() else cellThinBorder.toPx() }
            val color = CellColor

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
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            color = ContentColor
        )
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
}

@Preview
@Composable
fun GamePreview() {
    GameView(isPortrait = true)
}