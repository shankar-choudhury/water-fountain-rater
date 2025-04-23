package com.kotlinswe.waterfountainrater.model

import jakarta.annotation.Nonnull
import jakarta.persistence.*
import java.util.UUID

@Entity
data class WaterFountain(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column @Nonnull val type: FountainType,
    @Column @Nonnull var tasteRating: Double = 0.0,
    @Column @Nonnull var flowRating: Double = 0.0,
    @Column @Nonnull var temperatureRating: Double = 0.0,
    @Column @Nonnull var ambienceRating: Double = 0.0,
    @Column @Nonnull var usabilityRating: Double = 0.0,
    @ManyToOne @JoinColumn(name = "station_id")
    val station: WaterStation
) {
    val overallRating: Double
        get() = (tasteRating + flowRating + temperatureRating + ambienceRating + usabilityRating) / 5

    enum class FountainType {
        UPPER, LOWER, BOTTLE_FILLER
    }
}