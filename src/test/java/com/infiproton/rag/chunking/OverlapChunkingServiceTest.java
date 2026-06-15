package com.infiproton.rag.chunking;

import com.infiproton.rag.ingestion.PdfIngestionService;
import com.infiproton.rag.model.KnowledgeChunk;
import com.infiproton.rag.model.KnowledgeDocument;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class OverlapChunkingServiceTest {

    @Autowired
    private OverlapChunkingService overlapChunkingService;

    @Autowired
    private PdfIngestionService pdfIngestionService;

    @Test
    void shouldChunkPdfDocument() {

        List<KnowledgeDocument> documents = pdfIngestionService.loadPolicyDocuments();
        KnowledgeDocument document = documents.get(0);

        List<KnowledgeChunk> chunks = overlapChunkingService.chunkDocument(document);
        log.info("Total Chunks: {}", chunks.size());
        for (KnowledgeChunk chunk : chunks) {
            log.info("Chunk Index: {}", chunk.getChunkIndex());
            log.info("Chunk Length: {}", chunk.getContent().length());
            log.info("Chunk Content:\n{}", chunk.getContent());
            log.info("----------------------------------------");

        }

    }
}
