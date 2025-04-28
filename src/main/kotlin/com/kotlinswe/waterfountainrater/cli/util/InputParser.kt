package com.kotlinswe.waterfountainrater.cli.util

fun parseInput(input: String): Pair<String, String> {
    val parts = input.split(" ", limit = 2)
    return if (parts.size > 1) parts[0] to parts[1] else parts[0] to ""
}