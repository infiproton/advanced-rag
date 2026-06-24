package com.infiproton.rag.service;

import com.infiproton.rag.dto.RetrievalRequest;
import com.infiproton.rag.model.RetrievalResult;
import com.infiproton.rag.retrieval.HybridSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class PromptOrchestrationServiceTest {

    @Autowired
    private HybridSearchService hybridSearchService;
    @Autowired
    private PromptOrchestrationService promptOrchestrationService;

    @Test
    void shouldBuildGroundedPrompt() {
        RetrievalRequest request = new RetrievalRequest();
        request.setQuery("Why are users getting AUTH-403 errors?");

        List<RetrievalResult> results = hybridSearchService.search(request);
        String prompt = promptOrchestrationService.buildPrompt(request.getQuery(), results);

        log.info("PROMPT:\n{}", prompt);
    }
}
