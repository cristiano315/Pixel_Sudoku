package com.pera.sudoku.ui.views

import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pera.sudoku.R
import com.pera.sudoku.model.GameState
import com.pera.sudoku.model.toTimeString
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
import kotlinx.coroutines.launch


@Composable
fun GameView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
    isPortrait: Boolean,
    navController: NavController
) {
    val board = viewModel.board.collectAsState()
    val timer = viewModel.timer.collectAsState()
    val focusedCell = viewModel.focusedCell.collectAsState()
    val errorCount = viewModel.errorCount.collectAsState()
    val errorTrigger = viewModel.errorTrigger.collectAsState()
    val context = LocalContext.current
    val gameState = viewModel.gameState.collectAsState()
    val isPaused = viewModel.isPaused.collectAsState()
    val annotationsList = viewModel.cellsAnnotations.collectAsState()

    when(gameState.value) {
        GameState.LOADING -> { //wait for data
            SudokuLoading(isPortrait)
        }

        GameState.LOST -> { //losescreen
            GameEndScreen(
                isPortrait = isPortrait,
                onNewGame = {
                    viewModel.saveGame()
                    navController.navigate("gameScreen") {
                        popUpTo("gameScreen") { inclusive = true }
                    }
                },
                onQuitGame = {
                    viewModel.saveGame()
                    (context as? Activity)?.finish()
                },
                isWon = false,
                onHomeButton = {
                    viewModel.saveGame()
                    navController.navigate("homeScreen")
                }
            )
        }

        GameState.WON -> {
            GameEndScreen(isPortrait = isPortrait,
                onNewGame = {
                    viewModel.saveGame()
                    navController.navigate("gameScreen") {
                        popUpTo("gameScreen") { inclusive = true }
                    }
                },
                onQuitGame = {
                    viewModel.saveGame()
                    (context as? Activity)?.finish()
                },
                isWon = true,
                onHomeButton = {
                    viewModel.saveGame()
                    navController.navigate("homeScreen")
                })
        }

        GameState.PLAYING -> {
            if (isPortrait) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(ContainerColor)
                        .padding(vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameTimer(timer.value)
                    //Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.difficulty) + getTranslatedDifficulty(viewModel.newBoard.grids[0].difficulty),
                        style = SudokuTextStyles.bigTitle
                    )
                    SudokuGrid(
                        grid = board.value,
                        focusedCell = focusedCell.value,
                        annotationsList = annotationsList.value
                    ) { row: Int, col: Int ->
                        viewModel.focusCell(row, col)
                    }
                    ErrorBar(errorCount = errorCount.value)
                    //annotations button
                    SudokuToggleButton(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .align(Alignment.End),
                        onToggle =  {state ->
                            viewModel.updateAnnotationState(state)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null
                        )
                    }
                    //Spacer(modifier = Modifier.height(70.dp))
                    NumbersButtonBar { input: Int ->
                        viewModel.checkInputNumber(input)
                    }
                    //pause button
                    SudokuButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        onClick = {viewModel.pause()}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Pause,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Row {
                    GameTimer(timer.value)
                }
            }

            if(isPaused.value == true){
                AlertDialog(
                    modifier = Modifier
                        .width((cellSize * 9) + 30.dp)
                        .height((cellSize * 9) + 120.dp),
                    onDismissRequest = {
                        viewModel.saveGame()
                        navController.navigate("homeScreen")
                    },
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.paused),
                            style = SudokuTextStyles.veryBigTitle,
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.weight(1f))
                            SudokuButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                onClick = {viewModel.resume()}
                            ) {
                                Text(text = stringResource(R.string.resume),
                                    style = SudokuTextStyles.genericButton)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            SudokuButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                onClick = {
                                    viewModel.saveGame()
                                    navController.navigate("homeScreen")
                                }) {
                                Text(text = stringResource(R.string.quit),
                                    style = SudokuTextStyles.genericButton)
                            }

                        }
                    },
                    confirmButton = {},
                    dismissButton = {}
                )
            }

            ErrorEffect(errorTrigger.value) { viewModel.resetTrigger() }
        }
    }
}

