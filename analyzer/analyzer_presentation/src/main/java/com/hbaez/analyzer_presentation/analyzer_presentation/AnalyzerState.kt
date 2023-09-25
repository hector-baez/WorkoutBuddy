package com.hbaez.analyzer_presentation.analyzer_presentation

import java.time.LocalDate

data class AnalyzerState(
    val date: LocalDate = LocalDate.now(),
    val currentActivityIndex: Int = -1,
    val activityCountList: List<Int> = List(52) { 0 }
)