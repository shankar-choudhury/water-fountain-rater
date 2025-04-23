package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.model.Building
import com.kotlinswe.waterfountainrater.model.WaterStation
import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val buildingRepository: BuildingRepository,
    private val stationRepository: WaterStationRepository
) {
    @Transactional
    suspend fun findBuildingsNear(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<Building> = withContext(Dispatchers.IO) {
        buildingRepository.findByLocation(latitude, longitude, radius)
    }

    suspend fun getStationsForBuilding(buildingId: Long): List<WaterStation> =
        withContext(Dispatchers.IO) {
            stationRepository.findByBuildingId(buildingId)
        }
}