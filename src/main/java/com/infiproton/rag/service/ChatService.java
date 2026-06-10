package com.infiproton.rag.service;

import com.infiproton.rag.dto.ChatRequest;
import com.infiproton.rag.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    public ChatResponse getResponse(ChatRequest chatRequest) {

        String aiResponse = chatClient.prompt()
                .user(chatRequest.getMessage())
                .call()
                .content();

        return new ChatResponse(aiResponse);
    }
}
