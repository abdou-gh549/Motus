package com.compose.myapplication

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.compose.myapplication.screens.MainScreenContent
import com.compose.myapplication.screens.viewmodel.MainViewUiState
import com.compose.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test


/**
 * i didn't check all ui states because it is the same logic
 * just with different values you can add more tests easily
 */
class MainScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun validationButtonDisabledTest() {
        // prepare our test data

        val uiState = MainViewUiState(enableValidation = false)

        setupComposeContent(uiState)

        composeTestRule.onNodeWithTag("done_button").let { doneButton ->
            doneButton.assertExists()
            doneButton.assertIsNotEnabled()
        }
    }

    @Test
    fun validationButtonEnabledTest() {

        val uiState = MainViewUiState(enableValidation = true)

        setupComposeContent(uiState)

        composeTestRule.onNodeWithTag("done_button").let { doneButton ->
            doneButton.assertExists()
            doneButton.assertIsEnabled()
        }
    }

    private fun setupComposeContent(uiState: MainViewUiState) {
        composeTestRule.setContent {
            MyApplicationTheme {
                MainScreenContent(uiState = uiState) {}
            }
        }
    }
}