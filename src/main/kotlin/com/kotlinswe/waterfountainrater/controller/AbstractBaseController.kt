package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.service.BaseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

abstract class AbstractBaseController<TDto> (
    private val service: BaseService<TDto>
) {
    @GetMapping("/{id}")
    open suspend fun getDetails(@PathVariable id: Long): ResponseEntity<TDto> =
        ResponseEntity.ok(service.showDetails(id))

    @GetMapping("/all")
    open suspend fun getAll(): ResponseEntity<List<TDto>> =
        ResponseEntity.ok(service.listAll())
}
