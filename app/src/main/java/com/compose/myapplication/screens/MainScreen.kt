package com.compose.myapplication.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compose.myapplication.screens.component.GameGridComponent
import com.compose.myapplication.screens.component.GameKeyBoard
import com.compose.myapplication.screens.viewmodel.MainScreenEvent
import com.compose.myapplication.screens.viewmodel.MainScreenViewModel
import com.compose.myapplication.screens.viewmodel.MainViewUiState
import com.compose.myapplication.ui.theme.MyApplicationTheme

@Composable
fun MainScreen(mainScreenViewModel: MainScreenViewModel) {
    val uiState = mainScreenViewModel.uiState.collectAsStateWithLifecycle()
    MyApplicationTheme {
        MainScreenContent(uiState.value, mainScreenViewModel::onEvent)
    }
}

@Composable
fun MainScreenContent(uiState: MainViewUiState, onEvent: (MainScreenEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GameGridComponent(cellsDataList = uiState.wordsGrid)
        StartNewGameComposable(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            onResetGameClick = { onEvent(MainScreenEvent.OnGameResetClick) })


        GameStateContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            showMessage = uiState.showMessage,
            gameState = uiState.gameState
        )

        Spacer(modifier = Modifier.weight(1f))
        GameKeyBoard(
            modifier = Modifier.padding(bottom = 16.dp),
            disabledLetters = uiState.disabledLetters,
            enableValidation = uiState.enableValidation,
            onDeleteClick = { onEvent(MainScreenEvent.OnDeleteClick) },
            onKeyClick = { letter -> onEvent(MainScreenEvent.OnKeyBoardClick(letter)) },
            onDoneClick = { onEvent(MainScreenEvent.OnWordSubmitClick) },
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun StartNewGameComposable(modifier: Modifier = Modifier, onResetGameClick: () -> Unit = {}) {
    Box(modifier = modifier) {
        Button(onClick = { onResetGameClick() }) {
            Text(
                text = "Start New Game",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }

    }
}

@Composable
fun GameStateContent(modifier: Modifier, gameState: GameState, showMessage: String) {

    val text = when (gameState) {
        GameState.GAME_OVER -> "Game Over"
        GameState.GAME_WON -> "You Won"
        else -> ""
    }
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = text
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            text = showMessage
        )
    }
}

@Composable
@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:parent=Nexus 5"
)
fun MainScreenPreview() {
    MyApplicationTheme {
        MainScreenContent(MainViewUiState()) {}
    }
}

enum class GameState {
    LOADING,
    PLAYING,
    GAME_OVER,
    GAME_WON
}