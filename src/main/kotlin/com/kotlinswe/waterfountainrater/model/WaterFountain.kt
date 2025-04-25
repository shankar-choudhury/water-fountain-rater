package com.kotlinswe.waterfountainrater.model

import jakarta.annotation.Nonnull
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
data class WaterFountain(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: FountainType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: FountainStatus = FountainStatus.WORKING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    val station: WaterStation,

    @OneToMany(mappedBy = "waterFountain", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reviews: MutableList<WaterFountainReview> = mutableListOf(),

    @OneToMany(mappedBy = "waterFountain", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reports: MutableList<WaterFountainReport> = mutableListOf()
) {
    enum class FountainType {
        UPPER, LOWER, BOTTLE_FILLER
    }

    enum class FountainStatus {
        WORKING, BROKEN, UNDER_REPAIR, OUT_OF_ORDER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()

    @Column
    val updatedAt: Instant? = null

    val overallRating: Double
        get() = if (reviews.isEmpty()) 0.0 else
            reviews.map { it.averageRating }.average()
}