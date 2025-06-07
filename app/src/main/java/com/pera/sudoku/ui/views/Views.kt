package com.pera.sudoku.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pera.sudoku.ui.theme.ContainerColor
import com.pera.sudoku.ui.theme.ContentColor

@Composable
fun GameView(modifier: Modifier = Modifier, isPortrait: Boolean){
    //vars
    if(isPortrait){
        Column(
            modifier = modifier.fillMaxSize().background(ContainerColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ciao")
        }
    }
}

@Preview
@Composable
fun GamePreview(){
    GameView(isPortrait =  true)
}