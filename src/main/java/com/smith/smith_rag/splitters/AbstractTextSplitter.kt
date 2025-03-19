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

    protected fun getLastTokensWithinLimit(tokens: List<String>, chunkOverlap: Int): List<String> {
        val result = mutableListOf<String>()
        var totalLength = 0

        for (i in tokens.indices.reversed()) { // 反向遍歷
            val token = tokens[i]
            if (totalLength + token.length > chunkOverlap) break // 超過限制則停止
            result.add(0, token) // 保持原本順序
            totalLength += token.length
        }

        return result
    }

    protected fun applyOverlaps(chunks: List<String>, separator: String=" "): List<String> {
        if (chunks.isEmpty() || chunkOverlap <= 0) return chunks

        val result = mutableListOf<String>()
        var previousChunk = chunks[0]
        result.add(previousChunk)
        for (i in 1 until chunks.size) {
            val currentChunk = chunks[i]

            // 找出前一個 chunk 的結尾部分
            val previous_chunks = previousChunk.split(separator)

            val overlapFromPrevious = getLastTokensWithinLimit(previous_chunks, chunkOverlap)

            // 將重疊部分加到當前 chunk 的開頭
            val overlappedChunk = overlapFromPrevious.joinToString(separator) + currentChunk

            result.add(overlappedChunk)
            previousChunk = currentChunk
        }
        return result
    }
    protected fun applyOverlap(previous:String, current:String,separator: String = " "): String {
        if (previous.isEmpty() || chunkOverlap <= 0) return current
        val overlapFromPrevious = previous.takeLast(minOf(chunkOverlap, previous.length))
        val overlappedChunk = overlapFromPrevious + separator +current
        return overlappedChunk

    }
}