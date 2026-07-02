package com.infiproton.rag.controller;

import com.infiproton.rag.audit.AuditLogEntity;
import com.infiproton.rag.audit.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuditController {
    private final AuditLogRepository auditLogRepository;

    @GetMapping("/audit-logs")
    public List<AuditLogEntity> getLogs() {
        return auditLogRepository.findAll();
    }
}
