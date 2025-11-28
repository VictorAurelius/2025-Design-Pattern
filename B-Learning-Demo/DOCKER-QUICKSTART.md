# 🐳 Docker Quick Start (30 giây)

## 1️⃣ Yêu cầu
- Docker & Docker Compose đã cài đặt
- 4GB RAM free

## 2️⃣ Chạy

```bash
cd B-Learning-Demo

# Start all services
docker-compose up -d --build

# Xem logs (Ctrl+C để thoát)
docker-compose logs -f
```

**Chờ ~2 phút** để tất cả services khởi động.

## 3️⃣ Truy cập

| Service | URL |
|---------|-----|
| 🌐 **Frontend** | http://localhost:3000 |
| 🔌 **Backend API** | http://localhost:8000 |
| 📖 **API Docs** | http://localhost:8000/docs |
| 🗄️ **Database** | localhost:5432 |

## 4️⃣ Test

```bash
# Test backend
curl http://localhost:8000/health

# Test database
docker-compose exec db psql -U postgres -d b_learning -c "SELECT COUNT(*) FROM \"User\";"
```

## 5️⃣ Stop

```bash
# Stop (giữ data)
docker-compose stop

# Stop và xóa containers (giữ data)
docker-compose down

# Stop và xóa tất cả (mất data!)
docker-compose down -v
```

---

## 🔥 Makefile Commands

Nếu hệ thống hỗ trợ `make`:

```bash
make up          # Start
make logs        # View logs
make test        # Test all services
make down        # Stop
make clean       # Remove all
```

---

## 🐛 Lỗi?

### Port đã sử dụng?
```bash
# Đổi port trong docker-compose.yml
ports:
  - "3001:3000"  # Frontend
  - "8001:8000"  # Backend
  - "5433:5432"  # Database
```

### Container restart liên tục?
```bash
# Xem logs
docker-compose logs -f <service-name>

# Service names: db, backend, frontend
```

### Reset toàn bộ?
```bash
docker-compose down -v
docker system prune -a
docker-compose up -d --build
```

---

## 📖 Chi tiết

Xem [DOCKER.md](./DOCKER.md) để biết thêm chi tiết về:
- Architecture
- Commands
- Troubleshooting
- Production setup

---

**That's it! 🎉**

Nguyễn Văn Kiệt - CNTT1-K63
