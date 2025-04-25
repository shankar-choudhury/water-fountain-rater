package com.kotlinswe.waterfountainrater.dto.report

import com.kotlinswe.waterfountainrater.model.WaterFountain

data class ReportRequestDto(
    val status: WaterFountain.FountainStatus,
    val reportContents: String
)
