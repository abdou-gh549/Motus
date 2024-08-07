package com.compose.myapplication.screens.viewmodel

import com.compose.myapplication.screens.GameState
import com.compose.myapplication.screens.component.GridCellData

data class MainViewUiState(
    val wordsGrid: List<GridCellData> = emptyList(),
    val gameState: GameState = GameState.LOADING,
    val disabledLetters: List<Char> = emptyList(),
    val enableValidation: Boolean = false,
    val showMessage : String = ""
)