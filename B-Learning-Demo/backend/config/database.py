"""
Database Connection Module
==========================

Quản lý kết nối PostgreSQL sử dụng psycopg2.
Sử dụng connection pooling để tối ưu hiệu suất.

Author: Nguyễn Văn Kiệt - CNTT1-K63
Date: 2025-11-28
"""

import os
import psycopg2
from psycopg2 import pool
from psycopg2.extras import RealDictCursor
from dotenv import load_dotenv
from contextlib import contextmanager

# Load environment variables
load_dotenv()

# Database configuration
DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'port': os.getenv('DB_PORT', '5432'),
    'database': os.getenv('DB_NAME', 'b_learning'),
    'user': os.getenv('DB_USER', 'postgres'),
    'password': os.getenv('DB_PASSWORD', ''),
}

# Connection pool
# Min connections: 1, Max connections: 10
connection_pool = None


def init_connection_pool():
    """
    Khởi tạo connection pool khi ứng dụng start.
    Connection pool giúp tái sử dụng kết nối database, tăng hiệu suất.
    """
    global connection_pool
    try:
        connection_pool = psycopg2.pool.SimpleConnectionPool(
            minconn=1,
            maxconn=10,
            **DB_CONFIG
        )
        print("✅ Database connection pool created successfully")
        print(f"📊 Connected to: {DB_CONFIG['database']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}")
    except Exception as e:
        print(f"❌ Error creating connection pool: {e}")
        raise


def close_connection_pool():
    """
    Đóng tất cả kết nối trong pool khi ứng dụng shutdown.
    """
    global connection_pool
    if connection_pool:
        connection_pool.closeall()
        print("🔌 Database connection pool closed")


@contextmanager
def get_db_connection():
    """
    Context manager để lấy connection từ pool.

    Usage:
        with get_db_connection() as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM users")
            results = cursor.fetchall()

    Tự động trả connection về pool sau khi sử dụng.
    """
    connection = None
    try:
        connection = connection_pool.getconn()
        yield connection
    except Exception as e:
        if connection:
            connection.rollback()
        raise e
    finally:
        if connection:
            connection_pool.putconn(connection)


@contextmanager
def get_db_cursor(commit=True):
    """
    Context manager để lấy cursor với RealDictCursor.

    Args:
        commit (bool): Tự động commit sau khi thực thi (mặc định: True)

    Usage:
        with get_db_cursor() as cursor:
            cursor.execute("SELECT * FROM users WHERE email = %s", (email,))
            user = cursor.fetchone()

    RealDictCursor trả về kết quả dạng dict thay vì tuple.
    """
    with get_db_connection() as conn:
        cursor = conn.cursor(cursor_factory=RealDictCursor)
        try:
            yield cursor
            if commit:
                conn.commit()
        except Exception as e:
            conn.rollback()
            raise e
        finally:
            cursor.close()


def execute_query(query: str, params: tuple = None, fetch: str = 'all'):
    """
    Thực thi SQL query và trả về kết quả.

    Args:
        query (str): SQL query string (sử dụng %s cho parameters)
        params (tuple): Query parameters để tránh SQL injection
        fetch (str): 'all' | 'one' | 'none'
            - 'all': fetchall() - trả về list of dicts
            - 'one': fetchone() - trả về single dict
            - 'none': không fetch (INSERT, UPDATE, DELETE)

    Returns:
        List[dict] | dict | None

    Example:
        # SELECT all
        courses = execute_query(
            "SELECT * FROM \"Course\" WHERE status = %s",
            ('PUBLISHED',),
            fetch='all'
        )

        # SELECT one
        course = execute_query(
            "SELECT * FROM \"Course\" WHERE course_id = %s",
            (course_id,),
            fetch='one'
        )

        # INSERT
        execute_query(
            "INSERT INTO \"Course\" (code, title) VALUES (%s, %s)",
            (code, title),
            fetch='none'
        )
    """
    with get_db_cursor() as cursor:
        cursor.execute(query, params)

        if fetch == 'all':
            return cursor.fetchall()
        elif fetch == 'one':
            return cursor.fetchone()
        elif fetch == 'none':
            return None
        else:
            raise ValueError(f"Invalid fetch mode: {fetch}")


def execute_query_with_returning(query: str, params: tuple = None):
    """
    Thực thi INSERT/UPDATE query và trả về row được tạo/update.

    Yêu cầu query phải có RETURNING clause.

    Example:
        new_course = execute_query_with_returning(
            '''
            INSERT INTO "Course" (code, title, description)
            VALUES (%s, %s, %s)
            RETURNING *
            ''',
            (code, title, description)
        )
    """
    with get_db_cursor() as cursor:
        cursor.execute(query, params)
        return cursor.fetchone()


# Test connection
if __name__ == "__main__":
    print("🧪 Testing database connection...")
    init_connection_pool()

    try:
        result = execute_query("SELECT version()", fetch='one')
        print(f"✅ PostgreSQL Version: {result['version']}")

        # Test counting tables
        table_count = execute_query(
            """
            SELECT COUNT(*) as count
            FROM information_schema.tables
            WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
            """,
            fetch='one'
        )
        print(f"📊 Total tables in database: {table_count['count']}")

    finally:
        close_connection_pool()
