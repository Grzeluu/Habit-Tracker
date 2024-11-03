package com.grzeluu.habittracker.feature.addhabit.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddHabitScreen(
    onNavigateToMainPage: () -> Unit,
) {
    BackHandler {
        onNavigateToMainPage()
    }

    Text("Add Habit")
}