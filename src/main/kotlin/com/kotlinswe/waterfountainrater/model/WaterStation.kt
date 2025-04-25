package com.kotlinswe.waterfountainrater.model

import jakarta.annotation.Nonnull
import jakarta.persistence.*

@Entity
data class WaterStation(
    @Column
    val floor: Int,

    @Column
    val description: String,

    @OneToMany(mappedBy = "station", cascade = [CascadeType.ALL], orphanRemoval = true)
    val fountains: List<WaterFountain> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    val building: Building
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}