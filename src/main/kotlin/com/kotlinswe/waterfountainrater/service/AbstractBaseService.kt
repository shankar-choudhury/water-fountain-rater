package com.kotlinswe.waterfountainrater.service

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

abstract class AbstractBaseService<TEntity, TDto>(
    private val repository: JpaRepository<TEntity, Long>,
    private val toDto: (TEntity) -> TDto,
    private val emptyDto: () -> TDto
): BaseService<TDto> {
    @Transactional
    override suspend fun showDetails(id: Long): TDto =
        repository.findById(id)
            .map { toDto(it) }
            .orElseGet { emptyDto() }

    @Transactional
    override suspend fun listAll(): List<TDto> =
        repository.findAll().map { toDto(it) }
}