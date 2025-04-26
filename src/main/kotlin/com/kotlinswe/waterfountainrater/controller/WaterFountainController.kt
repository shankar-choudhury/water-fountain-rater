package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.service.WaterFountainService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/water-fountains")
class WaterFountainController (
    service: WaterFountainService
): AbstractBaseController<WaterFountainDto>(service)