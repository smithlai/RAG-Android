package com.smith.smith_rag.readers

class Readers {

    enum class DocumentType {
        PDF,
        MS_DOCX,
        UNKNOWN
    }

    companion object {

        fun getReaderForDocType(docType: DocumentType): Reader {
            return when (docType) {
                DocumentType.PDF -> PDFReader()
                DocumentType.MS_DOCX -> DOCXReader()
                DocumentType.UNKNOWN -> throw IllegalArgumentException("Unsupported document type.")
            }
        }
    }
}
