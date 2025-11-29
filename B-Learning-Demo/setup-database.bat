@echo off
echo ==========================================
echo B-Learning Database Setup Script
echo ==========================================
echo.

echo Step 1: Checking if PostgreSQL is installed...
where psql >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] PostgreSQL is not installed or not in PATH
    echo Please install PostgreSQL 14+ from: https://www.postgresql.org/download/windows/
    echo.
    echo After installation, add PostgreSQL bin directory to your PATH
    echo Example: C:\Program Files\PostgreSQL\15\bin
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL found

echo.
echo Step 2: Testing connection to PostgreSQL...
psql -U postgres -c "SELECT version();" >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Cannot connect to PostgreSQL
    echo Please make sure:
    echo 1. PostgreSQL service is running
    echo 2. Password for 'postgres' user is 'vkiet432'
    echo 3. PostgreSQL is accepting connections on localhost:5432
    echo.
    echo To set password: ALTER USER postgres PASSWORD 'vkiet432';
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL connection successful

echo.
echo Step 3: Creating database 'b_learning'...
createdb -U postgres -h localhost b_learning 2>nul
if %errorlevel% equ 0 (
    echo [OK] Database 'b_learning' created successfully
) else (
    echo [INFO] Database 'b_learning' might already exist
)

echo.
echo Step 4: Running SQL scripts...
echo [1/4] Creating schema...
psql -U postgres -d b_learning -f "../98-B-Learing-Core/sql/01-schema.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create schema
    pause
    exit /b 1
)

echo [2/4] Creating indexes...
psql -U postgres -d b_learning -f "../98-B-Learing-Core/sql/02-indexes.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create indexes
    pause
    exit /b 1
)

echo [3/4] Creating constraints...
psql -U postgres -d b_learning -f "../98-B-Learing-Core/sql/03-constraints.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create constraints
    pause
    exit /b 1
)

echo [4/4] Inserting seed data...
psql -U postgres -d b_learning -f "../98-B-Learing-Core/sql/04-seed-data.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to insert seed data
    pause
    exit /b 1
)

echo.
echo ==========================================
echo SUCCESS! Database setup completed
echo ==========================================
echo.
echo Database: b_learning
echo Host: localhost:5432
echo User: postgres
echo Password: vkiet432
echo.
echo You can now start the backend server:
echo cd backend
echo python -m uvicorn app.main:app --reload
echo.
pause