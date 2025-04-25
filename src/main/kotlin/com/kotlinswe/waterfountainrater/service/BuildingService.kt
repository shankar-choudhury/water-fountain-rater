package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import org.springframework.stereotype.Service

@Service
class BuildingService(
    private val buildingRepository: BuildingRepository,
    private val stationRepository: WaterStationRepository
) {
    suspend fun showDetails(buildingId: Long) =
        buildingRepository.findById(buildingId)
            .map { building -> BuildingSearchResponseDto.from(building)}
            .orElse(
                BuildingSearchResponseDto(
                    id = 0,
                    name = "",
                    latitude = 0.0,
                    longitude = 0.0,
                    waterStations = emptyList()
                )
            )

    suspend fun listBuildings() {
        val buildings = buildingRepository.findAll()
        if (buildings.isEmpty()) {
            println("No buildings found.")
        } else {
            println("All Buildings:")
            buildings.forEach {
                println("- ${it.name} (${it.id})")
            }
        }
    }
}