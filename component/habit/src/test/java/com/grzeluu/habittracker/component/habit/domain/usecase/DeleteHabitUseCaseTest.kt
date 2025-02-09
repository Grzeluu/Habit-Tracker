package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteHabitUseCaseTest {

    private lateinit var useCase: DeleteHabitUseCase
    private val habitRepository: HabitRepository = mockk()
    private val notificationRepository: NotificationRepository = mockk()
    private val notificationScheduler: NotificationScheduler = mockk()

    @BeforeEach
    fun setUp() {
        useCase = DeleteHabitUseCase(
            habitRepository,
            notificationRepository,
            notificationScheduler
        )
    }

    @Test
    fun `should delete habit without cancelling notification when notification is disabled`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        every { habit.notification } returns HabitNotificationSetting.Disabled
        coEvery { habitRepository.deleteHabit(habit) } returns Unit

        // Act
        val result = useCase.invoke(habit)

        // Assert
        assertEquals(Result.Success(Unit), result)
        coVerify { habitRepository.deleteHabit(habit) }
        coVerify(exactly = 0) { notificationRepository.deleteHabitNotificationByHabitId(any()) }
        coVerify(exactly = 0) { notificationScheduler.cancelNotification(any()) }
    }

    @Test
    fun `should delete habit and cancel notification when notification is enabled`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        val notification = mockk<HabitNotification>()
        every { habit.notification } returns HabitNotificationSetting.Enabled(LocalTime(17, 0))
        every { habit.id } returns 1
        coEvery { habitRepository.deleteHabit(habit) } returns Unit
        coEvery { notificationRepository.deleteHabitNotificationByHabitId(any()) } returns notification
        coEvery { notificationScheduler.cancelNotification(any()) } returns Unit
        coEvery { habitRepository.deleteHabit(habit) } returns Unit

        // Act
        val result = useCase.invoke(habit)

        // Assert
        assertEquals(Result.Success(Unit), result)
        coVerify { habitRepository.deleteHabit(habit) }
        coVerify { notificationRepository.deleteHabitNotificationByHabitId(1) }
        coVerify { notificationScheduler.cancelNotification(any()) }
    }

    @Test
    fun `should return error when exception occurs`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        val exception = RuntimeException("Database error")
        coEvery { habitRepository.deleteHabit(any()) } throws exception

        // Act
        val result = useCase.invoke(habit)

        // Assert
        assertEquals(Result.Error<BaseError>(BaseError.SAVE_ERROR), result)
    }
}