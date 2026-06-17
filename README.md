

# Vector Store Config

## SQL Commands

CREATE EXTENSION IF NOT EXISTS vector;


SELECT extname
FROM pg_extension
WHERE extname = 'vector';


CREATE TABLE vector_store (
id UUID PRIMARY KEY,
content TEXT,
metadata JSONB,
embedding VECTOR(1536)
);

CREATE INDEX vector_store_embedding_idx
ON vector_store
USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);

CREATE INDEX vector_store_content_idx
ON vector_store
USING GIN (to_tsvector('english', content));
