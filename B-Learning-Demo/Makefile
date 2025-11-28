# Makefile for B-Learning Demo
# Quick commands for Docker operations

.PHONY: help build up down restart logs clean test

# Default target
help:
	@echo "B-Learning Demo - Docker Commands"
	@echo ""
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@echo "  build      - Build all Docker images"
	@echo "  up         - Start all services (detached)"
	@echo "  down       - Stop and remove containers"
	@echo "  restart    - Restart all services"
	@echo "  logs       - View logs (follow mode)"
	@echo "  logs-db    - View database logs"
	@echo "  logs-backend - View backend logs"
	@echo "  logs-frontend - View frontend logs"
	@echo "  ps         - Show running containers"
	@echo "  clean      - Remove containers and volumes (DESTRUCTIVE)"
	@echo "  test       - Test all services"
	@echo "  shell-backend - Open backend shell"
	@echo "  shell-frontend - Open frontend shell"
	@echo "  db-shell   - Open PostgreSQL CLI"

# Build all images
build:
	docker-compose build

# Build without cache
build-clean:
	docker-compose build --no-cache

# Start all services
up:
	docker-compose up -d

# Start with build
up-build:
	docker-compose up -d --build

# Stop all services
down:
	docker-compose down

# Stop and remove volumes (DESTRUCTIVE)
down-clean:
	docker-compose down -v

# Restart all services
restart:
	docker-compose restart

# View all logs
logs:
	docker-compose logs -f

# View database logs
logs-db:
	docker-compose logs -f db

# View backend logs
logs-backend:
	docker-compose logs -f backend

# View frontend logs
logs-frontend:
	docker-compose logs -f frontend

# Show container status
ps:
	docker-compose ps

# Stop and remove everything
clean:
	docker-compose down -v
	docker system prune -f

# Test services
test:
	@echo "Testing database..."
	@docker-compose exec -T db psql -U postgres -d b_learning -c "SELECT COUNT(*) as users FROM \"User\";" || echo "❌ DB test failed"
	@echo ""
	@echo "Testing backend..."
	@curl -f http://localhost:8000/health || echo "❌ Backend test failed"
	@echo ""
	@echo "Testing frontend..."
	@curl -f -I http://localhost:3000 || echo "❌ Frontend test failed"
	@echo ""
	@echo "✅ All tests passed!"

# Open backend shell
shell-backend:
	docker-compose exec backend bash

# Open frontend shell
shell-frontend:
	docker-compose exec frontend sh

# Open PostgreSQL CLI
db-shell:
	docker-compose exec db psql -U postgres -d b_learning

# Database backup
db-backup:
	docker-compose exec -T db pg_dump -U postgres b_learning > backup_$(shell date +%Y%m%d_%H%M%S).sql
	@echo "Backup created: backup_$(shell date +%Y%m%d_%H%M%S).sql"

# Database restore (usage: make db-restore FILE=backup.sql)
db-restore:
	@if [ -z "$(FILE)" ]; then \
		echo "Usage: make db-restore FILE=backup.sql"; \
		exit 1; \
	fi
	docker-compose exec -T db psql -U postgres -d b_learning < $(FILE)
	@echo "Database restored from $(FILE)"
