package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.util.DtoMappers.emptyStation
import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import com.kotlinswe.waterfountainrater.util.DtoMappers.toDto
import com.kotlinswe.waterfountainrater.model.WaterStation
import org.springframework.stereotype.Service

@Service
class WaterStationService(
    stationRepository: WaterStationRepository
): AbstractBaseService<WaterStation, WaterStationDto> (
    stationRepository,
    ::toDto,
    ::emptyStation
)