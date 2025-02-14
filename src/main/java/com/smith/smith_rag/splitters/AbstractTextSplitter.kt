package com.smith.smith_rag.splitters

import kotlin.math.max

abstract class AbstractTextSplitter(
    protected val chunkSize: Int,
    protected val chunkOverlap: Int
) {
    /**
     * Splits the given text into smaller chunks based on the defined separators.
     *
     * @param text The input text to be split.
     * @return A list of text chunks, each no longer than `chunkSize`.
     */
    abstract fun splitText(text: String): List<String>

    protected fun applyOverlap(chunks: List<String>): List<String> {
        if (chunkOverlap <= 0) return chunks

        val overlappedChunks = mutableListOf<String>()
        for (i in chunks.indices) {
            overlappedChunks.add(chunks[i])
            if (i < chunks.size - 1) {
                val overlapStart = max(0, chunks[i].length - chunkOverlap)
                val overlapText = chunks[i].substring(overlapStart) + chunks[i + 1].take(chunkOverlap)
                overlappedChunks.add(overlapText)
            }
        }
        return overlappedChunks
    }
}