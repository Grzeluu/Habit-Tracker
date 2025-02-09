package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetDailyHabitInfosUseCaseTest {

    private lateinit var getDailyHabitInfosUseCase: GetDailyHabitInfosUseCase
    private val habitRepository: HabitRepository = mockk()

    @BeforeEach
    fun setUp() {
        getDailyHabitInfosUseCase = GetDailyHabitInfosUseCase(habitRepository)
    }

    @Test
    fun `should return success when repository returns data`() = runTest {
        // Arrange
        val testDate = LocalDate(2025, 2, 9)
        val habit = mockk<DailyHabitInfo>()
        val expectedResult = listOf(habit, habit)
        coEvery { habitRepository.getDailyHabitInfos(any(), any()) } returns flowOf(expectedResult)

        // Act
        val result =
            getDailyHabitInfosUseCase.invoke(GetDailyHabitInfosUseCase.Request(testDate)).toList()

        // Assert
        verify { habitRepository.getDailyHabitInfos(any(), any()) }
        assertEquals(1, result.size)
        assertEquals(Result.Success(expectedResult), result.first())
    }

    @Test
    fun `should return error when repository throws an exception`() = runTest {
        // Arrange
        val testDate = LocalDate(2025, 2, 9)
        val exception = RuntimeException("Database error")
        coEvery { habitRepository.getDailyHabitInfos(any(), any()) } returns flow { throw exception }

        // Act
        val result = getDailyHabitInfosUseCase.invoke(GetDailyHabitInfosUseCase.Request(testDate)).toList()

        // Assert
        verify { habitRepository.getDailyHabitInfos(any(), any()) }
        assertEquals(1, result.size)
        assertInstanceOf(Result.Error::class.java, result.first())
        assertEquals(BaseError.READ_ERROR, (result.first() as Result.Error).error)
    }
}