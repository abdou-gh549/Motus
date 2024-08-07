package com.compose.myapplication

import com.compose.myapplication.domain.interfaces.MotusRepository
import com.compose.myapplication.screens.GameState
import com.compose.myapplication.screens.component.CellStatus
import com.compose.myapplication.screens.viewmodel.MainScreenEvent
import com.compose.myapplication.screens.viewmodel.MainScreenViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

/**
 * this is an example of how to test the view model
 * i didn't test all the case but you can add more test cases easily,
 * since the repository is mocked we can inject any data we want to test the view model
 *
 * tests i didn't implement because of the time:
 *
 * - repository testing
 * - testing all case of view model
 * - testing all case of our ui
 */
class MainViewModelTests {
    private lateinit var viewModel: MainScreenViewModel

    private val mockedRepository = mockk<MotusRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `test if the view model is initialized correctly`() = runTest {

        val myFlow = MutableSharedFlow<List<String>>()

        coEvery { mockedRepository.getMotusData() } returns myFlow

        viewModel = MainScreenViewModel(mockedRepository)

        assertEquals(viewModel.uiState.value.gameState, GameState.LOADING)
        assertEquals(viewModel.uiState.value.enableValidation, false)
        assertTrue(viewModel.uiState.value.disabledLetters.isEmpty())
        assertTrue(viewModel.uiState.value.wordsGrid.isEmpty())

        myFlow.emit(listOf("abcdef"))

        assertEquals(viewModel.uiState.value.gameState, GameState.PLAYING)
        assertEquals(viewModel.uiState.value.enableValidation, false)
        assertTrue(viewModel.uiState.value.disabledLetters.isEmpty())
        assertTrue(viewModel.uiState.value.wordsGrid.isNotEmpty())
        // check if the first letter is set correctly
        assertEquals(viewModel.uiState.value.wordsGrid[0].letter, 'a')
        assertEquals(viewModel.uiState.value.wordsGrid[0].status, CellStatus.CORRECT)
    }


    @Test
    fun `test if the view model is initialized correctly with empty list`() = runTest {

        val myFlow = MutableSharedFlow<List<String>>()

        coEvery { mockedRepository.getMotusData() } returns myFlow

        viewModel = MainScreenViewModel(mockedRepository)

        assertEquals(viewModel.uiState.value.gameState, GameState.LOADING)
        assertEquals(viewModel.uiState.value.enableValidation, false)
        assertTrue(viewModel.uiState.value.disabledLetters.isEmpty())
        assertTrue(viewModel.uiState.value.wordsGrid.isEmpty())

        myFlow.emit(emptyList())

        assertEquals(viewModel.uiState.value.gameState, GameState.LOADING)
        assertEquals(viewModel.uiState.value.enableValidation, false)
        assertTrue(viewModel.uiState.value.disabledLetters.isEmpty())
        assertTrue(viewModel.uiState.value.wordsGrid.isEmpty())
    }

    @Test
    fun `test if the view model events works correctly `() = runTest {

        val myFlow = MutableSharedFlow<List<String>>()

        coEvery { mockedRepository.getMotusData() } returns myFlow

        viewModel = MainScreenViewModel(mockedRepository)

        assertEquals(viewModel.uiState.value.gameState, GameState.LOADING)
        assertEquals(viewModel.uiState.value.enableValidation, false)
        assertTrue(viewModel.uiState.value.disabledLetters.isEmpty())
        assertTrue(viewModel.uiState.value.wordsGrid.isEmpty())
        myFlow.emit(listOf("abcdef"))

        viewModel.onEvent(MainScreenEvent.OnDeleteClick)
        // player can't delete the first letter
        assertEquals(viewModel.uiState.value.wordsGrid[0].letter, 'a')
        assertEquals(viewModel.uiState.value.wordsGrid[0].status, CellStatus.CORRECT)

        viewModel.onEvent(MainScreenEvent.OnKeyBoardClick('b'))
        assertEquals(viewModel.uiState.value.wordsGrid[0].letter, 'a')
        assertEquals(viewModel.uiState.value.wordsGrid[0].status, CellStatus.CORRECT)
        assertEquals(viewModel.uiState.value.wordsGrid[1].letter, 'b')
        assertEquals(viewModel.uiState.value.wordsGrid[1].status,  CellStatus.NULL)

        viewModel.onEvent(MainScreenEvent.OnKeyBoardClick('c'))
        assertEquals(viewModel.uiState.value.wordsGrid[0].letter, 'a')
        assertEquals(viewModel.uiState.value.wordsGrid[0].status, CellStatus.CORRECT)
        assertEquals(viewModel.uiState.value.wordsGrid[1].letter, 'b')
        assertEquals(viewModel.uiState.value.wordsGrid[1].status,  CellStatus.NULL)
        assertEquals(viewModel.uiState.value.wordsGrid[2].letter, 'c')
        assertEquals(viewModel.uiState.value.wordsGrid[2].status,  CellStatus.NULL)

        viewModel.onEvent(MainScreenEvent.OnKeyBoardClick('e'))

        assertEquals(viewModel.uiState.value.wordsGrid[0].letter, 'a')
        assertEquals(viewModel.uiState.value.wordsGrid[0].status, CellStatus.CORRECT)
        assertEquals(viewModel.uiState.value.wordsGrid[1].letter, 'b')
        assertEquals(viewModel.uiState.value.wordsGrid[1].status,  CellStatus.NULL)
        assertEquals(viewModel.uiState.value.wordsGrid[2].letter, 'c')
        assertEquals(viewModel.uiState.value.wordsGrid[2].status,  CellStatus.NULL)
        assertEquals(viewModel.uiState.value.wordsGrid[3].letter, 'e')
        assertEquals(viewModel.uiState.value.wordsGrid[3].status,  CellStatus.NULL)

        viewModel.onEvent(MainScreenEvent.OnDeleteClick)

        assertEquals(viewModel.uiState.value.wordsGrid[0].letter, 'a')
        assertEquals(viewModel.uiState.value.wordsGrid[0].status, CellStatus.CORRECT)
        assertEquals(viewModel.uiState.value.wordsGrid[1].letter, 'b')
        assertEquals(viewModel.uiState.value.wordsGrid[1].status,  CellStatus.NULL)
        assertEquals(viewModel.uiState.value.wordsGrid[2].letter, 'c')
        assertEquals(viewModel.uiState.value.wordsGrid[2].status,  CellStatus.NULL)
        assertEquals(viewModel.uiState.value.wordsGrid[3].letter, ' ')
        assertEquals(viewModel.uiState.value.wordsGrid[3].status,  CellStatus.NULL)
    }
}