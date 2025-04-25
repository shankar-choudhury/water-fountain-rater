package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.dto.report.ReportRequestDto
import com.kotlinswe.waterfountainrater.dto.report.ReportResponseDto
import com.kotlinswe.waterfountainrater.service.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val reportService: ReportService
) {

    @PostMapping("/{fountainId}")
    suspend fun submitReport(
        @PathVariable fountainId: Long,
        @RequestBody requestDto: ReportRequestDto
    ): ResponseEntity<ReportResponseDto> {
        val responseDto = reportService.submitReport(
            fountainId = fountainId,
            status = requestDto.status,
            reportContents = requestDto.reportContents
        )
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping
    suspend fun getAllReports(): ResponseEntity<List<ReportResponseDto>> {
        val responseDtos = reportService.getAllReports()
        return ResponseEntity.ok(responseDtos)
    }

    @GetMapping("/{fountainId}")
    suspend fun getReportsForFountain(@PathVariable fountainId: Long): ResponseEntity<List<ReportResponseDto>> {
        val responseDtos = reportService.getReportsForFountain(fountainId)
        return ResponseEntity.ok(responseDtos)
    }
}