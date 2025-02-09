package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.grzeluu.habittracker.base.domain.result.Result
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.assertInstanceOf

class CheckIfHabitsAreAddedUseCaseTest {
    private lateinit var useCase: CheckIfHabitsAreAddedUseCase
    private val habitRepository: HabitRepository = mockk()

    @BeforeEach
    fun setUp() {
        useCase = CheckIfHabitsAreAddedUseCase(
            habitRepository
        )
    }

    @Test
    fun `should return true when habits exist`() = runTest {
        // Arrange
        every { habitRepository.getHabitsCount() } returns flowOf(5)

        // Act
        val result = useCase.invoke(Unit).toList()

        // Assert
        verify { habitRepository.getHabitsCount() }
        assertEquals(1, result.size)
        assertEquals(Result.Success(true), result.first())
    }

    @Test
    fun `should return false when habits not exist`() = runTest {
        // Arrange
        every { habitRepository.getHabitsCount() } returns flowOf(0)

        // Act
        val result = useCase.invoke(Unit).toList()

        // Assert
        verify { habitRepository.getHabitsCount() }
        assertEquals(1, result.size)
        assertEquals(Result.Success(false), result.first())
    }

    @Test
    fun `should return error when exception occurs`() = runTest {
        // Arrange
        val exception = RuntimeException("Database error")
        every { habitRepository.getHabitsCount() } returns flow { throw exception }

        // Act
        val result = useCase.invoke(Unit).toList()

        // Assert
        verify { habitRepository.getHabitsCount() }
        assertEquals(1, result.size)
        assertInstanceOf(Result.Error::class.java, result.first())
        assertEquals(BaseError.READ_ERROR, (result.first() as Result.Error).error)
    }
}