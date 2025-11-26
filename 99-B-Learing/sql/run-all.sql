-- Master runner: nạp toàn bộ script theo thứ tự
-- Chạy từ thư mục project root (ví dụ: F:\nam4\DesignPattern\99-B-Learing)
-- Sử dụng: psql -U postgres -d blelearning_db -f sql/run-all.sql

\i 'sql/01-schema.sql'
\i 'sql/02-indexes.sql'
\i 'sql/03-constraints.sql'
\i 'sql/04-triggers.sql'
\i 'sql/05-views.sql'
\i 'sql/06-seed-data.sql'
