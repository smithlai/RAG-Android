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
        // �򥻱��p�G�p�G�奻���פp��ε��� chunkSize�A������^
        if (text.length <= chunkSize) {
            return listOf(text)
        }
        val separator = remainingSeparators.firstOrNull() ?: ""
        val nextSeparators = remainingSeparators.drop(1)

        // �p�G�S����h���j�šA�������� chunkSize ����
        if (separator.isEmpty()) {
            return text.chunked(chunkSize)
        }

        val splits = text.split(separator)
        val finalChunks = mutableListOf<String>()
        var currentChunk = StringBuilder()
        for (split in splits) {
            // 1. �p�G��e��@���q�w�g�W�L chunkSize
            if (split.length > chunkSize) {
                // ���O�s��e�ֿn�����e
                if (currentChunk.isNotEmpty()) {
                    currentChunk.append(separator)
                    finalChunks.add(currentChunk.toString())
                    currentChunk.clear()
                }
                // ���j�B�z�W�����q
                finalChunks.addAll(splitRecursively(split, nextSeparators))
                continue
            }

            // 2. �p��s����b���]�]�A���j�š^
            val potentialChunk = if (currentChunk.isEmpty()) {
                split
            } else {
                "${currentChunk}$separator$split"
            }
            // �p�G�[�J�s���e��W�L chunkSize
            if (potentialChunk.length > chunkSize) {
                // �O�s��e��
                if (currentChunk.isNotEmpty()) {
                    currentChunk.append(separator)
                    finalChunks.add(currentChunk.toString())
                }
                // �}�l�s����
                currentChunk = StringBuilder(split)
            } else {
                // �p�G�S���W�L�A�N���[���e��
                if (currentChunk.isEmpty()) {
                    currentChunk = StringBuilder(split)
            } else {
                    currentChunk.append(separator).append(split)
                }
            }
        }

        // �B�z�̫�Ѿl�����e
        if (currentChunk.isNotEmpty()) {
            currentChunk.append(separator)
            finalChunks.add(currentChunk.toString())
        }

        return finalChunks
        }

}

