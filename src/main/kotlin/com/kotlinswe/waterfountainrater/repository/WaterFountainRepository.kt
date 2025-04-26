package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountain
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface WaterFountainRepository : JpaRepository<WaterFountain, Long> {
    override fun findAll(): List<WaterFountain>
    override fun findById(id: Long): Optional<WaterFountain>
    fun findByStationId(stationId: Long): List<WaterFountain>

    @Query("SELECT wf FROM WaterFountain wf WHERE " +
            "wf.type LIKE %:query% OR wf.station.description LIKE %:query%")
    fun searchFountains(@Param("query") query: String): List<WaterFountain>

    // For stats
    @Query("""
        SELECT wf FROM WaterFountain wf 
        LEFT JOIN WaterFountainReview r ON r.waterFountain = wf
        GROUP BY wf
        ORDER BY COALESCE(AVG(
            (r.tasteRating + r.flowRating + r.temperatureRating + 
             r.ambienceRating + r.usabilityRating)/5.0
        ), 0) DESC
    """)
    fun findTopRated(pageable: Pageable): Page<WaterFountain>

    @Query("""
        SELECT wf FROM WaterFountain wf 
        LEFT JOIN WaterFountainReview r ON r.waterFountain = wf
        GROUP BY wf
        ORDER BY COALESCE(AVG(
            (r.tasteRating + r.flowRating + r.temperatureRating + 
             r.ambienceRating + r.usabilityRating)/5.0
        ), 0) ASC
    """)
    fun findWorstRated(pageable: Pageable): Page<WaterFountain>
}