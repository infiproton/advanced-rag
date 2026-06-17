package com.infiproton.rag.service;

import com.infiproton.rag.chunking.ChunkingOrchestrator;
import com.infiproton.rag.ingestion.IngestionOrchestrator;
import com.infiproton.rag.model.KnowledgeChunk;
import com.infiproton.rag.model.KnowledgeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexingPipelineService {
    private final IngestionOrchestrator ingestionOrchestrator;
    private final ChunkingOrchestrator chunkingOrchestrator;
    private final VectorStoreService vectorStoreService;

    public void indexAll() {
        List<KnowledgeDocument> documents = ingestionOrchestrator.ingestAll();

        List<KnowledgeChunk> allChunks = new ArrayList<>();
        for (KnowledgeDocument document : documents) {
            allChunks.addAll(chunkingOrchestrator.chunkDocument(document));
        }

        vectorStoreService.storeChunks(allChunks);
    }
}

