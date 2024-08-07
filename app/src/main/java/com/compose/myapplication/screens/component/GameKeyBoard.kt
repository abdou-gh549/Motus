package com.compose.myapplication.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.myapplication.R
import com.compose.myapplication.config.LETTERS_LIST
import com.compose.myapplication.ui.theme.Green200
import com.compose.myapplication.ui.theme.KeyboardKeySize
import com.compose.myapplication.ui.theme.LightGray
import com.compose.myapplication.ui.theme.MyApplicationTheme
import com.compose.myapplication.ui.theme.Red200

/**
 * GameKeyBoard is a composable that represents the keyboard of the game.
 * @param lettersList list of letters to be displayed in the keyboard,
 * default is [LETTERS_LIST] which is a list of 26 letters.
 * we can use this parameter to customize they keyboard keys order
 * @param disabledLetters list of letters that should be disabled in the keyboard.
 * @param onDeleteClick a callback that will be called when the delete button is clicked.
 * @param onDoneClick a callback that will be called when the done button is clicked.
 * @param onKeyClick a callback that will be called when a clickable key is clicked.
 * @param enableValidation a boolean that indicates if the done button should be enabled or not.
 */
@Composable
fun GameKeyBoard(
    modifier: Modifier = Modifier,
    lettersList: List<Char> = LETTERS_LIST,
    disabledLetters: List<Char> = emptyList(),
    onDeleteClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
    onKeyClick: (Char) -> Unit = {},
    enableValidation: Boolean
) {
    if (lettersList.size != 26) {
        throw IllegalArgumentException("lettersList must have  26 letters")
    }

    val firstRow = lettersList.subList(0, 10)
    val secondRow = lettersList.subList(10, 20)
    val thirdRow = lettersList.subList(20, 26)

    Column(modifier = modifier) {
        KeyRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            letters = firstRow,
            disabledLetters = disabledLetters,
            onKeyClick = onKeyClick
        )
        KeyRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            letters = secondRow,
            disabledLetters = disabledLetters,
            onKeyClick = onKeyClick
        )

        Row(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()) {
            IconButton(
                onClick = { onDeleteClick() },
                Modifier
                    .size(KeyboardKeySize)
                    .weight(1f)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_button),
                    tint = Red200
                )
            }
            KeyRow(
                modifier = Modifier.weight(4f),
                letters = thirdRow,
                disabledLetters = disabledLetters,
                onKeyClick = onKeyClick
            )

            IconButton(
                onClick = { onDoneClick() },
                Modifier
                    .size(KeyboardKeySize)
                    .testTag("done_button")
                    .weight(1f),
                enabled =  enableValidation
                ) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = stringResource(id = R.string.done_button),
                    tint = if(enableValidation) Green200 else LightGray
                )
            }
        }
    }
}


@Composable
private fun KeyRow(
    modifier: Modifier = Modifier,
    letters: List<Char>,
    disabledLetters: List<Char>,
    onKeyClick: (Char) -> Unit = {}
) {
    Row(modifier = modifier) {
        letters.forEach { letter ->
            GameKey(
                modifier.weight(1f),
                letter = letter,
                disabled = disabledLetters.contains(letter),
                onKeyClick = onKeyClick
            )
        }
    }
}


@Composable
private fun GameKey(
    modifier: Modifier = Modifier,
    letter: Char,
    disabled: Boolean,
    onKeyClick: (Char) -> Unit
) {
    val backgroundColor = if (disabled) {
        Color.LightGray
    } else {
        Color.White
    }
    Box(
        modifier = modifier
            .height(height = KeyboardKeySize)
            .padding(1.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .border(width = 1.dp, color = LightGray, shape = RoundedCornerShape(4.dp))
            .clickable(enabled = !disabled) {
                onKeyClick(letter)
            }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = letter.toString(),
        )
    }
}

@Composable
@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:parent=Nexus 5"
)
fun GameKeyBoardPreview() {
    MyApplicationTheme {
        GameKeyBoard(
            disabledLetters = listOf('A', 'B', 'C'),
            enableValidation = true
        )
    }
}