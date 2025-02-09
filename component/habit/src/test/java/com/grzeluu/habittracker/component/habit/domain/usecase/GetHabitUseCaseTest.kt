package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetHabitUseCaseTest {
    private lateinit var getHabitsUseCase: GetHabitUseCase
    private val habitRepository: HabitRepository = mockk()

    @BeforeEach
    fun setUp() {
        getHabitsUseCase = GetHabitUseCase(habitRepository)
    }

    @Test
    fun `should return success when repository returns data`() = runTest {
        // Arrange
        val habitId = 1L
        val habit = mockk<Habit>()
        coEvery { habitRepository.getHabit(habitId) } returns flowOf(habit)

        //Act
        val result = getHabitsUseCase.invoke(
            GetHabitUseCase.Request(habitId)
        ).toList()

        //Assert
        verify { habitRepository.getHabit(habitId) }
        assertEquals(1, result.size)
        assertEquals(Result.Success(habit), result.first())
    }

    @Test
    fun `should return error when repository throws an exception`() = runTest {
        // Arrange
        val habitId = 1L
        val exception = RuntimeException("Database error")
        coEvery { habitRepository.getHabit(habitId) } returns flow { throw exception }

        //Act
        val result = getHabitsUseCase.invoke(
            GetHabitUseCase.Request(habitId)
        ).toList()

        //Assert
        verify { habitRepository.getHabit(habitId) }
        assertEquals(1, result.size)
        assertInstanceOf(Result.Error::class.java, result.first())
        assertEquals(BaseError.READ_ERROR, (result.first() as Result.Error).error)
    }
}