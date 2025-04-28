package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.WaterStationService
import com.kotlinswe.waterfountainrater.cli.formatter.formatAll
import com.kotlinswe.waterfountainrater.cli.formatter.formatById


import com.kotlinswe.waterfountainrater.cli.util.*
import kotlinx.coroutines.coroutineScope

class StationCommands(
    private val stationService: WaterStationService
) {
    suspend fun listStations(args: String) = coroutineScope {
        listEntities(
            args = args,
            listAll = { stationService.listAll() },
            findById = { buildingId -> stationService.findByBuildingId(buildingId) },
            formatAll = { station -> formatAll(station) },
            formatById = { station -> formatById(station) },
            usageMessage = "Usage: stations [buildingId]"
        )
    }
}