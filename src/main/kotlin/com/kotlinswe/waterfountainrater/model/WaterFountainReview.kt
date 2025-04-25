package com.kotlinswe.waterfountainrater.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class WaterFountainReview(
    @Column(nullable = false)
    val tasteRating: Double,

    @Column(nullable = false)
    val flowRating: Double,

    @Column(nullable = false)
    val temperatureRating: Double,

    @Column(nullable = false)
    val ambienceRating: Double,

    @Column(nullable = false)
    val usabilityRating: Double,

    @Column
    val review: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fountain_id")
    val waterFountain: WaterFountain
) {
    val averageRating: Double
        get() = (tasteRating + flowRating + temperatureRating + ambienceRating + usabilityRating) / 5

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
}