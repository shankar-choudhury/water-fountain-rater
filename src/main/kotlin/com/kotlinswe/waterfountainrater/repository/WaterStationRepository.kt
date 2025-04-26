package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterStation
import org.springframework.data.jpa.repository.JpaRepository

interface WaterStationRepository : JpaRepository<WaterStation, Long> {
    fun findByBuildingId(buildingId: Long): List<WaterStation>
}