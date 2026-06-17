package com.infiproton.rag.service;

import com.infiproton.rag.model.KnowledgeChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class VectorStoreService {

    private final VectorStore vectorStore;

    public void storeChunks(List<KnowledgeChunk> chunks) {

        List<Document> documents = new ArrayList<>();
        for (KnowledgeChunk chunk : chunks) {
            Document document = new Document(
              chunk.getContent(),
              chunk.getMetadata()
            );

            document.getMetadata().put("chunkId", chunk.getChunkId());
            document.getMetadata().put("documentId", chunk.getDocumentId());
            document.getMetadata().put("source",  chunk.getSource());
            document.getMetadata().put("sourceType", chunk.getSourceType());

            documents.add(document);
        }

        vectorStore.add(documents);

    }
}
