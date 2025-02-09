package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetHabitsNotificationsUseCaseTest {

    private lateinit var getHabitsNotificationsUseCase: GetHabitsNotificationsUseCase
    private val notificationRepository: NotificationRepository = mockk()

    @BeforeEach
    fun setUp() {
        getHabitsNotificationsUseCase = GetHabitsNotificationsUseCase(notificationRepository)
    }

    @Test
    fun `should return success when repository returns data`() = runTest {
        // Arrange
        val notification = mockk<HabitNotification>()
        val expectedResult = listOf(notification, notification)
        coEvery { notificationRepository.getHabitsNotification() } returns flowOf(expectedResult)

        // Act
        val result = getHabitsNotificationsUseCase.invoke(Unit).toList()

        // Assert
        verify { notificationRepository.getHabitsNotification() }
        assertEquals(1, result.size)
        assertEquals(Result.Success(expectedResult), result.first())
    }

    @Test
    fun `should return error when repository throws an exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Database error")
        coEvery { notificationRepository.getHabitsNotification() } returns flow { throw exception }

        // Act
        val result = getHabitsNotificationsUseCase.invoke(Unit).toList()

        // Assert
        verify { notificationRepository.getHabitsNotification() }
        assertEquals(1, result.size)
        assertInstanceOf(Result.Error::class.java, result.first())
        assertEquals(BaseError.READ_ERROR, (result.first() as Result.Error).error)
    }
}