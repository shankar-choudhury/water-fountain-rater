package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.Building
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BuildingRepository : JpaRepository<Building, Long> {
    @Query("SELECT b FROM Building b WHERE " +
            "ABS(b.latitude - :lat) <= :radius AND ABS(b.longitude - :lon) <= :radius")
    fun findByLocation(
        @Param("lat") latitude: Double,
        @Param("lon") longitude: Double,
        @Param("radius") radius: Double
    ): List<Building>
}