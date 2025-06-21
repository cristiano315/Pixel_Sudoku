package com.pera.sudoku.ui.views

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pera.sudoku.R
import com.pera.sudoku.ui.navigation.GAME_SCREEN_ROUTE
import com.pera.sudoku.ui.navigation.HISTORY_SCREEN_ROUTE
import com.pera.sudoku.ui.navigation.getGameRouteWithDifficulty
import com.pera.sudoku.ui.theme.ContainerColor
import com.pera.sudoku.ui.theme.SudokuTextStyles

@Composable
fun HomeScreenView(
    modifier: Modifier = Modifier,
    isPortrait: Boolean,
    navController: NavController
) {
    var difficulty by remember { mutableStateOf("Hard") }
    val context = LocalContext.current

    BackHandler { (context as? Activity)?.finish() }
    //portrait layout
    if (isPortrait) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ContainerColor)
                .padding(vertical = 30.dp)
                .offset(y = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTitle(isPortrait = true)
            Spacer(modifier = Modifier.height(250.dp))
            GenericHomeButton( //start game
                modifier = Modifier.homeButton(),
                text = stringResource(R.string.start_game)
            ) {
                if (difficulty.isEmpty()) navController.navigate(GAME_SCREEN_ROUTE)
                else navController.navigate(getGameRouteWithDifficulty(difficulty))
            }
            Spacer(modifier = Modifier.height(10.dp))
            //difficulty selector
            HomeScreenSelector(
                modifier = Modifier.homeButton(),
                options = listOf(
                    stringResource(R.string.hard),
                    stringResource(R.string.medium),
                    stringResource(R.string.easy),
                    stringResource(R.string.random)
                ),
            ) { selection ->
                difficulty = getCorrectDifficultyFilter(selection, context) //for translations
            }
            Spacer(modifier = Modifier.height(10.dp))
            GenericHomeButton( //go to history
                modifier = Modifier.homeButton(),
                text = stringResource(R.string.history)
            ) {
                navController.navigate(HISTORY_SCREEN_ROUTE)
            }
        }
    }
    //landscape layout
    else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(ContainerColor)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(100.dp))
            GameTitle(isPortrait = false)
            Spacer(modifier = Modifier.width(110.dp))
            Column {
                GenericHomeButton( //start game
                    modifier = Modifier.homeButton(),
                    text = stringResource(R.string.start_game)
                ) {
                    if (difficulty.isEmpty()) navController.navigate(GAME_SCREEN_ROUTE)
                    else navController.navigate(getGameRouteWithDifficulty(difficulty))
                }
                Spacer(modifier = Modifier.height(10.dp))
                HomeScreenSelector(
                    modifier = Modifier.homeButton(),
                    options = listOf(
                        stringResource(R.string.hard),
                        stringResource(R.string.medium),
                        stringResource(R.string.easy),
                        stringResource(R.string.random)
                    ),
                ) { selection ->
                    difficulty = getCorrectDifficultyFilter(selection, context)
                }
                Spacer(modifier = Modifier.height(10.dp))
                GenericHomeButton(
                    modifier = Modifier.homeButton(),
                    text = stringResource(R.string.history)
                ) {
                    navController.navigate(HISTORY_SCREEN_ROUTE)
                }
            }
        }
    }
}

@Composable
fun GameTitle(modifier: Modifier = Modifier, isPortrait: Boolean) {
    PopUpContent(modifier) { //everything is wrapped in this to have a homogeneous animation, in views file
        Text(
            text = "PIXEL\nSUDOKU", //no need for translation
            modifier = modifier,
            textAlign = TextAlign.Center,
            style = if (isPortrait) SudokuTextStyles.titleScreen else SudokuTextStyles.landscapeTitleScreen
        )
    }
}

@Composable
fun GenericHomeButton(modifier: Modifier = Modifier, text: String, startGame: () -> Unit) {
    PopUpContent(modifier) {
        SudokuButton(modifier = modifier, onClick = startGame) { //custom button, in views file
            Text(
                text = text,
                style = SudokuTextStyles.homeButton
            )
        }
    }
}

@Composable
fun HomeScreenSelector(
    modifier: Modifier = Modifier,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    PopUpContent(modifier) {
        SudokuDropDownMenu(
            modifier = Modifier.homeButton(),
            options = options,
            textStyle = SudokuTextStyles.homeButton,
            onOptionSelected = onOptionSelected,
        )
    }
}

@Composable
fun Modifier.homeButton(): Modifier { // custom modifier, all buttons have same dimensions
    val buttonWidth = 400.dp
    val buttonHeight = 100.dp
    return this
        .width(buttonWidth)
        .height(buttonHeight)
}

fun getCorrectDifficultyFilter(value: String, context: Context? = null): String {
    return when (value) {
        context?.getString(R.string.hard) -> "Hard"
        context?.getString(R.string.medium) -> "Medium"
        context?.getString(R.string.easy) -> "Easy"
        else -> ""
    }
}

