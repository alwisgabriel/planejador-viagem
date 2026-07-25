#!/usr/bin/env bash
set -e

echo "=== Subindo PostgreSQL ==="
docker compose up -d

echo ""
if [ -z "$GROQ_API_KEY" ]; then
  echo "AVISO: Variavel GROQ_API_KEY nao definida."
  echo "O sistema vai usar o modo InMemory (roteiro fixo)."
  echo ""
  echo "Para usar IA de verdade, rode:"
  echo "  export GROQ_API_KEY=sua-chave-aqui"
  echo "  ./run.sh"
  echo ""
fi

echo "=== Rodando aplicacao ==="
mvn spring-boot:run
