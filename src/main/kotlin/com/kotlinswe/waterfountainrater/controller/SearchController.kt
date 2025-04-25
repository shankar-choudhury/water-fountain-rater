package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchRequestDto
import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.service.SearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val searchService: SearchService
) {

    @PostMapping("/buildings")
    suspend fun closestFountainsByBuildings(
        @RequestBody request: BuildingSearchRequestDto
    ): ResponseEntity<List<BuildingSearchResponseDto>> {
        val response = searchService.findNearbyBuildingsWithFountains(
            latitude = request.latitude,
            longitude = request.longitude,
            radius = request.radius
        )
        return ResponseEntity.ok(response)
    }
}