@Composable
fun GameEndScreen(
    isPortrait: Boolean,
    isWon: Boolean,
    onNewGame: () -> Unit,
    onQuitGame: () -> Unit,
    onHomeButton: () -> Unit) {
    if(isPortrait){
        val buttonWidth = 150.dp
        val buttonHeight = 80.dp

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if(isWon) stringResource(R.string.you_won) else stringResource(R.string.you_lost),
                    style = SudokuTextStyles.veryBigTitle
                )
                Row{
                    SudokuButton(
                        modifier = Modifier
                            .width(buttonWidth)
                            .height(buttonHeight),
                        onClick = onNewGame
                    ) {
                        Text(
                            text = stringResource(R.string.new_game),
                            style = SudokuTextStyles.genericButton,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    SudokuButton(
                        modifier = Modifier
                            .width(buttonWidth)
                            .height(buttonHeight),
                        onClick = onQuitGame
                    ) {
                        Text(
                            text = stringResource(R.string.save_and_quit),
                            style = SudokuTextStyles.genericButton,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                SudokuButton(modifier = Modifier
                    .width((buttonWidth * 2) + 20.dp)
                    .height(buttonHeight),
                    onClick = onHomeButton) {
                    Text(
                        text = stringResource(R.string.home_button),
                        style = SudokuTextStyles.genericButton,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    else {
        //landscape layout
    }
}

@Preview(showBackground = true)
@Composable
fun GameEndScreenPreview(){
    GameEndScreen(true, true, {}, {}, {})
}

@Composable
fun SudokuGrid(
    grid: List<List<Int>>,
    annotationsList: List<List<List<Boolean>>>,
    focusedCell: Pair<Int, Int>,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column {
        for (row in 0 until 9) {
            Row {
                for (col in 0 until 9) {
                    SudokuCell(
                        row = row,
                        col = col,
                        value = grid[row][col],
                        focusedCell = focusedCell,
                        focusedValue = grid[focusedCell.first][focusedCell.second],
                        cellAnnotations = annotationsList[row][col]
                    ) {
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
    cellAnnotations: List<Boolean>,
    focusedCell: Pair<Int, Int>,
    focusedValue: Int,
    onCellClick: () -> Unit,
) {
    val backGroundColor = { row: Int, col: Int ->
        if (Pair(
                row,
                col
            ) == focusedCell || (value == focusedValue && value != 0)
        ) CellBackGroundFocusedColor
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
        //draw annotations
        else{
            Column(modifier = Modifier.fillMaxSize()
                .offset(y = (-4).dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                for(i in 0 until 3){
                    Row(modifier = Modifier.wrapContentHeight()) {
                        for(j in 0 until 3){
                            val index = (3 * i) + j
                            Box(modifier = Modifier.size((cellSize / 3)),
                                contentAlignment = Alignment.Center){
                                if(cellAnnotations[index] == true){
                                    Text(
                                        text = (index + 1).toString(),
                                        fontSize = 8.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }
                }
            }
        }
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
        text = stringResource(R.string.time) + timer.toTimeString(),
        style = SudokuTextStyles.bigTitle
    )
}

fun Modifier.sudokuCellBorder(row: Int, col: Int): Modifier {
    return this
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
fun SudokuLoading(isPortrait: Boolean) {
    if(isPortrait){
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
    else {
        //landscape layout
    }
}

@Composable
fun NumbersButtonBar(modifier: Modifier = Modifier, onNumberClick: (input: Int) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
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
                onClick = { onNumberClick(value) }) {
                Text(
                    text = value.toString(),
                    style = SudokuTextStyles.numberButton
                )
            }
        }
    }
}

@Composable
fun ErrorBar(modifier: Modifier = Modifier, errorCount: Int) {
    Row {
        Text(
            text = stringResource(R.string.errors),
            style = SudokuTextStyles.bigTitle
        )
        repeat(errorCount) {
            Box(
                modifier = modifier.padding(top = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = modifier.size(36.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Error",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun ErrorEffect(
    trigger: Boolean,
    onEffectEnd: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val alpha = remember { Animatable(0f) }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(trigger) {
        if (trigger) {
            //vibration
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

            coroutineScope.launch {
                //red flash
                alpha.snapTo(0.6f)
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 300)
                )
                onEffectEnd()
            }
        }
    }

    if (alpha.value > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red.copy(alpha = alpha.value))
                .zIndex(10f)
        )
    }
}

@Composable
fun getTranslatedDifficulty(difficulty: String): String {
    return when(difficulty){
        "Hard" -> stringResource(R.string.hard)
        "Medium" -> stringResource(R.string.medium)
        "Easy" -> stringResource(R.string.easy)
        else -> ""
    }
}

@Preview
@Composable
fun GamePreview() {
}