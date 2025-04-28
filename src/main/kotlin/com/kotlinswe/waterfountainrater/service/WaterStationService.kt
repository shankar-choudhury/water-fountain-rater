package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.util.DtoMappers.emptyStation
import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import com.kotlinswe.waterfountainrater.util.DtoMappers.toDto
import com.kotlinswe.waterfountainrater.model.WaterStation
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class WaterStationService(
    val stationRepository: WaterStationRepository
): AbstractBaseService<WaterStation, WaterStationDto> (
    stationRepository,
    ::toDto,
    ::emptyStation
) {
    @Transactional
    suspend fun findByBuildingId(buildingId: Long): List<WaterStationDto> {
        return stationRepository.findByBuildingId(buildingId).map { toDto(it) }
    }
}