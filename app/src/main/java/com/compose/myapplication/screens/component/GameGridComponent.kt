package com.compose.myapplication.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.myapplication.config.MAX_LETTERS
import com.compose.myapplication.config.MAX_RETRY
import com.compose.myapplication.ui.theme.CorrectLetterColor
import com.compose.myapplication.ui.theme.DefaultColor
import com.compose.myapplication.ui.theme.GridCellSize
import com.compose.myapplication.ui.theme.GridPadding
import com.compose.myapplication.ui.theme.LightGray
import com.compose.myapplication.ui.theme.WrongLetterColor
import com.compose.myapplication.ui.theme.WrongPositionLetterColor

/**
 * Component that displays the grid of the game
 * the grid size is defined by the [retries] and [letters] parameters
 *
 * @param modifier Modifier to be applied to the layout
 * @param retries Number of retries
 * @param letters Number of letters
 * @param cellsDataList List of [GridCellData] to be displayed
 */
@Composable
fun GameGridComponent(
    modifier: Modifier = Modifier,
    retries: Int = MAX_RETRY,
    letters: Int = MAX_LETTERS,
    cellsDataList: List<GridCellData> = emptyList()
) {
    LazyVerticalGrid(columns = GridCells.Fixed(letters), modifier = modifier.padding(GridPadding)) {
        items(retries * letters) { index ->
            val cellData = if (cellsDataList.isNotEmpty() && index < cellsDataList.size) {
                cellsDataList[index]
            } else {
                null
            }
            GameCellComponent(cellData = cellData)
        }
    }
}

@Composable
private fun GameCellComponent(modifier: Modifier = Modifier, cellData: GridCellData? = null) {
    val backgroundColor = when (cellData?.status) {
        CellStatus.CORRECT -> CorrectLetterColor
        CellStatus.WRONG_POSITION -> WrongPositionLetterColor
        CellStatus.WRONG -> WrongLetterColor
        else -> DefaultColor
    }
    Box(
        modifier = modifier
            .size(GridCellSize)
            .padding(4.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .border(width = 1.dp, color = LightGray, shape = RoundedCornerShape(4.dp))
    ) {
        val text = cellData?.letter ?: ' '
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text.toString(),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameGridComponentPreview() {
    GameGridComponent(
        cellsDataList = listOf(
            GridCellData('A', CellStatus.CORRECT),
            GridCellData('S', CellStatus.WRONG),
            GridCellData('S', CellStatus.WRONG_POSITION),
            GridCellData('F', CellStatus.WRONG),
            GridCellData('F', CellStatus.WRONG),
            GridCellData('F', CellStatus.CORRECT),
        )
    )
}