package com.pera.sudoku.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pera.sudoku.ui.theme.ContentColor
import com.pera.sudoku.ui.theme.SudokuTextStyles
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SudokuButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed = remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed.value = true
                is PressInteraction.Release, is PressInteraction.Cancel -> isPressed.value = false
            }
        }
    }
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed.value) Color(0xFF005CCC) else ContentColor,
        label = "buttonColor"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed.value) 2.dp else 8.dp,
        label = "elevation"
    )

    val yOffset by animateDpAsState(
        targetValue = if (isPressed.value) 2.dp else 0.dp,
        label = "yOffset"
    )

    val shape = RoundedCornerShape(12.dp)

    Surface(
        color = buttonColor,
        shape = shape,
        tonalElevation = elevation,
        shadowElevation = elevation,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .offset(y = yOffset)
            .width(220.dp)
            .height(60.dp)
            .drawWithContent {
                val cornerRadiusPx = 12.dp.toPx()
                drawContent()
                //outer border
                drawRoundRect(
                    color = Color.White,
                    size = size,
                    cornerRadius = CornerRadius(cornerRadiusPx),
                    style = Stroke(4.dp.toPx())
                )
                //inner border
                val inset = 2.dp.toPx()
                drawRoundRect(
                    color = Color.Black,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - 2 * inset, size.height - 2 * inset),
                    cornerRadius = CornerRadius(cornerRadiusPx),
                    style = Stroke(2.dp.toPx())
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(13f),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

@Composable
@Preview
fun ButtonPreview(){
    SudokuButton(onClick = {}) { Text("ciao", color = Color.White, style = SudokuTextStyles.genericButton)}
}