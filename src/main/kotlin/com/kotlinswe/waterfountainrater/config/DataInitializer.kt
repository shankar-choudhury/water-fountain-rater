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
            val adminBuilding = buildingRepo.save(
                Building(
                    name = "Administration Building",
                    latitude = 34.051000,
                    longitude = -118.242000
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
            val scienceStation2 = stationRepo.save(
                WaterStation(
                    building = scienceBuilding,
                    floor = 2,
                    description = "Chemistry Wing"
                )
            )
            val engineeringStation1 = stationRepo.save(
                WaterStation(
                    building = engineeringBuilding,
                    floor = 3,
                    description = "Main hallway near elevators"
                )
            )
            val engineeringStation2 = stationRepo.save(
                WaterStation(
                    building = engineeringBuilding,
                    floor = 1,
                    description = "Computer Lab Area"
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
            val adminStation1 = stationRepo.save(
                WaterStation(
                    building = adminBuilding,
                    floor = 1,
                    description = "Main Lobby"
                )
            )

            // --- Fountains ---
            val fountains = listOf(
                // Science Building Fountains
                WaterFountain(
                    station = scienceStation1,
                    type = WaterFountain.FountainType.UPPER
                ).apply {
                    reviews.addAll(listOf(
                        WaterFountainReview(
                            tasteRating = 4.5,
                            flowRating = 3.8,
                            temperatureRating = 4.2,
                            ambienceRating = 3.5,
                            usabilityRating = 4.0,
                            review = "Tastes pretty clean.",
                            waterFountain = this
                        ),
                        WaterFountainReview(
                            tasteRating = 3.0,
                            flowRating = 4.2,
                            temperatureRating = 3.8,
                            ambienceRating = 2.5,
                            usabilityRating = 3.0,
                            review = "Could be colder.",
                            waterFountain = this
                        )
                    ))
                },
                WaterFountain(
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
                },
                WaterFountain(
                    station = scienceStation2,
                    type = WaterFountain.FountainType.LOWER,
                    status = WaterFountain.FountainStatus.BROKEN
                ),

                // Engineering Building Fountains
                WaterFountain(
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
                },
                WaterFountain(
                    station = engineeringStation2,
                    type = WaterFountain.FountainType.BOTTLE_FILLER,
                    status = WaterFountain.FountainStatus.UNDER_REPAIR
                ),

                // Library Fountains
                WaterFountain(
                    station = libraryStation1,
                    type = WaterFountain.FountainType.UPPER
                ).apply {
                    reviews.addAll(listOf(
                        WaterFountainReview(
                            tasteRating = 4.0,
                            flowRating = 3.5,
                            temperatureRating = 4.0,
                            ambienceRating = 4.5,
                            usabilityRating = 4.0,
                            review = "Nice and clean.",
                            waterFountain = this
                        ),
                        WaterFountainReview(
                            tasteRating = 5.0,
                            flowRating = 4.5,
                            temperatureRating = 4.8,
                            ambienceRating = 4.0,
                            usabilityRating = 4.5,
                            review = "Perfect study break spot!",
                            waterFountain = this
                        )
                    ))
                },

                // Arts Building Fountains
                WaterFountain(
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
                },

                // Admin Building Fountains (no reviews - for testing)
                WaterFountain(
                    station = adminStation1,
                    type = WaterFountain.FountainType.UPPER,
                    status = WaterFountain.FountainStatus.OUT_OF_ORDER
                )
            )

            // Persist fountains (and reviews due to CascadeType.ALL)
            fountainRepo.saveAll(fountains)

            // --- Water Fountain Reports ---
            val reports = listOf(
                WaterFountainReport(
                    waterFountain = fountains[0], // Science upper fountain
                    status = WaterFountain.FountainStatus.BROKEN,
                    reportContents = "The fountain is leaking from the side."
                ),
                WaterFountainReport(
                    waterFountain = fountains[2], // Science lower fountain
                    status = WaterFountain.FountainStatus.BROKEN,
                    reportContents = "No water coming out at all."
                ),
                WaterFountainReport(
                    waterFountain = fountains[3], // Engineering lower fountain
                    status = WaterFountain.FountainStatus.OUT_OF_ORDER,
                    reportContents = "The flow is too low to use."
                ),
                WaterFountainReport(
                    waterFountain = fountains[7], // Admin fountain
                    status = WaterFountain.FountainStatus.OUT_OF_ORDER,
                    reportContents = "Display shows error code E5."
                ),
                WaterFountainReport(
                    waterFountain = fountains[5], // Library fountain
                    status = WaterFountain.FountainStatus.WORKING,
                    reportContents = "Minor drip from nozzle."
                )
            )

            // Persist reports
            reportRepo.saveAll(reports)

            println("✅ Comprehensive sample data initialized!")
            println("   Buildings: ${buildingRepo.count()}")
            println("   Stations: ${stationRepo.count()}")
            println("   Fountains: ${fountainRepo.count()}")
            println("   Reviews: ${reviewRepo.count()}")
            println("   Reports: ${reportRepo.count()}")
        }
    }
}