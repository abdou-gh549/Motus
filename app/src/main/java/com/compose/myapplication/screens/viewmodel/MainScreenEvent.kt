package com.compose.myapplication.screens.viewmodel

import androidx.compose.runtime.Immutable


@Immutable
sealed class MainScreenEvent {
    data object OnGameResetClick : MainScreenEvent()
    data object OnWordSubmitClick : MainScreenEvent()
    data object OnDeleteClick : MainScreenEvent()
    data class OnKeyBoardClick(val letter: Char) : MainScreenEvent()
}