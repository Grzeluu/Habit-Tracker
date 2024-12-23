package com.grzeluu.habittracker.util.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun getCurrentDate() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date