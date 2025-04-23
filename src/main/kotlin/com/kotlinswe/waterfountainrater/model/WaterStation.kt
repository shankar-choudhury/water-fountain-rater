package com.kotlinswe.waterfountainrater.model

import jakarta.annotation.Nonnull
import jakarta.persistence.*

@Entity
data class WaterStation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column @Nonnull val floor: Int,
    @Column @Nonnull val description: String,
    @OneToMany(mappedBy = "station", cascade = [CascadeType.ALL])
    @Nonnull val fountains: List<WaterFountain> = mutableListOf(),
    @ManyToOne @JoinColumn(name = "building_id")
    val building: Building
)