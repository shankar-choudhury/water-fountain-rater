package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.DtoMappers.emptyBuilding
import com.kotlinswe.waterfountainrater.dto.DtoMappers.toDto
import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.model.Building
import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import org.springframework.stereotype.Service

@Service
class BuildingService(
    buildingRepository: BuildingRepository
): AbstractBaseService<Building, BuildingSearchResponseDto>(
    buildingRepository,
    ::toDto,
    ::emptyBuilding
)