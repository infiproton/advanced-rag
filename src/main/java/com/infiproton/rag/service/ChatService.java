package com.infiproton.rag.service;

import com.infiproton.rag.audit.AuditService;
import com.infiproton.rag.dto.ChatRequest;
import com.infiproton.rag.dto.ChatResponse;
import com.infiproton.rag.dto.RetrievalRequest;
import com.infiproton.rag.model.RetrievalResult;
import com.infiproton.rag.retrieval.HybridSearchService;
import com.infiproton.rag.security.TenantContext;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatClient chatClient;
    private final HybridSearchService hybridSearchService;
    private final PromptOrchestrationService promptOrchestrationService;
    private final MeterRegistry meterRegistry;
    private final AuditService auditService;

    public ChatResponse getResponse(ChatRequest chatRequest, String userId) {

        RetrievalRequest retrievalRequest = new RetrievalRequest();
        retrievalRequest.setQuery(chatRequest.getMessage());

        Timer.Sample retrievalSample = Timer.start(meterRegistry);
        List<RetrievalResult> results = hybridSearchService.search(retrievalRequest);
        retrievalSample.stop(meterRegistry.timer("rag.retrieval.latency"));
        meterRegistry.counter("rag.retrieval.requests").increment();

        String prompt = promptOrchestrationService.buildPrompt(chatRequest.getMessage(), results);
        log.info("PROMPT: \n{}", prompt);
        meterRegistry.summary("rag.prompt.size").record(prompt.length());

        Timer.Sample generationSample = Timer.start(meterRegistry);
        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        generationSample.stop(meterRegistry.timer("rag.generation.latency"));

        List<String> sources = results.stream()
                .map(result -> (String) result.getMetadata().get("source"))
                .distinct()
                .toList();

        auditService.logQuery(userId, TenantContext.getTenant(), chatRequest.getMessage(),
                sources, true);
        return new ChatResponse(aiResponse, sources);
    }
}
