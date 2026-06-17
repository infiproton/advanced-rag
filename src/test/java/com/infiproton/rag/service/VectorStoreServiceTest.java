package com.infiproton.rag.service;

import com.infiproton.rag.chunking.SemanticChunkingService;
import com.infiproton.rag.ingestion.WikiIngestionService;
import com.infiproton.rag.model.KnowledgeChunk;
import com.infiproton.rag.model.KnowledgeDocument;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class VectorStoreServiceTest {
    @Autowired
    private VectorStoreService vectorStoreService;

    @Autowired
    private WikiIngestionService  wikiIngestionService;
    @Autowired private SemanticChunkingService semanticChunkingService;

    @Test
    void shouldStoreSemanticChunksInVectorStore() {
        KnowledgeDocument document = wikiIngestionService.loadDocuments().get(0);

        List<KnowledgeChunk> chunks = semanticChunkingService.chunkDocument(document);

        vectorStoreService.storeChunks(chunks);

    }
}
