package com.kotlinswe.waterfountainrater.service

import org.springframework.data.jpa.repository.JpaRepository

abstract class AbstractBaseService<TEntity, TDto>(
    private val repository: JpaRepository<TEntity, Long>,
    private val toDto: (TEntity) -> TDto,
    private val emptyDto: () -> TDto
): BaseService<TDto> {
    override suspend fun showDetails(id: Long): TDto =
        repository.findById(id)
            .map { toDto(it) }
            .orElseGet { emptyDto() }

    override suspend fun listAll(): List<TDto> =
        repository.findAll().map { toDto(it) }
}