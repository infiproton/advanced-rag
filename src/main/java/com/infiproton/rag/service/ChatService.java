package com.infiproton.rag.service;

import com.infiproton.rag.dto.ChatRequest;
import com.infiproton.rag.dto.ChatResponse;
import com.infiproton.rag.dto.RetrievalRequest;
import com.infiproton.rag.model.RetrievalResult;
import com.infiproton.rag.retrieval.HybridSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final HybridSearchService hybridSearchService;
    private final PromptOrchestrationService promptOrchestrationService;

    public ChatResponse getResponse(ChatRequest chatRequest) {

        RetrievalRequest retrievalRequest = new RetrievalRequest();
        retrievalRequest.setQuery(chatRequest.getMessage());
        List<RetrievalResult> results = hybridSearchService.search(retrievalRequest);

        String prompt = promptOrchestrationService.buildPrompt(chatRequest.getMessage(), results);

        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return new ChatResponse(aiResponse);
    }
}
