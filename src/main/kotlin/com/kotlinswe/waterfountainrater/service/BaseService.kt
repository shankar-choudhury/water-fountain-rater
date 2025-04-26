package com.kotlinswe.waterfountainrater.service

interface BaseService<TDto> {
    suspend fun showDetails(id: Long): TDto
    suspend fun listAll(): List<TDto>
}