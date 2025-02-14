package com.smith.smith_rag.splitters

class WhiteSpaceSplitter(
    chunkSize: Int,
    chunkOverlap: Int,
    private val separatorParagraph: String = "\n\n",
    private val separator: String = " "
) : AbstractTextSplitter(chunkSize, chunkOverlap) {
    override fun splitText(text: String): List<String> {
        val textChunks = mutableListOf<String>()
        text.split(separatorParagraph).forEach { paragraph ->
            var currChunk = ""
            val chunks = mutableListOf<String>()
            paragraph.split(separator).forEach { word ->
                val newChunk = if (currChunk.isNotEmpty()) "$currChunk$separator$word" else word
                if (newChunk.length <= chunkSize) {
                    currChunk = newChunk
                } else {
                    if (currChunk.isNotEmpty()) chunks.add(currChunk)
                    currChunk = word
                }
            }
            if (currChunk.isNotEmpty()) chunks.add(currChunk)
            textChunks.addAll(applyOverlap(chunks))
        }
        return textChunks
    }
}