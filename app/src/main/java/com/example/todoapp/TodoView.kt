package com.example.todoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoView(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = uiState.currentTitle,
                onValueChange = { if (it.length <= 30) viewModel.updateTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter title (max 30 characters)") },
                singleLine = true,
            )
            
            TextField(
                value = uiState.currentDescription,
                onValueChange = { if (it.length <= 100) viewModel.updateDescription(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter description (optional, max 100 characters)") },
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.currentTitle.length > 30 || uiState.currentDescription.length > 100) {
                    Text(
                        text = when {
                            uiState.currentTitle.length > 30 -> "Title exceeds 30 characters"
                            else -> "Description exceeds 100 characters"
                        },
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                
                Button(
                    onClick = { viewModel.addTodoItem() },
                    enabled = uiState.currentTitle.isNotBlank() 
                             && uiState.currentTitle.length <= 30 
                             && uiState.currentDescription.length <= 100
                ) {
                    Text("Add")
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(uiState.todoItems) { index, todo ->
                TodoListItem(
                    todo = todo,
                    onCheckedChange = { viewModel.toggleTodoItem(index) }
                )
            }
        }
    }
}

@Composable
fun TodoListItem(
    todo: TodoItem,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = if (todo.description == null) Alignment.CenterVertically else Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = todo.isChecked,
            onCheckedChange = { onCheckedChange() }
        )
        Column {
            Text(
                text = todo.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (todo.isChecked) Color.Gray else Color.Black,
                textDecoration = if (todo.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )
            todo.description?.let { description ->
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = if (todo.isChecked) Color.Gray else Color.Gray.copy(alpha = 0.7f),
                    textDecoration = if (todo.isChecked) TextDecoration.LineThrough else TextDecoration.None
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoViewPreview() {
    TodoAppTheme {
        TodoView()
    }
}