package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface WaterFountainReportRepository: JpaRepository<WaterFountainReport, Long> {
    fun findAllByWaterFountainId(fountainId: Long): List<WaterFountainReport>
    @Query("SELECT wf FROM WaterFountain wf WHERE " +
            "wf.type LIKE %:query% OR wf.station.description LIKE %:query%")
    fun searchFountains(@Param("query") query: String): List<WaterFountain>

    fun countByStatus(status: WaterFountain.FountainStatus): Long

    @Query("SELECT COUNT(r) FROM WaterFountainReport r WHERE r.status = 'BROKEN'")
    fun countBrokenFountains(): Long

    @Query("""
        SELECT COUNT(DISTINCT r.waterFountain) 
        FROM WaterFountainReport r 
        WHERE r.status = 'BROKEN'
    """)
    fun countFountainsWithBrokenReports(): Long

    fun countByWaterFountainId(fountainId: Long): Long
}