package com.example.todoapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TodoItem(
    val title: String,
    val description: String? = null,
    val isChecked: Boolean = false
)

data class TodoUiState(
    val todoItems: List<TodoItem> = emptyList(),
    val currentTitle: String = "",
    val currentDescription: String = ""
)

class TodoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(currentTitle = title) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(currentDescription = description) }
    }

    fun addTodoItem() {
        val currentState = _uiState.value
        if (currentState.currentTitle.isBlank()) return

        val newItem = TodoItem(
            title = currentState.currentTitle,
            description = currentState.currentDescription.takeIf { it.isNotBlank() }
        )

        _uiState.update { state ->
            state.copy(
                todoItems = state.todoItems + newItem,
                currentTitle = "",
                currentDescription = ""
            )
        }
    }

    fun toggleTodoItem(index: Int) {
        _uiState.update { state ->
            val updatedItems = state.todoItems.toMutableList()
            updatedItems[index] = updatedItems[index].copy(
                isChecked = !updatedItems[index].isChecked
            )
            state.copy(todoItems = updatedItems)
        }
    }
}