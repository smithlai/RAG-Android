package com.smith.smith_rag.splitters

import android.util.Log

/**
 * A text splitter that recursively splits text into smaller chunks based on a list of separators.
 * It first attempts to split using the largest separator and falls back to smaller ones if necessary.
 *
 * @param chunkSize The maximum allowed size of a chunk.
 * @param chunkOverlap The number of overlapping characters between consecutive chunks.
 * @param separators A list of separators used for splitting, ordered from highest to lowest priority.
 */
class RecursiveCharacterTextSplitter(
    chunkSize: Int,
    chunkOverlap: Int,
    private val separators: List<String> = mutableListOf("\n\n\n","\n\n", "\n", " ")
) : AbstractTextSplitter(chunkSize, chunkOverlap) {

    /**
     * Splits the given text into smaller chunks based on the defined separators.
     *
     * @param text The input text to be split.
     * @return A list of text chunks, each no longer than `chunkSize`.
     */
    override fun splitText(text: String): List<String> {
        val chunks = splitRecursively(text, separators)
        return applyOverlaps(chunks)
    }

    /**
     * Recursively splits the text using the given separators.
     * If the text cannot be split further with a separator, it proceeds to the next one.
     * Note: Each chunk retains its original tail separator
     *
     * @param text The text to be split.
     * @param remainingSeparators The list of separators to try, in order of priority.
     * @return A list of text chunks that adhere to the `chunkSize` constraint.
     */
    private fun splitRecursively(text: String, remainingSeparators: List<String>): List<String> {
        // 基本情況：如果文本長度小於或等於 chunkSize，直接返回
        if (text.length <= chunkSize) {
            return listOf(text)
        }
        val separator = remainingSeparators.firstOrNull() ?: ""
        val nextSeparators = remainingSeparators.drop(1)

        // 如果沒有更多分隔符，直接按照 chunkSize 切割
        if (separator.isEmpty()) {
            return text.chunked(chunkSize)
        }

        val splits = text.split(separator)
        val finalChunks = mutableListOf<String>()
        var currentChunk = StringBuilder()
        for (split in splits) {
            // 1. 如果當前單一片段已經超過 chunkSize
            if (split.length > chunkSize) {
                // 先保存當前累積的內容
                if (currentChunk.isNotEmpty()) {
                    currentChunk.append(separator)
                    finalChunks.add(currentChunk.toString())
                    currentChunk.clear()
                }
                // 遞迴處理超長片段
                finalChunks.addAll(splitRecursively(split, nextSeparators))
                continue
            }

            // 2. 計算新的潛在塊（包括分隔符）
            val potentialChunk = if (currentChunk.isEmpty()) {
                split
            } else {
                "${currentChunk}$separator$split"
            }
            // 如果加入新內容後超過 chunkSize
            if (potentialChunk.length > chunkSize) {
                // 保存當前塊
                if (currentChunk.isNotEmpty()) {
                    currentChunk.append(separator)
                    finalChunks.add(currentChunk.toString())
                }
                // 開始新的塊
                currentChunk = StringBuilder(split)
            } else {
                // 如果沒有超過，就附加到當前塊
                if (currentChunk.isEmpty()) {
                    currentChunk = StringBuilder(split)
                } else {
                    currentChunk.append(separator).append(split)
                }
            }
        }

        // 處理最後剩餘的內容
        if (currentChunk.isNotEmpty()) {
            currentChunk.append(separator)
            finalChunks.add(currentChunk.toString())
        }

        return finalChunks
    }

}

