package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountainReport
import org.springframework.data.jpa.repository.JpaRepository

interface WaterFountainReportRepository: JpaRepository<WaterFountainReport, Long> {
    fun findAllByWaterFountainId(fountainId: Long): List<WaterFountainReport>
}