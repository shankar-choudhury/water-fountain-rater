package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import org.springframework.stereotype.Service

@Service
class BuildingService(
    private val buildingRepository: BuildingRepository,
    private val stationRepository: WaterStationRepository
) {
    suspend fun showDetails(buildingId: Long) {
        val building = buildingRepository.findById(buildingId).orElse(null)
        if (building == null) {
            println("Building not found.")
            return
        }

        println("Building: ${building.name} (${building.id})")
        val stations = stationRepository.findByBuildingId(building.id)
        if (stations.isEmpty()) {
            println("No water stations found.")
        } else {
            println("Water Stations:")
            stations.forEach {
                println("- (${it.id})")
            }
        }
    }

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