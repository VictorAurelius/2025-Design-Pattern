# 🐳 Docker Deployment Guide

Hướng dẫn deploy B-Learning Demo bằng Docker Compose.

## 📋 Yêu cầu

- **Docker**: 20.10+ ([Download](https://docs.docker.com/get-docker/))
- **Docker Compose**: 2.0+ (đi kèm với Docker Desktop)
- **RAM**: Ít nhất 4GB free
- **Disk**: Ít nhất 2GB free

Kiểm tra:
```bash
docker --version
docker-compose --version
```

---

## 🚀 Quick Start (1 phút)

### 1️⃣ Build và Run

```bash
# Clone hoặc cd vào project
cd B-Learning-Demo

# Build và start tất cả services
docker-compose up -d --build

# Xem logs
docker-compose logs -f
```

### 2️⃣ Truy cập

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8000
- **API Docs**: http://localhost:8000/docs
- **Database**: localhost:5432 (postgres/postgres)

### 3️⃣ Stop

```bash
# Stop services (giữ data)
docker-compose stop

# Stop và xóa containers (giữ volumes)
docker-compose down

# Stop, xóa containers VÀ volumes (mất data!)
docker-compose down -v
```

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│                   Docker Host                    │
│                                                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────┐ │
│  │  Frontend   │  │   Backend   │  │   DB    │ │
│  │  (Next.js)  │→→│  (FastAPI)  │→→│(Postgres)│ │
│  │  Port 3000  │  │  Port 8000  │  │Port 5432│ │
│  └─────────────┘  └─────────────┘  └─────────┘ │
│         ↓                ↓                ↓      │
│  ┌──────────────────────────────────────────┐  │
│  │       blearning-network (bridge)         │  │
│  └──────────────────────────────────────────┘  │
│                       ↓                          │
│               ┌──────────────┐                  │
│               │ postgres_data│                  │
│               │   (volume)   │                  │
│               └──────────────┘                  │
└─────────────────────────────────────────────────┘
```

---

## 📦 Services

### 1. Database (`blearning-db`)
- **Image**: Custom PostgreSQL 14 Alpine
- **Port**: 5432
- **Database**: b_learning
- **User**: postgres / postgres
- **Init**: Tự động chạy SQL từ `db/init/`:
  - `01-schema.sql` - Tables, constraints
  - `02-indexes.sql` - Indexes
  - `03-constraints.sql` - Foreign keys
  - `04-seed-data.sql` - Sample data
- **Volume**: `postgres_data` (persist data)

### 2. Backend (`blearning-backend`)
- **Image**: Python 3.11 Slim
- **Port**: 8000
- **Dependencies**: FastAPI, psycopg2, pydantic
- **Health check**: GET /health
- **Restart**: unless-stopped

### 3. Frontend (`blearning-frontend`)
- **Image**: Node 18 Alpine (multi-stage build)
- **Port**: 3000
- **Build**: Next.js standalone output
- **Health check**: HTTP GET /
- **Restart**: unless-stopped

---

## 🔧 Commands

### Basic Operations

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs
docker-compose logs -f                    # Follow
docker-compose logs -f backend           # Specific service

# Check status
docker-compose ps

# Stop services
docker-compose stop

# Restart services
docker-compose restart

# Stop and remove
docker-compose down
```

### Build Commands

```bash
# Build all images
docker-compose build

# Build specific service
docker-compose build backend

# Build without cache (clean build)
docker-compose build --no-cache

# Pull latest base images
docker-compose pull
```

### Database Operations

```bash
# Access PostgreSQL CLI
docker-compose exec db psql -U postgres -d b_learning

# Run SQL script
docker-compose exec -T db psql -U postgres -d b_learning < script.sql

# Backup database
docker-compose exec -T db pg_dump -U postgres b_learning > backup.sql

# Restore database
docker-compose exec -T db psql -U postgres -d b_learning < backup.sql

# View database logs
docker-compose logs db
```

### Debugging

```bash
# Execute command in container
docker-compose exec backend bash
docker-compose exec frontend sh

# View container details
docker inspect blearning-backend

# View resource usage
docker stats

# Remove unused images/volumes
docker system prune -a
docker volume prune
```

---

## 🔍 Troubleshooting

### ❌ Port already in use

```bash
# Check what's using the port
# Linux/Mac:
lsof -i :3000
lsof -i :8000
lsof -i :5432

# Windows:
netstat -ano | findstr :3000

# Change ports in docker-compose.yml:
ports:
  - "3001:3000"  # Host:Container
```

### ❌ Database init fails

```bash
# View init logs
docker-compose logs db

# Remove volume and recreate
docker-compose down -v
docker-compose up -d --build

# Check SQL files
ls -la db/init/
```

### ❌ Backend can't connect to DB

```bash
# Check if DB is healthy
docker-compose ps
docker-compose exec db pg_isready -U postgres

# Check backend logs
docker-compose logs backend

# Verify environment variables
docker-compose exec backend env | grep DB_
```

### ❌ Frontend can't reach backend

```bash
# Check network
docker network ls
docker network inspect blearning-demo_blearning-network

# Test from frontend container
docker-compose exec frontend wget -O- http://backend:8000/health

# Check CORS in backend
docker-compose logs backend | grep CORS
```

### ❌ Container keeps restarting

```bash
# View logs
docker-compose logs -f <service-name>

# Check health status
docker-compose ps

# Disable restart to debug
# In docker-compose.yml: remove "restart: unless-stopped"
```

---

## 🔐 Production Considerations

### 1. Change default passwords

```yaml
# docker-compose.yml
environment:
  POSTGRES_PASSWORD: your_strong_password_here
  DB_PASSWORD: your_strong_password_here
```

### 2. Use secrets

```yaml
# docker-compose.yml
services:
  db:
    secrets:
      - db_password
    environment:
      POSTGRES_PASSWORD_FILE: /run/secrets/db_password

secrets:
  db_password:
    file: ./secrets/db_password.txt
```

### 3. Limit resources

```yaml
# docker-compose.yml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 512M
        reservations:
          cpus: '0.5'
          memory: 256M
```

### 4. Use nginx reverse proxy

```yaml
# Add nginx service
nginx:
  image: nginx:alpine
  ports:
    - "80:80"
    - "443:443"
  volumes:
    - ./nginx.conf:/etc/nginx/nginx.conf
  depends_on:
    - frontend
    - backend
```

### 5. Enable HTTPS

Use Let's Encrypt with nginx or Traefik.

---

## 📊 Monitoring

### View logs

```bash
# All services
docker-compose logs -f

# Specific service with timestamps
docker-compose logs -f --timestamps backend

# Last N lines
docker-compose logs --tail=100 backend
```

### Resource usage

```bash
# Real-time stats
docker stats

# Specific containers
docker stats blearning-backend blearning-frontend blearning-db
```

### Health checks

```bash
# Check health status
docker-compose ps

# Manually check endpoints
curl http://localhost:8000/health
curl http://localhost:3000
```

---

## 🗂️ File Structure

```
B-Learning-Demo/
├── docker-compose.yml        # Orchestration file
├── .env.docker               # Environment template
│
├── backend/
│   ├── Dockerfile            # Python FastAPI image
│   └── .dockerignore
│
├── frontend/
│   ├── Dockerfile            # Next.js image (multi-stage)
│   └── .dockerignore
│
└── db/
    ├── Dockerfile            # PostgreSQL image
    └── init/                 # SQL initialization scripts
        ├── 01-schema.sql
        ├── 02-indexes.sql
        ├── 03-constraints.sql
        └── 04-seed-data.sql
```

---

## 🧪 Testing

### Test each service

```bash
# Database
docker-compose exec db psql -U postgres -d b_learning -c "SELECT COUNT(*) FROM \"User\";"

# Backend
curl http://localhost:8000/health
curl http://localhost:8000/api/courses

# Frontend
curl -I http://localhost:3000
```

### Integration test

```bash
# Create a course via API
curl -X POST http://localhost:8000/api/courses \
  -H "Content-Type: application/json" \
  -d '{"code":"TEST","title":"Test Course","status":"DRAFT"}'

# View in browser
# http://localhost:3000/courses
```

---

## 📝 Notes

### First run
- Database initialization takes ~30-60 seconds
- Backend waits for DB to be healthy
- Frontend waits for backend to be healthy
- Total startup time: ~1-2 minutes

### Data persistence
- Database data is stored in Docker volume `postgres_data`
- Survives `docker-compose down`
- Only deleted with `docker-compose down -v`

### Updates
```bash
# Pull latest code
git pull

# Rebuild and restart
docker-compose up -d --build

# Force recreate containers
docker-compose up -d --force-recreate
```

### Development mode
For development with hot-reload, use local setup (not Docker).

---

## 🆘 Support

### Logs location
- Docker logs: `docker-compose logs`
- Container logs: `docker logs <container-name>`

### Common issues
1. Port conflicts → Change ports in docker-compose.yml
2. Out of memory → Increase Docker memory limit
3. Slow startup → Check logs for errors

### Reset everything
```bash
# Nuclear option - remove everything
docker-compose down -v
docker system prune -a --volumes
docker-compose up -d --build
```

---

**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Date**: 2025-11-28
