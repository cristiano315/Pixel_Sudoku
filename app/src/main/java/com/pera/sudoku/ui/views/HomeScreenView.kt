package com.pera.sudoku.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pera.sudoku.R
import com.pera.sudoku.ui.theme.ContainerColor
import com.pera.sudoku.ui.theme.SudokuTextStyles

@Composable
fun HomeScreenView(
    modifier: Modifier = Modifier,
    isPortrait: Boolean,
    navController: NavController
) {
    if (isPortrait) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ContainerColor)
                .padding(vertical = 30.dp)
                .offset(y = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTitle()
            Spacer(modifier = Modifier.height(350.dp))
            GenericHomeButton( //start game
                modifier = Modifier.homeButton(),
                text = stringResource(R.string.start_game)
            ) {
                navController.navigate("gameScreen")
            }
            Spacer(modifier = Modifier.height(10.dp))
            GenericHomeButton( //start game
                modifier = Modifier.homeButton(),
                text = stringResource(R.string.history)
            ) {
                navController.navigate("historyScreen")
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ContainerColor)
                .padding(vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTitle()
            Row(modifier = Modifier.fillMaxWidth()) {
            }
        }
    }
}

@Composable
fun GameTitle(modifier: Modifier = Modifier) {
    PopUpContent(modifier) {
        Text(
            text = "PIXEL\nSUDOKU", //no need for translation
            modifier = modifier,
            textAlign = TextAlign.Center,
            style = SudokuTextStyles.titleScreen
        )
    }
}

@Composable
fun GenericHomeButton(modifier: Modifier = Modifier, text: String, startGame: () -> Unit) {
    PopUpContent(modifier) {
        SudokuButton(modifier = modifier, onClick = startGame) {
            Text(
                text = text,
                style = SudokuTextStyles.homeButton
            )
        }
    }
}

@Composable
fun Modifier.homeButton(): Modifier{
    val buttonWidth = 400.dp
    val buttonHeight = 100.dp
    return this
        .width(buttonWidth)
        .height(buttonHeight)
}

