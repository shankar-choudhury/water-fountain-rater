package com.kotlinswe.waterfountainrater.cli.util

import kotlinx.coroutines.coroutineScope

suspend fun <T> listEntities(
    args: String,
    listAll: suspend () -> List<T>,
    findById: suspend (Long) -> List<T>,
    formatAll: (T) -> String,
    formatById: (T) -> String,
    usageMessage: String
) = coroutineScope {
    if (args.isBlank()) {
        listAll().forEach { entity ->
            println(formatAll(entity))
        }
    } else {
        val id = args.toLongOrNull() ?: run {
            println(usageMessage)
            return@coroutineScope
        }
        findById(id).forEach { entity ->
            println(formatById(entity))
        }
    }
}

suspend fun <T> searchEntities(
    query: String,
    searchFunction: suspend (String) -> List<T>,
    formatResult: (T) -> String,
    usageMessage: String,
    emptyMessage: (String) -> String
) = coroutineScope {
    if (query.isBlank()) {
        println(usageMessage)
        return@coroutineScope
    }

    val results = searchFunction(query)
    if (results.isEmpty()) {
        println(emptyMessage(query))
        return@coroutineScope
    }

    println("\n🔍 Search Results:")
    results.forEach { entity ->
        println(formatResult(entity))
    }
}