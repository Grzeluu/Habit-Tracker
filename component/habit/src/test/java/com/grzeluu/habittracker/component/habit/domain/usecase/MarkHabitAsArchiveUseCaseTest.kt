package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationManager
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MarkHabitAsArchiveUseCaseTest {

    private lateinit var markHabitAsArchiveUseCase: MarkHabitAsArchiveUseCase
    private val habitRepository: HabitRepository = mockk()
    private val notificationRepository: NotificationRepository = mockk()
    private val notificationManager: NotificationManager = mockk()
    private val notificationScheduler: NotificationScheduler = mockk()

    @BeforeEach
    fun setUp() {
        markHabitAsArchiveUseCase = MarkHabitAsArchiveUseCase(
            habitRepository,
            notificationRepository,
            notificationManager,
            notificationScheduler
        )
    }

    @Test
    fun `should mark habit as archived and delete notification when archived`() = runTest {
        // Arrange
        val habitId = 1L
        val isArchived = true
        val habit = mockk<Habit>()
        val notification = mockk<HabitNotification>()
        val habitNotificationSetting = HabitNotificationSetting.Enabled(LocalTime(17, 0))

        every { habit.notification } returns habitNotificationSetting
        coEvery { habitRepository.getHabit(habitId) } returns flowOf(habit)
        coEvery { habitRepository.markHabitAsArchived(habitId, isArchived) } just Runs
        coEvery { notificationRepository.deleteHabitNotificationByHabitId(habitId) } returns notification
        coEvery { notificationScheduler.cancelNotification(notification) } just Runs

        // Act
        val result = markHabitAsArchiveUseCase.execute(MarkHabitAsArchiveUseCase.Request(habitId, isArchived))

        // Assert
        verify { habitRepository.getHabit(habitId) }
        coVerify { habitRepository.markHabitAsArchived(habitId, isArchived) }
        coVerify { notificationRepository.deleteHabitNotificationByHabitId(habitId) }
        verify { notificationScheduler.cancelNotification(notification) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should mark habit as not archived and schedule notification when not archived`() = runTest {
        // Arrange
        val habitId = 1L
        val isArchived = false
        val habit = mockk<Habit>()
        val notification = mockk<HabitNotification>()
        val habitNotificationSetting = HabitNotificationSetting.Enabled(LocalTime(17, 0))

        every { habit.notification } returns habitNotificationSetting

        coEvery { habitRepository.getHabit(habitId) } returns flowOf(habit)
        coEvery { habitRepository.markHabitAsArchived(habitId, isArchived) } just Runs
        coEvery { notificationManager.calculateNextNotificationForHabit(habit) } returns notification
        coEvery { notificationRepository.addOrUpdateHabitNotification(notification) } just Runs
        coEvery { notificationScheduler.scheduleNotification(notification) } just Runs

        // Act
        val result = markHabitAsArchiveUseCase.execute(MarkHabitAsArchiveUseCase.Request(habitId, isArchived))

        // Assert
        verify { habitRepository.getHabit(habitId) }
        coVerify { habitRepository.markHabitAsArchived(habitId, isArchived) }
        coVerify { notificationManager.calculateNextNotificationForHabit(habit) }
        coVerify { notificationRepository.addOrUpdateHabitNotification(notification) }
        coVerify { notificationScheduler.scheduleNotification(notification) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should return error when habit is not found`() = runTest {
        // Arrange
        val habitId = 1L
        val isArchived = true

        coEvery { habitRepository.getHabit(habitId) } returns flowOf(null)

        // Act
        val result = markHabitAsArchiveUseCase.execute(MarkHabitAsArchiveUseCase.Request(habitId, isArchived))

        // Assert
        verify { habitRepository.getHabit(habitId) }
        assertInstanceOf(Result.Error::class.java, result)
        assertEquals(BaseError.READ_ERROR, (result as Result.Error).error)
    }

    @Test
    fun `should return error when repository throws exception`() = runTest {
        // Arrange
        val habitId = 1L
        val isArchived = true
        val exception = RuntimeException("Database error")

        coEvery { habitRepository.getHabit(habitId) } throws exception

        // Act
        val result = markHabitAsArchiveUseCase.execute(MarkHabitAsArchiveUseCase.Request(habitId, isArchived))

        // Assert
        verify { habitRepository.getHabit(habitId) }
        assertInstanceOf(Result.Error::class.java, result)
        assertEquals(BaseError.SAVE_ERROR, (result as Result.Error).error)
    }
}
