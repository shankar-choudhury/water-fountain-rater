package com.kotlinswe.waterfountainrater.config

import com.kotlinswe.waterfountainrater.model.Building
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterStation
import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val buildingRepo: BuildingRepository,
    private val stationRepo: WaterStationRepository,
    private val fountainRepo: WaterFountainRepository
) {

    @PostConstruct
    fun init() {
        if (buildingRepo.count() == 0L) {
            // Create buildings
            val scienceBuilding = buildingRepo.save(
                Building(
                    name = "Science Building",
                    latitude = 34.052235,
                    longitude = -118.243683
                )
            )

            val engineeringBuilding = buildingRepo.save(
                Building(
                    name = "Engineering Building",
                    latitude = 34.052245,
                    longitude = -118.243693
                )
            )

            // Create water stations
            val scienceStation1 = stationRepo.save(
                WaterStation(
                    building = scienceBuilding,
                    floor = 1,
                    description = "Near Room 101"
                )
            )

            val engineeringStation1 = stationRepo.save(
                WaterStation(
                    building = engineeringBuilding,
                    floor = 3,
                    description = "Main hallway near elevators"
                )
            )

            // Create water fountains with sample ratings
            fountainRepo.save(
                WaterFountain(
                    station = scienceStation1,
                    type = WaterFountain.FountainType.UPPER,
                    tasteRating = 4.5,
                    flowRating = 3.8,
                    temperatureRating = 4.2,
                    ambienceRating = 3.5,
                    usabilityRating = 4.0
                ))
            fountainRepo.save(
                WaterFountain(
                    station = scienceStation1,
                    type = WaterFountain.FountainType.BOTTLE_FILLER,
                    tasteRating = 4.8,
                    flowRating = 4.5,
                    temperatureRating = 4.5,
                    ambienceRating = 4.2,
                    usabilityRating = 4.7
                ))
            fountainRepo.save(
                WaterFountain(
                    station = engineeringStation1,
                    type = WaterFountain.FountainType.LOWER,
                    tasteRating = 3.2,
                    flowRating = 4.0,
                    temperatureRating = 3.5,
                    ambienceRating = 2.8,
                    usabilityRating = 3.5
                ))

            println("Sample data initialized!")
        }
    }
}