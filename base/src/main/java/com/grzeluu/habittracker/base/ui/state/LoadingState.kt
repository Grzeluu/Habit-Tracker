package com.grzeluu.habittracker.base.ui.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class LoadingState @Inject constructor() {

    private val taskCount = MutableStateFlow(0)
    val isInProgress: Flow<Boolean> = taskCount.map { it > 0 }

    fun incrementTasksInProgress() {
        taskCount.update { it + 1 }
    }

    fun decrementTasksInProgress() {
        taskCount.update { (it - 1).coerceAtLeast(0) }
    }

    fun stopLoading() {
        taskCount.update { 0 }
    }
}