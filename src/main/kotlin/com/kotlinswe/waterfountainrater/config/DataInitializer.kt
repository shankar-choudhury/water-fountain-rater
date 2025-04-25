package com.kotlinswe.waterfountainrater.config

import com.kotlinswe.waterfountainrater.model.*
import com.kotlinswe.waterfountainrater.repository.*
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val buildingRepo: BuildingRepository,
    private val stationRepo: WaterStationRepository,
    private val fountainRepo: WaterFountainRepository,
    private val reviewRepo: WaterFountainReviewRepository,
    private val reportRepo: WaterFountainReportRepository
) {

    @PostConstruct
    fun init() {
        if (buildingRepo.count() == 0L) {
            // --- Buildings ---
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
            val libraryBuilding = buildingRepo.save(
                Building(
                    name = "Library",
                    latitude = 34.053234,
                    longitude = -118.244693
                )
            )
            val artsBuilding = buildingRepo.save(
                Building(
                    name = "Arts Building",
                    latitude = 34.054235,
                    longitude = -118.245683
                )
            )

            // --- Water Stations ---
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
            val libraryStation1 = stationRepo.save(
                WaterStation(
                    building = libraryBuilding,
                    floor = 2,
                    description = "Near Reading Area"
                )
            )
            val artsStation1 = stationRepo.save(
                WaterStation(
                    building = artsBuilding,
                    floor = 1,
                    description = "Near Art Gallery"
                )
            )

            // --- Fountains + Reviews ---
            val fountain1 = WaterFountain(
                station = scienceStation1,
                type = WaterFountain.FountainType.UPPER
            ).apply {
                reviews.add(
                    WaterFountainReview(
                        tasteRating = 4.5,
                        flowRating = 3.8,
                        temperatureRating = 4.2,
                        ambienceRating = 3.5,
                        usabilityRating = 4.0,
                        review = "Tastes pretty clean.",
                        waterFountain = this
                    )
                )
            }

            val fountain2 = WaterFountain(
                station = scienceStation1,
                type = WaterFountain.FountainType.BOTTLE_FILLER
            ).apply {
                reviews.add(
                    WaterFountainReview(
                        tasteRating = 4.8,
                        flowRating = 4.5,
                        temperatureRating = 4.5,
                        ambienceRating = 4.2,
                        usabilityRating = 4.7,
                        review = "Great pressure and easy to use.",
                        waterFountain = this
                    )
                )
            }

            val fountain3 = WaterFountain(
                station = engineeringStation1,
                type = WaterFountain.FountainType.LOWER
            ).apply {
                reviews.add(
                    WaterFountainReview(
                        tasteRating = 3.2,
                        flowRating = 4.0,
                        temperatureRating = 3.5,
                        ambienceRating = 2.8,
                        usabilityRating = 3.5,
                        review = "Decent but a bit hard to reach.",
                        waterFountain = this
                    )
                )
            }

            val fountain4 = WaterFountain(
                station = libraryStation1,
                type = WaterFountain.FountainType.UPPER
            ).apply {
                reviews.add(
                    WaterFountainReview(
                        tasteRating = 4.0,
                        flowRating = 3.5,
                        temperatureRating = 4.0,
                        ambienceRating = 4.5,
                        usabilityRating = 4.0,
                        review = "Nice and clean.",
                        waterFountain = this
                    )
                )
            }

            val fountain5 = WaterFountain(
                station = artsStation1,
                type = WaterFountain.FountainType.BOTTLE_FILLER
            ).apply {
                reviews.add(
                    WaterFountainReview(
                        tasteRating = 4.3,
                        flowRating = 4.7,
                        temperatureRating = 4.5,
                        ambienceRating = 4.0,
                        usabilityRating = 4.8,
                        review = "Very convenient for filling bottles.",
                        waterFountain = this
                    )
                )
            }

            // Persist fountains (and reviews due to CascadeType.ALL)
            fountainRepo.saveAll(listOf(fountain1, fountain2, fountain3, fountain4, fountain5))

            // --- Water Fountain Reports ---
            val report1 = WaterFountainReport(
                waterFountain = fountain1,
                status = WaterFountain.FountainStatus.BROKEN,
                reportContents = "The fountain is leaking from the side.",
            )

            val report2 = WaterFountainReport(
                waterFountain = fountain3,
                status = WaterFountain.FountainStatus.OUT_OF_ORDER,
                reportContents = "The flow is too low to use.",
            )

            val report3 = WaterFountainReport(
                waterFountain = fountain5,
                status = WaterFountain.FountainStatus.BROKEN,
                reportContents = "The bottle filling feature is malfunctioning.",
            )

            // Persist reports
            reportRepo.saveAll(listOf(report1, report2, report3))

            println("✅ Sample data initialized!")
        }
    }
}