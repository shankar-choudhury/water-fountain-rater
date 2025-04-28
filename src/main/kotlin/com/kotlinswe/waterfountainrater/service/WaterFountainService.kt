package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.util.DtoMappers.emptyFountain
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.util.DtoMappers.toDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class WaterFountainService(
    val fountainRepository: WaterFountainRepository
): AbstractBaseService<WaterFountain, WaterFountainDto> (
    fountainRepository,
    ::toDto,
    ::emptyFountain
) {
    @Transactional
    suspend fun findByStationId(stationId: Long): List<WaterFountainDto> {
        return fountainRepository.findByStationId(stationId).map { toDto(it) }
    }
}