package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.DtoMappers.emptyFountain
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.dto.DtoMappers.toDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import org.springframework.stereotype.Service

@Service
class WaterFountainService(
    fountainRepository: WaterFountainRepository
): AbstractBaseService<WaterFountain, WaterFountainDto> (
    fountainRepository,
    ::toDto,
    ::emptyFountain
)