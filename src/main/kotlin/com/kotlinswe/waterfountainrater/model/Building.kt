package com.kotlinswe.waterfountainrater.model

import jakarta.annotation.Nonnull
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
data class Building(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column @Nonnull val name: String,
    @Column @Nonnull val latitude: Double,
    @Column @Nonnull val longitude: Double,
    @OneToMany(mappedBy = "building", cascade = [CascadeType.ALL])
    @Nonnull val waterStations: List<WaterStation> = mutableListOf()
)
