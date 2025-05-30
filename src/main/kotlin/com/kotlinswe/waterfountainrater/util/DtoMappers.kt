package com.kotlinswe.waterfountainrater.util

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.dto.report.ReportResponseDto
import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.model.*

object DtoMappers {

    fun toDto(entity: WaterStation): WaterStationDto =
        WaterStationDto(
            id = entity.id,
            floor = entity.floor,
            description = entity.description,
            buildingId = entity.building.id,  // Map building ID
            fountains = entity.fountains.map(DtoMappers::toDto)
        )

    fun toDto(entity: WaterFountain): WaterFountainDto =
        WaterFountainDto(
            id = entity.id,
            type = entity.type,
            status = entity.status,
            overallRating = entity.overallRating,
            stationId = entity.station.id  // Map station ID
        )

    fun toDto(entity: Building): BuildingSearchResponseDto =
        BuildingSearchResponseDto(
            id = entity.id,
            name = entity.name,
            latitude = entity.latitude,
            longitude = entity.longitude,
            waterStations = entity.waterStations.map(DtoMappers::toDto)
        )

    fun toDto(report: WaterFountainReport): ReportResponseDto =
        ReportResponseDto(
            id = report.id,
            fountainId = report.waterFountain.id,
            status = report.status,
            reportContents = report.reportContents,
            reportedAt = report.reportedAt
        )

    fun toDto(review: WaterFountainReview): WaterFountainReviewDto =
        WaterFountainReviewDto(
            fountainId = review.waterFountain.id,
            taste = review.tasteRating,
            flow = review.flowRating,
            temperature = review.temperatureRating,
            ambience = review.ambienceRating,
            usability = review.usabilityRating,
            review = review.review
        )

    fun emptyStation() = WaterStationDto(
        id = 0,
        floor = 0,
        description = "Station not found",
        buildingId = -1,
        fountains = emptyList()
    )

    fun emptyFountain() = WaterFountainDto(
        id = 0,
        type = WaterFountain.FountainType.NONE,
        status = WaterFountain.FountainStatus.NONE,
        stationId = -1,
        overallRating = -1.0
    )

    fun emptyBuilding() = BuildingSearchResponseDto(
        id = 0,
        name = "",
        latitude = 0.0,
        longitude = 0.0,
        waterStations = emptyList()
    )
}