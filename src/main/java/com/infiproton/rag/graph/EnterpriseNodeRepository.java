package com.infiproton.rag.graph;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface EnterpriseNodeRepository extends Neo4jRepository<EnterpriseNode, Long> {

    Optional<EnterpriseNode> findByEntityId(String entityId);
}
