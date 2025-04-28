package com.kotlinswe.waterfountainrater.cli

import com.kotlinswe.waterfountainrater.cli.command.*
import com.kotlinswe.waterfountainrater.service.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CliConfig {
    @Bean
    fun buildingCommands(buildingService: BuildingService) = BuildingCommands(buildingService)

    @Bean
    fun stationCommands(stationService: WaterStationService) = StationCommands(stationService)

    @Bean
    fun fountainCommands(
        fountainService: WaterFountainService,
        reviewService: ReviewService
    ) = FountainCommands(fountainService, reviewService)

    @Bean
    fun reviewCommands( reviewService: ReviewService) = ReviewCommands(reviewService)

    @Bean
    fun reportCommands(reportService: ReportService) = ReportCommands(reportService)

    @Bean
    fun searchCommands(searchService: SearchService) = SearchCommands(searchService)

    @Bean
    fun statsCommands(reviewService: ReviewService) = StatsCommands(reviewService)
}