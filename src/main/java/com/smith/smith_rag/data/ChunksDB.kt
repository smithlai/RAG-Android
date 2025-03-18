package com.smith.smith_rag.data

import com.smith.smith_rag.BuildConfig
import org.koin.core.annotation.Single

@Single
class ChunksDB {
    companion object{
        val DefaultRAGPrompt : String =
        "Here is the retrieved context\n" +
        "--------------------------------------------------\n" +
        "\$CONTEXT\n" +
        "--------------------------------------------------\n" +
        "Here is the user\\'s query: \$QUERY"
    }
    private val chunksBox = ObjectBoxStore.store.boxFor(Chunk::class.java)

    fun addChunk(chunk: Chunk) {
        chunksBox.put(chunk)
    }

    fun getSimilarChunks(
        queryEmbedding: FloatArray,
        n: Int = BuildConfig.TOP_K,
    ): List<Pair<Float, Chunk>> {
        /*
        Use maxResultCount to set the maximum number of objects to return by the ANN condition.
        Hint: it can also be used as the "ef" HNSW parameter to increase the search quality in combination
        with a query limit. For example, use maxResultCount of 100 with a Query limit of 10 to have 10 results
        that are of potentially better quality than just passing in 10 for maxResultCount
        (quality/performance tradeoff).
         */
        return chunksBox
            .query(Chunk_.chunkEmbedding.nearestNeighbors(queryEmbedding, 25))
            .build()
            .findWithScores()
            .map { Pair(it.score.toFloat(), it.get()) }
            .subList(0, n)
    }

    fun removeChunks(docId: Long) {
        chunksBox.removeByIds(
            chunksBox
                .query(Chunk_.docId.equal(docId))
                .build()
                .findIds()
                .toList(),
        )
    }
}
