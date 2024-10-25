package com.grzeluu.habittracker.base.ui.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class LoadingState @Inject constructor() {

    private val count = AtomicInteger()
    private val taskCount = MutableStateFlow(0)

    val isInProgress: Flow<Boolean> = taskCount.map { it > 0 }

    fun incrementTasksInProgress() {
        taskCount.update { count.incrementAndGet() }
    }

    fun decrementTasksInProgress() {
        taskCount.update {
            val newCount = count.decrementAndGet()
            newCount.coerceAtLeast(0)
        }
    }

    suspend fun stopLoading() {
        count.set(0)
        taskCount.emit(0)
    }
}