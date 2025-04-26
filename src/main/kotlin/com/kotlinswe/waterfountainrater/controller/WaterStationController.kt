package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.service.WaterStationService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/water-stations")
class WaterStationController (
    service: WaterStationService
): AbstractBaseController<WaterStationDto>(service)