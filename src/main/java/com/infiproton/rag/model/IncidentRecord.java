package com.infiproton.rag.model;

public record IncidentRecord(String incidentId,
                             String title,
                             String severity,
                             String affectedServices) {
}
