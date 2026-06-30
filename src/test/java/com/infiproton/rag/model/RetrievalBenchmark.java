package com.infiproton.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class RetrievalBenchmark {
    private final String query;
    private final Set<String> expectedSources;
}
