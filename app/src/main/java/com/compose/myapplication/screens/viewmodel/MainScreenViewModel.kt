package com.compose.myapplication.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.myapplication.config.MAX_LETTERS
import com.compose.myapplication.config.MAX_RETRY
import com.compose.myapplication.domain.interfaces.MotusRepository
import com.compose.myapplication.screens.GameState
import com.compose.myapplication.screens.component.CellStatus
import com.compose.myapplication.screens.component.GridCellData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private var motusRepository: MotusRepository
) : ViewModel() {

    private val TAG: String = MainScreenViewModel::class.java.name

    private var _uiState = MutableStateFlow(MainViewUiState())

    var uiState = _uiState.asStateFlow()
        private set

    private var wordsList = listOf<String>()
    private var selectedWord = ""

    // currentRow and currentRowIndex are used to track the current position of the player in the grid
    private var currentRow = 0
    private var currentRowIndex = 0

    init {
        initData()
    }

    /**
     * Fetches the words list from the repository,
     * then selects a random word from the list to start the game
     */
    private fun initData() {
        viewModelScope.launch {
            motusRepository.getMotusData().catch {
                it.printStackTrace()
            }.collect {
                wordsList = it
                withContext(Dispatchers.Main) {
                    startNewGame()
                }
            }
        }
    }

    /**
     * This function is responsible for starting a new game by selecting a random word
     * from the word list, and then initializing the grid state.
     */
    private fun startNewGame() {
        if (wordsList.isEmpty()) {
            return
        }
        selectedWord = wordsList.random()

      // Log.d(TAG, "startNewGame: $selectedWord")

        if (selectedWord.isEmpty()) {
            return
        }

        val grid = mutableListOf<GridCellData>()

        // init first row
        grid.add(GridCellData(selectedWord[0], CellStatus.CORRECT))

        for (i in 1 until MAX_LETTERS * MAX_RETRY) {
            grid.add(GridCellData(' ', CellStatus.NULL))
        }

        currentRow = 0
        currentRowIndex = 0

        _uiState.value = MainViewUiState(
            gameState = GameState.PLAYING,
            wordsGrid = grid
        )
    }

    /**
     * onEvent is a function that is called when an event is triggered in the UI.
     * @param event the event that is triggered in the UI
     */
    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.OnGameResetClick -> {
                if (wordsList.isEmpty()) {
                    return
                }
                startNewGame()
            }

            is MainScreenEvent.OnDeleteClick -> onDeleteClick()

            is MainScreenEvent.OnKeyBoardClick -> onKeyBoardClick(event.letter)

            is MainScreenEvent.OnWordSubmitClick -> onWordValidationClick()
        }
    }

    /**
     * this function is called when the player clicks on the done button to validate the word
     * that he entered in the grid.
     * if the word is correct then the player wins the game
     * if the word is incorrect and doesn't exist in [wordsList] then the player loses the game
     * if the word is incorrect and exists in [wordsList] then the player can try again until he reaches the maximum number of retries
     */
    private fun onWordValidationClick() {
        if (_uiState.value.gameState != GameState.PLAYING) {
            return
        }

        if (currentRowIndex < 0 || currentRowIndex < MAX_LETTERS - 1) {
            /*    Log.d(
                    TAG,
                    "onWordValidationClick: currentRowIndex < 0 || currentRowIndex < MAX_LETTERS - 1"
                )

             */
            return
        }
        val grid = _uiState.value.wordsGrid.toMutableList()
        val startIndex = (currentRow * MAX_LETTERS)
        val endIndex = startIndex + currentRowIndex
        val playerWord =
            grid.subList(startIndex, endIndex + 1).joinToString("") { it.letter.toString() }

        if (playerWord == selectedWord) {
            playerWon()
            return
        }

        if (!wordsList.contains(playerWord) || currentRow >= MAX_RETRY - 1) {
            playerLost()
            return
        }


        val disabledLettersList = _uiState.value.disabledLetters.toMutableList()
        // else validate the worded entered by the player
        var playerWordIndex = startIndex
        for (usedWordLetter in selectedWord.toCharArray()) {

            val playerLetter = grid[playerWordIndex]

            if (playerLetter.letter == usedWordLetter) {
                grid[playerWordIndex] = playerLetter.copy(status = CellStatus.CORRECT)
            } else if (selectedWord.contains(playerLetter.letter)) {
                grid[playerWordIndex] = playerLetter.copy(status = CellStatus.WRONG_POSITION)
            } else {
                grid[playerWordIndex] = playerLetter.copy(status = CellStatus.WRONG)
                // add the letter to the disabled letters list to avoid using it again
                if (!disabledLettersList.contains(playerLetter.letter)) {
                    disabledLettersList.add(playerLetter.letter)
                }
            }
            playerWordIndex++
        }
        // move to the next row
        currentRow++
        // reset the currentRowIndex
        currentRowIndex = -1
        // update the grid state
        _uiState.value = _uiState.value.copy(
            wordsGrid = grid,
            disabledLetters = disabledLettersList,
            enableValidation = false
        )
    }

    private fun playerLost() {
        _uiState.value = _uiState.value.copy(
            gameState = GameState.GAME_OVER,
            showMessage = "used word: $selectedWord"
        )
    }

    private fun playerWon() {
        _uiState.value = _uiState.value.copy(
            gameState = GameState.GAME_WON,
            showMessage = "used word: $selectedWord"
        )
    }

    /**
     * this function is called when the player clicks on a letter in the keyboard
     * @param letter the letter that the player clicked on
     */
    private fun onKeyBoardClick(letter: Char) {
        if (_uiState.value.gameState != GameState.PLAYING) {
            return
        }
        val grid = _uiState.value.wordsGrid.toMutableList()
        val currentIndex = (currentRow * MAX_LETTERS) + this.currentRowIndex

        // avoid incrementing index in case of row > 0 && currentRowIndex == 0
        if (currentRowIndex >= MAX_LETTERS - 1 || currentIndex >= grid.size - 1) {
            //   Log.d(TAG, "onKeyBoardClick: grid row is full")
            return
        }

        // add the letter to the grid
        grid[currentIndex + 1] = GridCellData(letter, CellStatus.NULL)

        this.currentRowIndex++

        val enableValidation = currentRowIndex == MAX_LETTERS - 1

        _uiState.value = _uiState.value.copy(
            wordsGrid = grid,
            enableValidation = enableValidation
        )
    }

    /**
     * this function is called when the player clicks on the delete button in the keyboard
     * it removes the last letter that the player entered in the grid
     */
    private fun onDeleteClick() {
        if (_uiState.value.gameState != GameState.PLAYING) {
            return
        }
        if (this.currentRowIndex < 0) {
            //          Log.d(TAG, "onDeleteClick: currentRowIndex < 0")
            return
        }
        val grid = _uiState.value.wordsGrid.toMutableList()
        val currentIndex = (currentRow * MAX_LETTERS) + this.currentRowIndex

        if (currentIndex <= 0) { // the player can't remove the first letter
            //      Log.d(TAG, "onDeleteClick: the player can't remove the first letter")
            return
        }

        grid[currentIndex] = GridCellData(' ', CellStatus.NULL)

        if (currentRowIndex >= 0) {
            this.currentRowIndex--
        }

        val enableValidation = currentRowIndex == MAX_LETTERS - 1

        _uiState.value = _uiState.value.copy(
            wordsGrid = grid,
            enableValidation = enableValidation
        )
    }
}
