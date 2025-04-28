package com.kotlinswe.waterfountainrater.cli.formatter

import com.kotlinswe.waterfountainrater.dto.report.ReportResponseDto

fun formatAll(report: ReportResponseDto): String {
    return """
        ⚠️ Report ${report.id} for Fountain ${report.fountainId}
           Status: ${report.status}
           Details: ${report.reportContents.take(50)}...
    """.trimIndent()
}

fun formatById(report: ReportResponseDto): String {
    return """
        ⚠️ Report ${report.id}
           Status: ${report.status}
           Details: ${report.reportContents.take(50)}...
    """.trimIndent()
}