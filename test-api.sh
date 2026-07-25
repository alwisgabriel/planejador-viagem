#!/usr/bin/env bash
BASE="http://localhost:8080"

echo "=== 1. Registrando ==="
RESP=$(curl -s -X POST "$BASE/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@email.com","password":"123456"}')
echo "$RESP"

TOKEN=$(echo "$RESP" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
[ -z "$TOKEN" ] && TOKEN=$(curl -s -X POST "$BASE/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@email.com","password":"123456"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

echo "TOKEN=$TOKEN"

echo ""
echo "=== 2. Criando viagem ==="
TRIP_RESP=$(curl -s -X POST "$BASE/trips" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Europa 2026","startDate":"2026-08-01","endDate":"2026-08-15","budget":5000}')
echo "$TRIP_RESP"

TRIP_ID=$(echo "$TRIP_RESP" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "TRIP_ID=$TRIP_ID"

echo ""
echo "=== 3. Adicionando destino ==="
curl -s -X POST "$BASE/trips/$TRIP_ID/destinations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"city":"Paris","country":"Franca","displayOrder":1}'

echo ""
echo ""
echo "=== 4. Gerando plano ==="
curl -s -X POST "$BASE/trips/$TRIP_ID/plan" \
  -H "Authorization: Bearer $TOKEN" | python3 -c "
import sys, json
d = json.load(sys.stdin)
print()
print(d['content'])
"
