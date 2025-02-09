package com.grzeluu.habittracker.component.habit.domain.scheduler

import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.util.enums.Day
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NotificationManagerImplTest {

    private lateinit var notificationManagerImpl: NotificationManagerImpl

    @BeforeEach
    fun setUp() {
        notificationManagerImpl = NotificationManagerImpl()
        mockkObject(Clock.System)
    }

    @Test
    fun `should calculate next notification correctly for current day`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        every { Clock.System.now() } returns LocalDateTime(2025, 2, 1, 12, 0, 0).toInstant(TimeZone.UTC)

        val nextNotificationDateTime = LocalDateTime(2025, 2, 2, 17, 0, 0)
        val notificationSetting = HabitNotificationSetting.Enabled(time = LocalTime(17, 0))
        every { habit.notification } returns notificationSetting

        coEvery { habit.notification } returns notificationSetting
        coEvery { habit.desirableDays } returns listOf(Day.SUNDAY, Day.MONDAY, Day.WEDNESDAY)

        // Act
        val notification = notificationManagerImpl.calculateNextNotificationForHabit(habit)

        // Assert
        assertEquals(nextNotificationDateTime, notification.dateTime)
    }

    @Test
    fun `should throw error when notification is disabled`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        every { habit.notification } returns HabitNotificationSetting.Disabled

        // Act & Assert
        assertThrows<RuntimeException> {
            notificationManagerImpl.calculateNextNotificationForHabit(habit)
        }
    }

    @Test
    fun `should calculate next notification to the next valid day`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        every { Clock.System.now() } returns LocalDateTime(2025, 2, 3, 12, 0, 0).toInstant(TimeZone.UTC)

        val nextNotificationDateTime = LocalDateTime(2025, 2, 7, 17, 0, 0)
        val notificationSetting = HabitNotificationSetting.Enabled(time = LocalTime(17, 0))
        every { habit.notification } returns notificationSetting

        coEvery { habit.notification } returns notificationSetting
        coEvery { habit.desirableDays } returns listOf(Day.SUNDAY, Day.FRIDAY)

        // Act
        val notification = notificationManagerImpl.calculateNextNotificationForHabit(habit)

        // Assert
        assertEquals(nextNotificationDateTime, notification.dateTime)
    }
}