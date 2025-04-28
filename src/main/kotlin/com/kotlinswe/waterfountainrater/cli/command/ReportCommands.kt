package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.ReportService
import kotlinx.coroutines.coroutineScope

import com.kotlinswe.waterfountainrater.cli.util.listEntities
import com.kotlinswe.waterfountainrater.cli.formatter.formatAll
import com.kotlinswe.waterfountainrater.cli.formatter.formatById
import com.kotlinswe.waterfountainrater.model.WaterFountain
import java.util.*

class ReportCommands(
    private val reportService: ReportService,
) {
    suspend fun listReports(args: String) = coroutineScope {
        listEntities(
            args = args,
            listAll = { reportService.getAllReports() },
            findById = { fountainId -> reportService.getReportsForFountain(fountainId) },
            formatAll = { report -> formatAll(report) },
            formatById = { reportId -> formatById(reportId) },
            usageMessage = "Usage: reports [fountainId]"
        )
    }

    suspend fun submitReport(args: String, reportContents: String?) = coroutineScope {
        val parts = args.split(" ")
        if (parts.size < 2) {
            println("Usage: report [fountainId] [issue]")
            return@coroutineScope
        }

        val fountainId = parts[0].toLongOrNull() ?: run {
            println("❌ Invalid fountain ID")
            return@coroutineScope
        }

        val status = when (parts[1].lowercase()) {
            "broken" -> WaterFountain.FountainStatus.BROKEN
            "leaking" -> WaterFountain.FountainStatus.OUT_OF_ORDER
            "dirty" -> WaterFountain.FountainStatus.UNDER_REPAIR
            else -> WaterFountain.FountainStatus.NONE
        }

        if (reportContents.isNullOrBlank()) {
            println("❌ Report contents are required!")
            return@coroutineScope
        }

        try {
            val report = reportService.submitReport(fountainId, status, reportContents)
            println("✅ Reported issue for fountain $fountainId (Status: ${report.status})")
        } catch (e: Exception) {
            println("❌ Failed to submit report: ${e.message}")
        }
    }


}