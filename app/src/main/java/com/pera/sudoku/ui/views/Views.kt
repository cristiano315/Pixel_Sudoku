package com.pera.sudoku.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pera.sudoku.ui.theme.ContentColor
import com.pera.sudoku.ui.theme.DarkContentColor
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

    //collect press
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed.value = true
                is PressInteraction.Release, is PressInteraction.Cancel -> isPressed.value = false
            }
        }
    }

    //simulate 3d button
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed.value) DarkContentColor else ContentColor,
        label = "buttonColor"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed.value) 2.dp else 8.dp,
        label = "elevation"
    )

    val shape = RoundedCornerShape(12.dp)

    //outer container
    Surface(
        shape = shape,
        shadowElevation = elevation,
        modifier = modifier
            .width(220.dp)
            .height(60.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        val outerBorderSize = 4.dp
        val innerBorderSize = 2.dp
        val totalBorderSize = outerBorderSize + innerBorderSize

        //background darker rounded rectangle for 3d effect
        Surface(
            color = DarkContentColor,
            shape = shape,
            modifier = Modifier
                .drawWithContent { //double border
                    val cornerRadiusPx = 12.dp.toPx()
                    drawContent()
                    //outer border
                    drawRoundRect(
                        color = Color.White,
                        size = size,
                        cornerRadius = CornerRadius(cornerRadiusPx),
                        style = Stroke(outerBorderSize.toPx())
                    )
                    //inner border
                    val inset = innerBorderSize.toPx()
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = Offset(inset, inset),
                        size = Size(size.width - 2 * inset, size.height - 2 * inset),
                        cornerRadius = CornerRadius(cornerRadiusPx),
                        style = Stroke(innerBorderSize.toPx())
                    )
                }
        ) {}
        //another lighter rounded rectangle on top, to simulate higher surface
        BoxWithConstraints { //for proportions
            val width = this.maxWidth - totalBorderSize
            val height =
                if (isPressed.value) (this.maxHeight - totalBorderSize) else (this.maxHeight - totalBorderSize) * 0.9f

            Surface(
                color = buttonColor,
                shape = shape,
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .offset(x = totalBorderSize / 2, totalBorderSize / 2)
            ) {
                //content
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = content
                )
                //shadow with gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun PopUpContent(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        //animate title pop up
        alpha.animateTo(1f, animationSpec = tween(500))
        scale.animateTo(1.5f, animationSpec = tween(700)) //grow more than actual size

        scale.animateTo(
            1.0f,
            animationSpec = tween(300)
        ) //now shrink back to normal. total time = 1 sec
    }

    Box(
        modifier = modifier.graphicsLayer(
            scaleX = scale.value,
            scaleY = scale.value,
            alpha = alpha.value
        ),
        content = content,
        contentAlignment = Alignment.Center
    )
}

@Composable
@Preview
fun ButtonPreview(){
    SudokuButton(onClick = {}) { Text("Test", color = Color.White, style = SudokuTextStyles.genericButton)}
}