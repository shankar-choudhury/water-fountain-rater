package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.model.Building
import com.kotlinswe.waterfountainrater.model.WaterStation
import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import jakarta.transaction.Transactional
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
    ): List<Building> =
        buildingRepository.findByLocation(latitude, longitude, radius)

    @Transactional
    suspend fun findNearbyBuildingsWithFountains(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<BuildingSearchResponseDto> {
        return buildingRepository
            .findByLocation(latitude, longitude, radius)
            .map(BuildingSearchResponseDto::from)
    }


    suspend fun getStationsForBuilding(buildingId: Long): List<WaterStation> = stationRepository.findByBuildingId(buildingId)
}