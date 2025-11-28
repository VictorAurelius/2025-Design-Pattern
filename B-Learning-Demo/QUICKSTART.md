# 🚀 Quick Start Guide

Hướng dẫn nhanh để chạy demo trong 5 phút.

## ✅ Checklist

- [ ] Python 3.11+ đã cài đặt
- [ ] Node.js 18+ đã cài đặt
- [ ] PostgreSQL 14+ đang chạy
- [ ] Database B-Learning đã setup

## 📋 Các bước

### 1️⃣ Setup Backend (2 phút)

```bash
# Mở terminal 1
cd B-Learning-Demo/backend

# Tạo virtual environment
python -m venv venv

# Activate (chọn OS của bạn)
source venv/bin/activate        # Linux/Mac
venv\Scripts\activate           # Windows

# Install packages
pip install -r requirements.txt

# Tạo .env
cp .env.example .env

# ⚠️ QUAN TRỌNG: Sửa .env với thông tin database
# DB_NAME=b_learning
# DB_USER=postgres
# DB_PASSWORD=your_password
```

**Chạy backend:**
```bash
python -m uvicorn app.main:app --reload
```

✅ Backend chạy tại: **http://localhost:8000**
📖 API Docs: **http://localhost:8000/docs**

---

### 2️⃣ Setup Frontend (2 phút)

```bash
# Mở terminal 2 (terminal mới)
cd B-Learning-Demo/frontend

# Install packages
npm install

# Tạo .env.local
echo "NEXT_PUBLIC_API_URL=http://localhost:8000" > .env.local
```

**Chạy frontend:**
```bash
npm run dev
```

✅ Frontend chạy tại: **http://localhost:3000**

---

### 3️⃣ Sử dụng (1 phút)

1. Mở browser: **http://localhost:3000**

2. **Page 1 - Courses:**
   - Click "Courses" trên navbar
   - Click "Tạo Course Mới"
   - Điền form và Submit
   - Thử Search, Filter, Edit, Delete

3. **Page 2 - Submissions:**
   - Click "Submissions" trên navbar
   - Xem danh sách submissions
   - Click "Grade" để chấm điểm
   - Thử các filters

---

## 🐛 Gặp lỗi?

### Backend không start?
```bash
# Kiểm tra PostgreSQL
psql -U postgres -d b_learning

# Nếu lỗi "database not found"
# → Tạo database hoặc check DB_NAME trong .env
```

### Frontend lỗi "Cannot connect to backend"?
```bash
# Kiểm tra backend đang chạy:
curl http://localhost:8000/health

# Kiểm tra .env.local
cat frontend/.env.local
# → Phải có: NEXT_PUBLIC_API_URL=http://localhost:8000
```

### Port đã sử dụng?
```bash
# Backend (8000): Đổi port trong .env
API_PORT=8001

# Frontend (3000): Chạy với port khác
npm run dev -- -p 3001
```

---

## 📊 Test nhanh

### Test Backend
```bash
# Health check
curl http://localhost:8000/health

# Get courses
curl http://localhost:8000/api/courses

# Create course
curl -X POST http://localhost:8000/api/courses \
  -H "Content-Type: application/json" \
  -d '{"code":"TEST101","title":"Test Course","status":"DRAFT"}'
```

### Test Frontend
- Home: http://localhost:3000
- Courses: http://localhost:3000/courses
- Submissions: http://localhost:3000/submissions

---

## 📝 Notes

- Backend cần chạy **trước** Frontend
- Cả 2 servers phải chạy **cùng lúc**
- Nếu thay đổi backend code → auto-reload (FastAPI)
- Nếu thay đổi frontend code → auto-reload (Next.js)

---

**Chúc bạn demo thành công! 🎉**

Nguyễn Văn Kiệt - CNTT1-K63
