package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import org.springframework.stereotype.Service

@Service
class WaterFountainService(
    private val fountainRepository: WaterFountainRepository
) {
    suspend fun showDetails(fountainId: Long) {
        val fountain = fountainRepository.findById(fountainId).orElse(null)
        if (fountain == null) {
            println("Water Fountain not found.")
            return
        }

        println("Water Fountain ID: ${fountain.id}")
        println("Station ID: ${fountain.station.id}")
        println("Created: ${fountain.createdAt}")
        println("Last Updated: ${fountain.updatedAt}")
    }

    suspend fun listFountains() {
        val fountains = fountainRepository.findAll()
        if (fountains.isEmpty()) {
            println("No water fountains found.")
        } else {
            println("All Water Fountains:")
            fountains.forEach {
                println("- ${it.id} (Station ID: ${it.station.id})")
            }
        }
    }

}