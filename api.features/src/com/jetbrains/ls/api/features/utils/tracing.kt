// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.ls.api.features.utils

import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

/**
 * No-op version of traceProvider that simply passes through the flow without adding telemetry.
 * This replaces the OpenTelemetry-based implementation.
 */
internal fun <T> noOpTraceProvider(
    spanName: String,
    provider: Any,
    resultsFlow: Flow<T>,
): Flow<T> = flow {
    try {
        emitAll(resultsFlow)
    } catch (e: Throwable) {
        if (Logger.shouldRethrow(e)) throw e
        throw e
    }
}

internal suspend fun <T> noOpTraceProvider(
    spanName: String,
    provider: Any,
    block: suspend () -> T,
): T {
    return noOpTraceProvider(
        spanName = spanName,
        provider = provider,
        resultsFlow = flow { emit(block()) },
    ).single()
}
