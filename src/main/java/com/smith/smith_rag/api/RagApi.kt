package com.smith.smith_rag.api

import android.content.Context
import android.util.Log
import com.smith.smith_rag.BuildConfig
import com.smith.smith_rag.data.Chunk
import com.smith.smith_rag.data.ChunksDB
import com.smith.smith_rag.data.Document
import com.smith.smith_rag.data.DocumentsDB
import com.smith.smith_rag.data.RetrievedContext
import com.smith.smith_rag.embeddings.SentenceEmbeddingProvider
import com.smith.smith_rag.readers.Readers
import com.smith.smith_rag.splitters.RecursiveCharacterTextSplitter
import com.smith.smith_rag.ui.components.setProgressDialogText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.koin.core.context.GlobalContext
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.min
import kotlin.time.measureTimedValue
@Single
class RagApi (
){
    private val documentsDB: DocumentsDB = GlobalContext.get().get()
    private val chunksDB: ChunksDB = GlobalContext.get().get()
    private val sentenceEncoder: SentenceEmbeddingProvider = GlobalContext.get().get()
    suspend fun addDocument(
        inputStream: InputStream,
        fileName: String,
        documentType: Readers.DocumentType,
    ) = withContext(Dispatchers.IO) {
        val text =
            Readers.getReaderForDocType(documentType).readFromInputStream(inputStream)
                ?: return@withContext
        val newDocId =
            documentsDB.addDocument(
                Document(
                    docText = text,
                    docFileName = fileName,
                    docAddedTime = System.currentTimeMillis(),
                ),
            )
        setProgressDialogText("Creating chunks...")
        val textSplitter = RecursiveCharacterTextSplitter(chunkSize = BuildConfig.CHUNKSIZE, chunkOverlap = BuildConfig.CHUNKOVERLAP) //WhiteSpaceSplitter(chunkSize = 500, chunkOverlap = 50)
        val chunks = textSplitter.splitText(text)
        setProgressDialogText("Adding chunks to database...")
        val size = chunks.size
        chunks.forEachIndexed { index, s ->
            setProgressDialogText("Added ${index + 1}/$size chunk(s) to database...")
            val embedding = sentenceEncoder.encodeText(s)
            chunksDB.addChunk(
                Chunk(
                    docId = newDocId,
                    docFileName = fileName,
                    chunkData = s,
                    chunkEmbedding = embedding,
                ),
            )
        }
    }

    suspend fun addDocumentFromUrl(
        url: String,
        context: Context,
        onDownloadComplete: (Boolean) -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val fileName = getFileNameFromURL(url)
                val file = File(context.cacheDir, fileName)

                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // Determine the document type based on the file extension
                // Add handle for unknown types if supported
                val documentType =
                    when (fileName.substringAfterLast(".", "").lowercase()) {
                        "pdf" -> Readers.DocumentType.PDF
                        "docx" -> Readers.DocumentType.MS_DOCX
                        "doc" -> Readers.DocumentType.MS_DOCX
                        else -> Readers.DocumentType.UNKNOWN
                    }

                // Pass file to your document handling logic
                addDocument(file.inputStream(), fileName, documentType)

                withContext(Dispatchers.Main) {
                    onDownloadComplete(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onDownloadComplete(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onDownloadComplete(false)
            }
        }
    }

    fun getAllDocuments(): Flow<List<Document>> = documentsDB.getAllDocuments()

    fun removeDocument(docId: Long) {
        documentsDB.removeDocument(docId)
        chunksDB.removeChunks(docId)
    }

    fun getDocsCount(): Long = documentsDB.getDocsCount()

    // Extracts the file name from the URL
    // Source: https://stackoverflow.com/a/11576046/13546426
    private fun getFileNameFromURL(url: String?): String {
        if (url == null) {
            return ""
        }
        try {
            val resource = URL(url)
            val host = resource.host
            if (host.isNotEmpty() && url.endsWith(host)) {
                return ""
            }
        } catch (e: MalformedURLException) {
            return ""
        }
        val startIndex = url.lastIndexOf('/') + 1
        val length = url.length
        var lastQMPos = url.lastIndexOf('?')
        if (lastQMPos == -1) {
            lastQMPos = length
        }
        var lastHashPos = url.lastIndexOf('#')
        if (lastHashPos == -1) {
            lastHashPos = length
        }
        val endIndex = min(lastQMPos.toDouble(), lastHashPos.toDouble()).toInt()
        return url.substring(startIndex, endIndex)
    }


    suspend fun search(query: String): String = withContext(Dispatchers.Main) {
        Log.d("RagSearchTool","RAG Search: ${query}")
        val retrieveDuration = measureTimedValue {
            var jointContext = ""
            val retrievedContextList = ArrayList<RetrievedContext>()
            val queryEmbedding = sentenceEncoder.encodeText(query)
            chunksDB.getSimilarChunks(queryEmbedding)
                .forEach {
                    jointContext += "\n" + it.second.chunkData
                    retrievedContextList.add(
                        RetrievedContext(
                            it.second.docFileName,
                            it.second.chunkData
                        )
                    )
                }
            jointContext
        }
        return@withContext retrieveDuration.value
    }
}