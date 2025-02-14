package com.smith.smith_rag.splitters

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
    private val separators: List<String> = listOf("\n\n", "\n", " ", "")
) : AbstractTextSplitter(chunkSize, chunkOverlap) {

    /**
     * Splits the given text into smaller chunks based on the defined separators.
     *
     * @param text The input text to be split.
     * @return A list of text chunks, each no longer than `chunkSize`.
     */
    override fun splitText(text: String): List<String> {
        return splitRecursively(text, separators)
    }

    /**
     * Recursively splits the text using the given separators.
     * If the text cannot be split further with a separator, it proceeds to the next one.
     *
     * @param text The text to be split.
     * @param remainingSeparators The list of separators to try, in order of priority.
     * @return A list of text chunks that adhere to the `chunkSize` constraint.
     */
    private fun splitRecursively(text: String, remainingSeparators: List<String>): List<String> {
        // If the text is already within the allowed chunk size, return it as a single chunk
        if (text.length <= chunkSize) return listOf(text)

        // Select the first available separator, or use an empty string as a last resort
        val separator = remainingSeparators.firstOrNull() ?: ""

        // If a separator exists, split the text using it; otherwise, chunk it directly
        val splits = if (separator.isNotEmpty()) text.split(separator) else text.chunked(chunkSize)

        val chunks = mutableListOf<String>()
        var currentChunk = ""

        for (part in splits) {
            val newChunk = if (currentChunk.isEmpty()) part else "$currentChunk$separator$part"

            if (newChunk.length <= chunkSize) {
                // Append the part to the current chunk if it remains within size limits
                currentChunk = newChunk
            } else {
                // Store the current chunk and start a new one
                if (currentChunk.isNotEmpty()) chunks.add(currentChunk)
                currentChunk = part
            }
        }

        // Add any remaining text as a final chunk
        if (currentChunk.isNotEmpty()) chunks.add(currentChunk)

        // If only one chunk was created and more separators are available, try again with the next separator
        if (chunks.size == 1 && remainingSeparators.size > 1) {
            return splitRecursively(chunks.first(), remainingSeparators.drop(1))
        }

        // Apply overlap between consecutive chunks before returning the result
        return applyOverlap(chunks)
    }
}

