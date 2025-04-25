package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountain
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface WaterFountainRepository : JpaRepository<WaterFountain, Long> {
    override fun findAll(): List<WaterFountain>
    override fun findById(id: Long): Optional<WaterFountain>
    fun findByStationId(stationId: Long): List<WaterFountain>
    fun save(fountain: WaterFountain): WaterFountain

    @Query("SELECT wf FROM WaterFountain wf ORDER BY " +
            "(SELECT AVG((r.tasteRating + r.flowRating + r.temperatureRating + r.ambienceRating + r.usabilityRating)/5) " +
            "FROM WaterFountainReview r WHERE r.waterFountain = wf) DESC")
    fun findTopRated(pageable: Pageable): Page<WaterFountain>
}