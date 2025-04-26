package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.report.ReportResponseDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReport
import com.kotlinswe.waterfountainrater.repository.WaterFountainReportRepository
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.dto.DtoMappers.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ReportService(
    private val reportRepository: WaterFountainReportRepository,
    private val fountainRepository: WaterFountainRepository
) {

    @Transactional
    suspend fun submitReport(
        fountainId: Long,
        status: WaterFountain.FountainStatus,
        reportContents: String
    ): ReportResponseDto = withContext(Dispatchers.IO) {
        val fountain = fountainRepository.findById(fountainId)
            .orElseThrow { IllegalArgumentException("Water fountain with id $fountainId not found") }

        val report = WaterFountainReport(
            waterFountain = fountain,
            status = status,
            reportContents = reportContents
        )

        toDto(reportRepository.save(report))
    }

    suspend fun getAllReports(): List<ReportResponseDto> =
        reportRepository.findAll()
            .map { toDto(it) }

    suspend fun getReportsForFountain(fountainId: Long): List<ReportResponseDto> =
        reportRepository.findAllByWaterFountainId(fountainId)
            .map { toDto(it) }
}