package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.model.Building
import com.kotlinswe.waterfountainrater.model.WaterStation
import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import com.kotlinswe.waterfountainrater.util.DistanceCalculator
import com.kotlinswe.waterfountainrater.util.DtoMappers.toDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val buildingRepository: BuildingRepository,
    private val stationRepository: WaterStationRepository,
    private val fountainRepository: WaterFountainRepository
) {
    @Transactional
    suspend fun findBuildingsNear(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<BuildingSearchResponseDto> =
        buildingRepository.findByLocation(latitude, longitude, radius)
            .map { toDto(it) }

    @Transactional
    suspend fun findNearbyBuildingsWithFountains(
        latitude: Double,
        longitude: Double,
        radius: Double
    ): List<BuildingSearchResponseDto> =
        buildingRepository.findByLocation(
            latitude = latitude,
            longitude = longitude,
            radius = radius * 1.1 // Expand radius a bit to cover edge cases
        )
            .asSequence()
            .filter { building -> isWithinRadius(latitude, longitude, building, radius) }
            .map { toDto(it) }
            .toList()

    @Transactional
    suspend fun searchFountains(query: String): List<WaterFountainDto> =
        fountainRepository.searchFountains(query).map { toDto(it) }

    private fun isWithinRadius(
        latitude: Double,
        longitude: Double,
        building: Building,
        radius: Double
    ): Boolean {
        val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
        return distance <= radius
    }

    suspend fun getStationsForBuilding(buildingId: Long): List<WaterStation> = stationRepository.findByBuildingId(buildingId)
}