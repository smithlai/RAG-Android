package com.ml.shubham0204.docqa.domain.readers

import com.smith.smith_rag.readers.DOCXReader
import com.smith.smith_rag.readers.PDFReader
import com.smith.smith_rag.readers.Reader

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
