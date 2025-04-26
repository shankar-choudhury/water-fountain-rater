package com.kotlinswe.waterfountainrater.dto.report

import com.kotlinswe.waterfountainrater.model.WaterFountain
import java.time.Instant

data class ReportResponseDto(
    val id: Long,
    val fountainId: Long,
    val status: WaterFountain.FountainStatus,
    val reportContents: String,
    val reportedAt: Instant
)
