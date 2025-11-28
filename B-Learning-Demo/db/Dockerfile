# Database Dockerfile
# PostgreSQL 14 with B-Learning schema

FROM postgres:14-alpine

# Set environment variables
ENV POSTGRES_DB=b_learning
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Copy initialization scripts
# These will be executed in alphabetical order
COPY init/*.sql /docker-entrypoint-initdb.d/

# Install pg_trgm extension (for full-text search)
RUN echo "CREATE EXTENSION IF NOT EXISTS pg_trgm;" > /docker-entrypoint-initdb.d/00-extensions.sql

# Expose PostgreSQL port
EXPOSE 5432

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD pg_isready -U postgres -d b_learning || exit 1

# Use default postgres entrypoint
# It will automatically run all SQL scripts in /docker-entrypoint-initdb.d/
