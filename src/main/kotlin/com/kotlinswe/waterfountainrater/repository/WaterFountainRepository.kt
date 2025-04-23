package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountain
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WaterFountainRepository : JpaRepository<WaterFountain, Long> {
    override fun findAll(): List<WaterFountain>
    override fun findById(id: Long): Optional<WaterFountain>
    fun findByStationId(stationId: Long): List<WaterFountain>
    fun save(fountain: WaterFountain): WaterFountain
}