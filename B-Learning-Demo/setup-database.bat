@echo off
echo ==========================================
echo B-Learning Database Setup Script
echo ==========================================
echo.

echo Step 1: Checking if PostgreSQL is installed...

REM Check if psql is in PATH
where psql >nul 2>nul
if %errorlevel% equ 0 (
    echo [OK] PostgreSQL found in PATH
    set PSQL_CMD=psql
    set CREATEDB_CMD=createdb
    goto :connection_test
)

echo [INFO] PostgreSQL not in PATH, searching common installation directories...

REM Check common PostgreSQL installation paths
set PG_FOUND=0
for /d %%G in ("C:\Program Files\PostgreSQL\*") do (
    if exist "%%G\bin\psql.exe" (
        echo [OK] PostgreSQL found at: %%G
        set "PSQL_CMD=%%G\bin\psql.exe"
        set "CREATEDB_CMD=%%G\bin\createdb.exe"
        set PG_FOUND=1
        goto :connection_test
    )
)

REM Check Program Files (x86)
for /d %%G in ("C:\Program Files (x86)\PostgreSQL\*") do (
    if exist "%%G\bin\psql.exe" (
        echo [OK] PostgreSQL found at: %%G
        set "PSQL_CMD=%%G\bin\psql.exe"
        set "CREATEDB_CMD=%%G\bin\createdb.exe"
        set PG_FOUND=1
        goto :connection_test
    )
)

if %PG_FOUND% equ 0 (
    echo [ERROR] PostgreSQL is not installed or not found
    echo Please install PostgreSQL 14+ from: https://www.postgresql.org/download/windows/
    echo.
    echo After installation, you can either:
    echo 1. Add PostgreSQL bin directory to your PATH
    echo    Example: C:\Program Files\PostgreSQL\15\bin
    echo 2. Run this script again (it will auto-detect PostgreSQL)
    echo.
    pause
    exit /b 1
)

:connection_test
echo.
echo Step 2: Testing connection to PostgreSQL...
"%PSQL_CMD%" -U postgres -c "SELECT version();" >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Cannot connect to PostgreSQL
    echo Please make sure:
    echo 1. PostgreSQL service is running
    echo 2. Password for 'postgres' user is 'vkiet432'
    echo 3. PostgreSQL is accepting connections on localhost:5432
    echo.
    echo To set password, run:
    echo   "%PSQL_CMD%" -U postgres
    echo Then execute: ALTER USER postgres PASSWORD 'vkiet432';
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL connection successful

echo.
echo Step 3: Checking if database 'b_learning' exists...
"%PSQL_CMD%" -U postgres -lqt | findstr /C:"b_learning" >nul 2>nul
if %errorlevel% equ 0 (
    echo [WARNING] Database 'b_learning' already exists
    echo [ACTION] Dropping existing database and recreating...
    "%PSQL_CMD%" -U postgres -c "DROP DATABASE IF EXISTS b_learning WITH (FORCE);" >nul 2>nul
    if %errorlevel% equ 0 (
        echo [OK] Old database dropped successfully
    ) else (
        echo [WARNING] Could not drop database, trying to terminate connections...
        "%PSQL_CMD%" -U postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'b_learning' AND pid <> pg_backend_pid();" >nul 2>nul
        "%PSQL_CMD%" -U postgres -c "DROP DATABASE IF EXISTS b_learning;" >nul 2>nul
    )
)

echo [ACTION] Creating database 'b_learning'...
"%CREATEDB_CMD%" -U postgres -h localhost b_learning 2>nul
if %errorlevel% equ 0 (
    echo [OK] Database 'b_learning' created successfully
) else (
    echo [ERROR] Failed to create database
    pause
    exit /b 1
)

echo.
echo Step 4: Running SQL scripts...
echo [1/4] Creating schema...
"%PSQL_CMD%" -U postgres -d b_learning -f "../98-B-Learing-Core/sql/01-schema.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create schema
    pause
    exit /b 1
)

echo [2/4] Creating indexes...
"%PSQL_CMD%" -U postgres -d b_learning -f "../98-B-Learing-Core/sql/02-indexes.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create indexes
    pause
    exit /b 1
)

echo [3/4] Creating constraints...
"%PSQL_CMD%" -U postgres -d b_learning -f "../98-B-Learing-Core/sql/03-constraints.sql"
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create constraints
    pause
    exit /b 1
)

echo [4/4] Inserting seed data...
"%PSQL_CMD%" -U postgres -d b_learning -f "../98-B-Learing-Core/sql/04-seed-data.sql"
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
