package com.pera.sudoku.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pera.sudoku.R
import com.pera.sudoku.model.Difficulties
import com.pera.sudoku.model.FilterType
import com.pera.sudoku.model.Results
import com.pera.sudoku.model.SavedGame
import com.pera.sudoku.ui.theme.ContainerColor
import com.pera.sudoku.ui.theme.ContentColor
import com.pera.sudoku.ui.theme.SudokuTextStyles
import com.pera.sudoku.viewmodels.HistoryViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun HistoryView(
    modifier: Modifier = Modifier,
    isPortrait: Boolean,
    viewModel: HistoryViewModel = hiltViewModel(),
    navController: NavController
){
    val games = viewModel.games.collectAsState()

    if(isPortrait){
        Column(modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp)
            .background(ContainerColor)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                val selectedOption = remember { mutableStateOf("All") }
                val selectedOptionFilter = remember { mutableStateOf(FilterType.DIFFICULTY) }
                val context = LocalContext.current
                SudokuDropDownMenu(
                    modifier = Modifier.width(120.dp),
                    options = listOf(
                        stringResource(R.string.all),
                        stringResource(R.string.difficulty_history), stringResource(R.string.result)
                    )
                ){selection ->
                    selectedOption.value = selection
                    if(selection == context.getString(R.string.difficulty_history)) selectedOptionFilter.value = FilterType.DIFFICULTY
                    else selectedOptionFilter.value = FilterType.RESULT
                }
                SudokuDropDownMenu(
                    modifier = Modifier.width(120.dp),
                    options = when(selectedOption.value){
                        stringResource(R.string.all) -> listOf("")
                        stringResource(R.string.difficulty_history) -> listOf(stringResource(R.string.hard), stringResource(R.string.medium), stringResource(R.string.easy))
                        stringResource(R.string.result) -> listOf(stringResource(R.string.wins), stringResource(R.string.losses))
                        else -> listOf("")
                    }
                ){filter ->
                    viewModel.loadGames(filter, selectedOptionFilter.value, context)
                }
                SudokuDropDownMenu(
                    modifier = Modifier.width(120.dp),
                    options = listOf(
                        stringResource(R.string.time_lowest), stringResource(R.string.time_highest)
                    ),
                    textStyle = SudokuTextStyles.smallGenericButton
                ){orderMethod ->
                    viewModel.sortGames(orderMethod, context)
                }
                Spacer(modifier = Modifier.weight(1f))
                SudokuButton(modifier = Modifier
                    .width(60.dp)
                    .height(60.dp), onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
            LazyColumn {
                if(games.value.isEmpty()){
                    item {
                        Text(
                            text = stringResource(R.string.no_games_yet),
                            style = SudokuTextStyles.bigTitle,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else {
                    items(games.value) { game ->
                        GameItem(game){
                            viewModel.deleteGame(game)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDialog(onDismissRequest: () -> Unit, deleteGame: () -> Unit){
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(
            text = stringResource(R.string.confirm),
            style = SudokuTextStyles.genericButton,
            color = Color.Black) },
        text = { Text(
            text = stringResource(R.string.delete_the_game),
            style = SudokuTextStyles.genericButton,
            color = Color.Black,
            fontSize = 25.sp) },
        confirmButton = {
            SudokuButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDismissRequest()
                    deleteGame()
                }
            ) {
                Text(text = stringResource(R.string.delete),
                    style = SudokuTextStyles.genericButton)
            }
        },
        dismissButton = {
            SudokuButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel),
                    style = SudokuTextStyles.genericButton)
            }
        }
    )
}

@Composable
fun GameItem(
    game: SavedGame,
    onDeleteRequest: (game: SavedGame) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            contentColor = ContentColor
        )
    ) {
        Column(modifier = Modifier
            .padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.date) + game.date.toItemString(),
                    style = SudokuTextStyles.gameItem
                )
                Spacer(modifier = Modifier.weight(1f))
                SudokuButton(modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                    onClick = {showDialog.value = true}) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = SudokuTextStyles.genericButton
                    )
                }
            }
            Text(
                text = stringResource(R.string.difficulty) + getDifficultyString(game.difficulty),
                style = SudokuTextStyles.gameItem
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.time_history) + game.time.toString(),
                    style = SudokuTextStyles.gameItem
                )
                Spacer(modifier = Modifier.weight(1f))
                //result
                Text(
                    text = getResultString(game.result),
                    style = SudokuTextStyles.gameItem,
                    color = if(game.result == Results.Win) Color.Green else Color.Red,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if(showDialog.value){
        ConfirmDialog(onDismissRequest = {
            showDialog.value = false
        },
            deleteGame = { onDeleteRequest(game) }
        )
    }
}

fun Date.toItemString(): String{
    val calendar = Calendar.getInstance()
    calendar.time = this

    return calendar.get(Calendar.DAY_OF_WEEK).toString() + "/" + calendar.get(Calendar.MONTH).toString() + "/" + calendar.get(Calendar.YEAR).toString() + " " + calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString()
}

@Composable
fun getResultString(result: Results): String{
    return if(result == Results.Win) stringResource(R.string.win) else stringResource(R.string.lose)
}

@Composable
fun getDifficultyString(difficulty: Difficulties): String{
    return when(difficulty){
        Difficulties.Easy -> stringResource(R.string.easy)
        Difficulties.Medium -> stringResource(R.string.medium)
        Difficulties.Hard -> stringResource(R.string.hard)
    }
}


@Composable
@Preview
fun GameItemPreview(){
    val game = SavedGame(0, Difficulties.Medium, 0L, Results.Lose, Date(1750279581524))

    GameItem(game){}
}
