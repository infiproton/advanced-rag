package com.infiproton.rag.service;

import com.infiproton.rag.dto.ChatRequest;
import com.infiproton.rag.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final SelfRagService selfRagService;

    @Cacheable(
            value = "chatResponses",
            key = "T(com.infiproton.rag.security.TenantContext).getTenant() + ':' + #chatRequest.message.trim().toLowerCase()"
    )
    public ChatResponse getResponse(ChatRequest chatRequest, String userId) {
        return selfRagService.generateAnswer(chatRequest.getMessage());
    }
}
