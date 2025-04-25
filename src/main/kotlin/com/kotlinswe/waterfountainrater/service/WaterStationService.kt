package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import org.springframework.stereotype.Service

@Service
class WaterStationService(
    private val stationRepository: WaterStationRepository,
    private val fountainRepository: WaterFountainRepository
) {
    suspend fun showDetails(stationId: Long) {
        val station = stationRepository.findById(stationId).orElse(null)
        if (station == null) {
            println("Water Station not found.")
            return
        }

        println("Water Station: (${station.id})")
        val fountains = fountainRepository.findByStationId(station.id)
        if (fountains.isEmpty()) {
            println("No water fountains found.")
        } else {
            println("Water Fountains:")
            fountains.forEach {
                println("- ${it.id}")
            }
        }
    }

    suspend fun listStations() {
        val stations = stationRepository.findAll()
        if (stations.isEmpty()) {
            println("No water stations found.")
        } else {
            println("All Water Stations:")
            stations.forEach {
                println("- (${it.id})")
            }
        }
    }
}