package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterStation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WaterStationRepository : JpaRepository<WaterStation, Long> {
    override fun findAll(): List<WaterStation>
    override fun findById(id: Long): Optional<WaterStation>
    fun findByBuildingId(buildingId: Long): List<WaterStation>
    fun save(station: WaterStation): WaterStation
}