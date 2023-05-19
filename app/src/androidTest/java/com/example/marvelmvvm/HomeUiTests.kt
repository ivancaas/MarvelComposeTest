package com.example.marvelmvvm

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.marvelmvvm.presentation.ui.MainActivity
import com.example.marvelmvvm.presentation.ui.home.SearchBar
import org.junit.Rule
import org.junit.Test

class HomeUiTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

//    @Before
//    fun setup(){
//
//    }
    @Test
    fun searchbar_valueChanged() {
        // Launch the composable

        composeTestRule.setContent {
            SearchBar(
                query = "",
                onValueChange = { }
            )
        }
        // Find the TextField by content description
        val textField = composeTestRule.onNodeWithContentDescription("Search Bar")

        // Verify initial value
        textField.assert(hasText(""))

        // Enter text into the TextField
        textField.performTextInput("Hello")

        // Verify the value has changed
        textField.assert(hasText("Hello"))
    }

    @Test
    fun searchbar_clearButton() {
        // Launch the composable
        composeTestRule.setContent {
            SearchBar(
                query = "",
                onValueChange = { }
            )
        }
        val textField = composeTestRule.onNodeWithContentDescription("Search Bar")
        textField.performTextInput("Hello")
        textField.assert(hasText("Hello"))
        // Find the clear button by content description
        val clearButton = composeTestRule.onNodeWithContentDescription("Clear text")

        // Click the clear button
        clearButton.performClick()

        // Verify the TextField is cleared
        textField.assert(hasText(""))
    }

}