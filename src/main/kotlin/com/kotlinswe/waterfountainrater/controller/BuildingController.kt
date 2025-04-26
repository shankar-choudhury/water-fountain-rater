package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.service.BuildingService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/buildings")
class BuildingController (
    service: BuildingService
): AbstractBaseController<BuildingSearchResponseDto>(service)