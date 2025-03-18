package com.smith.smith_rag.splitters

class WhiteSpaceSplitter(
    chunkSize: Int,
    chunkOverlap: Int,
//    private val separatorParagraph: String = "\n\n",
//    private val separator: String = " "
) : AbstractTextSplitter(chunkSize, chunkOverlap) {
    override fun splitText(text: String): List<String> {
        val splitter = RecursiveCharacterTextSplitter(
            chunkSize,
            chunkOverlap,
            separators = listOf("\n\n", " ")
        )
        return splitter.splitText(text)
    }
}