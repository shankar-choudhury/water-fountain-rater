package com.kotlinswe.waterfountainrater.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class WaterFountainReport(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fountain_id")
    val waterFountain: WaterFountain,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: WaterFountain.FountainStatus,

    @Column
    val reportContents: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @Column
    val reportedAt: Instant = Instant.now()
}