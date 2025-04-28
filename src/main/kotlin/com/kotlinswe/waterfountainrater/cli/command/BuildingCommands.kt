package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.BuildingService
import kotlinx.coroutines.coroutineScope

class BuildingCommands(private val buildingService: BuildingService) {
    suspend fun listBuildings(args: String) = coroutineScope {
        buildingService.listAll().forEach { println("🏢 ${it.name} (ID: ${it.id})") }
    }
}