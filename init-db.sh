#!/bin/bash
set -e

# Выполнение исходного entrypoint скрипта для PostgreSQL
/docker-entrypoint.sh postgres &

# Подождите, пока PostgreSQL запустится
until pg_isready -h localhost -p 5432 -U "$POSTGRES_USER"; do
  echo "Waiting for PostgreSQL to start..."
  sleep 2
done

# Выполните schema.sql для пересоздания таблиц
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /docker-entrypoint-initdb.d/schema.sql

# Подождите, пока исходный процесс PostgreSQL завершится
wait