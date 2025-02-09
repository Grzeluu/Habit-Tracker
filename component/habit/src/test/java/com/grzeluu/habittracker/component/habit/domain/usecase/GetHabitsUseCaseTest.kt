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

class GetHabitsUseCaseTest {
    private lateinit var getHabitsUseCase: GetHabitsUseCase
    private val habitRepository: HabitRepository = mockk()

    @BeforeEach
    fun setUp() {
        getHabitsUseCase = GetHabitsUseCase(habitRepository)
    }

    @Test
    fun `should return success when repository returns data`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        val expectedResult = listOf(habit, habit)
        coEvery { habitRepository.getHabits() } returns flowOf(expectedResult)

        //Act
        val result = getHabitsUseCase.invoke(Unit).toList()

        //Assert
        verify { habitRepository.getHabits() }
        assertEquals(1, result.size)
        assertEquals(Result.Success(expectedResult), result.first())
    }

    @Test
    fun `should return error when repository throws an exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Database error")
        coEvery { habitRepository.getHabits() } returns flow { throw exception }

        //Act
        val result = getHabitsUseCase.invoke(Unit).toList()

        //Assert
        verify { habitRepository.getHabits() }
        assertEquals(1, result.size)
        assertInstanceOf(Result.Error::class.java, result.first())
        assertEquals(BaseError.READ_ERROR, (result.first() as Result.Error).error)
    }
}