package com.kotlinswe.waterfountainrater.dto.report

import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReport
import java.time.Instant

data class ReportResponseDto(
    val id: Long,
    val fountainId: Long,
    val status: WaterFountain.FountainStatus,
    val reportContents: String,
    val reportedAt: Instant
) {
    companion object {
        fun from(report: WaterFountainReport): ReportResponseDto =
            ReportResponseDto(
                id = report.id,
                fountainId = report.waterFountain.id,
                status = report.status,
                reportContents = report.reportContents,
                reportedAt = report.reportedAt
            )
    }
}
