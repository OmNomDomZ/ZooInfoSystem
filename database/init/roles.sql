CREATE ROLE zoo_reader LOGIN PASSWORD 'reader_pass';

-- ► база, на которой работает приложение
GRANT ALL PRIVILEGES ON DATABASE zoo_db   TO zoo_admin;
GRANT CONNECT          ON DATABASE zoo_db TO zoo_reader;

\c zoo_db                                                    -- ← подключились

-- существующие объекты
GRANT ALL PRIVILEGES ON ALL TABLES    IN SCHEMA public TO zoo_admin;
GRANT USAGE,SELECT  ON ALL SEQUENCES IN SCHEMA public TO zoo_admin;
GRANT SELECT        ON ALL TABLES    IN SCHEMA public TO zoo_reader;
GRANT USAGE         ON ALL SEQUENCES IN SCHEMA public TO zoo_reader;

-- будущее (чтобы не забывать)
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT               ON TABLES    TO zoo_reader;