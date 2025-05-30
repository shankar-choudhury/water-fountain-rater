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

    @Query("""
        SELECT wf FROM WaterFountain wf 
        JOIN wf.station s
        WHERE LOWER(CAST(wf.type AS string)) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    fun searchFountains(@Param("query") query: String): List<WaterFountain>

    // For stats
    @Query("""
        SELECT wf FROM WaterFountain wf
        LEFT JOIN FETCH wf.reviews
        WHERE wf.id IN (
            SELECT wf2.id FROM WaterFountain wf2
            LEFT JOIN wf2.reviews r
            GROUP BY wf2.id
            ORDER BY COALESCE(AVG(
                (r.tasteRating + r.flowRating + r.temperatureRating + 
                 r.ambienceRating + r.usabilityRating)/5.0
            ), 0) DESC
        )
    """)
    fun findTopRated(pageable: Pageable): Page<WaterFountain>

    @Query("""
        SELECT wf FROM WaterFountain wf
        LEFT JOIN FETCH wf.reviews
        WHERE wf.id IN (
            SELECT wf2.id FROM WaterFountain wf2
            LEFT JOIN wf2.reviews r
            GROUP BY wf2.id
            ORDER BY COALESCE(AVG(
                (r.tasteRating + r.flowRating + r.temperatureRating + 
                 r.ambienceRating + r.usabilityRating)/5.0
            ), 0) ASC
        )
    """)
    fun findWorstRated(pageable: Pageable): Page<WaterFountain>
}