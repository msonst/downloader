#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE dbdownload;
    GRANT ALL PRIVILEGES ON DATABASE dbdownload TO dbuser;
EOSQL
