CREATE ROLE zoo_admin LOGIN PASSWORD 'admin_pass';
CREATE ROLE zoo_reader LOGIN PASSWORD 'reader_pass';

-- ► база, на которой работает приложение
GRANT ALL PRIVILEGES ON DATABASE zoo_db TO zoo_admin;
GRANT CONNECT ON DATABASE zoo_db TO zoo_reader;

\c zoo_db

-- существующие объекты
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO zoo_admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO zoo_admin;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO zoo_reader;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO zoo_reader;

-- задаёт привилегии по умолчанию для новых объектов, создаваемых в указанном схеме
-- если создана новая таблица, то zoo_reader автоматически получает на неё право SELECT
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT ON TABLES TO zoo_reader;