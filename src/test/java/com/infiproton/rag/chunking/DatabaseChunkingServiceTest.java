package com.infiproton.rag.chunking;

import com.infiproton.rag.ingestion.DatabaseIngestionService;
import com.infiproton.rag.model.KnowledgeChunk;
import com.infiproton.rag.model.KnowledgeDocument;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class DatabaseChunkingServiceTest {

    @Autowired
    private DatabaseIngestionService databaseIngestionService;

    @Autowired
    private DatabaseChunkingService databaseChunkingService;

    @Test
    void shouldChunkSupportTickets() {
        List<KnowledgeDocument> documents = databaseIngestionService.loadSupportTickets();
        KnowledgeDocument document = documents.get(0);

        List<KnowledgeChunk> chunks = databaseChunkingService.chunkDocument(document);
        log.info("Total Chunks: {}", chunks.size());
        for (KnowledgeChunk chunk : chunks) {
            log.info("Chunk Content:\n{}", chunk.getContent());
            log.info("Chunk Metadata: {}", chunk.getMetadata());
            log.info("----------------------------------------");

        }
    }
}
