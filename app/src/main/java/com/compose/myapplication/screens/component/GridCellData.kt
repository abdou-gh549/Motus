package com.compose.myapplication.screens.component

data class GridCellData (
    val letter: Char,
    val status: CellStatus
)

enum class CellStatus {
    CORRECT,
    WRONG_POSITION,
    WRONG,
    NULL
}