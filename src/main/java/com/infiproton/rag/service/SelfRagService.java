package com.infiproton.rag.service;

import com.infiproton.rag.dto.ChatResponse;
import com.infiproton.rag.dto.RetrievalRequest;
import com.infiproton.rag.model.RetrievalResult;
import com.infiproton.rag.query.QueryRewritingService;
import com.infiproton.rag.retrieval.RetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SelfRagService {
    private final RetrievalService retrievalService;
    private final QueryRewritingService  queryRewritingService;
    private final PromptOrchestrationService promptOrchestrationService;
    private final AnswerEvaluationService  answerEvaluationService;
    private final ChatClient chatClient;


    public ChatResponse generateAnswer(String query) {
        log.info("SELF-RAG: Initial retrieval started");

        RetrievalRequest retrievalRequest = new RetrievalRequest();
        retrievalRequest.setQuery(query);

        List<RetrievalResult> retrievalResults = retrievalService.retrieve(retrievalRequest);

        String answer = generateAnswerFromResults(query, retrievalResults);
        log.info("ANSWER: {}", answer);

        boolean weakAnswer = answerEvaluationService.isWeakAnswer(answer, retrievalResults);

        if (!weakAnswer) {
            log.info("SELF-RAG: Initial answer accepted");
            List<String> sources = retrievalResults.stream()
                    .map(result ->
                            (String) result.getMetadata().get("source")
                    )
                    .distinct()
                    .toList();
            return new ChatResponse(answer, sources);
        }

        log.info("SELF-RAG: Weak answer detected. Retrying retrieval.");

        String retryQuery = queryRewritingService.rewrite(query);
        log.info("SELF-RAG: Rewritten query: {}", retryQuery);

        RetrievalRequest retryRequest = new RetrievalRequest();

        retryRequest.setQuery(retryQuery);

        List<RetrievalResult> retryResults = retrievalService.retrieve(retryRequest);

        String improvedAnswer = generateAnswerFromResults(query, retryResults);
        log.info("IMPROVED ANSWER: {}", improvedAnswer);
        List<String> sources = retryResults.stream()
                .map(result ->
                        (String) result.getMetadata().get("source")
                )
                .distinct()
                .toList();

        return new ChatResponse(improvedAnswer, sources);
    }

    private String generateAnswerFromResults(String query, List<RetrievalResult> retrievalResults) {
        String prompt = promptOrchestrationService.buildPrompt(query, retrievalResults);

        return chatClient.prompt().user(prompt).call().content();
    }
}












